package com.tuituicoin.blockchain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class Wallet {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    private static final Logger LOGGER = Logger.getLogger(Wallet.class.getName());

    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;

    /* Public constructor for generating a new wallet */
    public Wallet() throws NoSuchAlgorithmException {
        LOGGER.info("Generating new wallet key pair.");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        LOGGER.info("Wallet key pair generated successfully.");
    }

    /* Private Wallet loader from file */
    private Wallet(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void sendMoney(long amount, Block block, PublicKey payeePublicKey) throws Exception {
        LOGGER.info("Creating transaction to send " + amount + " coins to recipient.");
        Transaction transaction = new Transaction(amount, block.getHash(), this.publicKey, payeePublicKey);
        transaction.sign(this.privateKey);
        LOGGER.info("Sending money from wallet.");
        Chain.getInstance().addBlock(transaction);
    }

    /* Encrypt and save wallet to file using AES/GCM */
    public void save(String fileName, String password, boolean overwrite) {
        LOGGER.info("Saving wallet to file.");

        // Empty filename check
        // Default filenames are generated in WalletCreateCommand, so this should only be hit when
        // reconstructing a wallet from an existing file and saving it back to disk without providing a filename
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.severe("Filename cannot be null or empty. Please provide a valid filename.");
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        // Prepend wallets directory to filename
        fileName = "wallets/" + fileName;

        // TODO: Prompt user for confirmation if file already exists and overwrite is false, instead of just generating a new name
        if (WalletManager.exists(fileName) && !overwrite) {
            LOGGER.warning("Wallet " + fileName + " already exists. Generating new wallet name.");
            fileName = WalletManager.generateWalletName();
        }

        // Password check. This should be hit when creating a new wallet without providing a password, or when loading an existing 
        // wallet and saving it back to disk without providing a password
        if (password == null || password.isEmpty()) {
            LOGGER.severe("No password provided for wallet encryption. Please provide a password");
            System.err.println("No password provided for wallet encryption. Please provide a password");
            return;
        }        

        char[] passwordChars = password.toCharArray();

        // Encrypt wallet data and save to file
        try (OutputStream out = Files.newOutputStream(Paths.get(fileName))) {
            JSONObject json = this.toJSON();
            byte[] walletBytes = json.toString().getBytes();
            SecureRandom random = new SecureRandom();

            // Salt for key derivation. 
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // IV for encryption. 
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);
            
            // Derive AES key from password and salt
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, 65536, 256);

            // Generate AES key from password
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Encrypt wallet data using AES/GCM
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new javax.crypto.spec.GCMParameterSpec(128, iv));
            byte[] encrypted = cipher.doFinal(walletBytes);

            // Write salt, IV, and encrypted data to file
            byte[] output = new byte[salt.length + iv.length + encrypted.length];
            // Concatenate salt, IV, and encrypted data into a single byte array for storage
            System.arraycopy(salt, 0, output, 0, salt.length);
            System.arraycopy(iv, 0, output, salt.length, iv.length);
            System.arraycopy(encrypted, 0, output, salt.length + iv.length, encrypted.length);

            out.write(output);

            LOGGER.info("Wallet saved successfully.");
        } catch (IOException e) {
            LOGGER.severe("Failed to save wallet: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe("Encryption algorithm not found: " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            LOGGER.severe("Invalid key specification: " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            LOGGER.severe("Invalid encryption key: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            LOGGER.severe("No such padding scheme: " + e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.severe("Invalid algorithm parameters: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.severe("Unexpected error while saving wallet: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clear password from memory
            Arrays.fill(passwordChars, '\0');
        }
    }

    /* Load and decrypt wallet from file using AES/GCM/NoPadding. 
     * Returns the loaded wallet or null if loading fails. */
    public static Wallet load(String filename, String password) {
        if (filename == null || filename.isEmpty()) {
            LOGGER.info("No filename provided, using default 'wallet.dat'.");
            filename = "wallet.dat";
        }

        if (password == null || password.isEmpty()) {
            LOGGER.severe("No password provided for wallet decryption. Please provide a password");
            System.err.println("No password provided for wallet decryption. Please provide a password");
            return null;
        }

        LOGGER.info("Loading wallet from file '" + filename + "'.");

        try {
            // Read entire wallet file into byte array
            byte[] fileBytes = Files.readAllBytes(Paths.get(filename));

            // Extract salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(fileBytes, 0, salt, 0, SALT_LENGTH);

            // Extract IV
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(fileBytes, SALT_LENGTH, iv, 0, IV_LENGTH);

            // Extract encrypted data
            int encryptedStart = SALT_LENGTH + IV_LENGTH;
            // The remaining bytes after salt and IV are the encrypted wallet data
            byte[] encrypted = new byte[fileBytes.length - encryptedStart];
            // Copy encrypted data from fileBytes into encrypted array
            System.arraycopy(fileBytes, encryptedStart, encrypted, 0, encrypted.length);

            // Recreate AES key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

            // Generate AES key from password
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Decrypt wallet data
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new javax.crypto.spec.GCMParameterSpec(128, iv));
            // Decrypt the encrypted wallet data using the cipher and store the result in decrypted byte array
            byte[] decrypted = cipher.doFinal(encrypted);

            JSONObject json = new JSONObject(new String(decrypted));

            // Rebuild keys
            byte[] publicKeyBytes = Base64.getDecoder().decode(json.getString("public_key"));
            byte[] privateKeyBytes = Base64.getDecoder().decode(json.getString("private_key"));

            // Use KeyFactory to reconstruct PublicKey and PrivateKey objects from their byte representations
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            LOGGER.info("Wallet loaded successfully.");
            return new Wallet(publicKey, privateKey);
        } catch (IOException e) {
            LOGGER.severe("Failed to read wallet file: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe("Encryption algorithm not found: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (InvalidKeySpecException e) {
            LOGGER.severe("Invalid key specification: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            LOGGER.severe("Invalid encryption key: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            LOGGER.severe("No such padding scheme: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.severe("Invalid algorithm parameters: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            LOGGER.severe("Unexpected error while saving wallet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("public_key", Base64.getEncoder().encodeToString(this.publicKey.getEncoded()));
        json.put("private_key", Base64.getEncoder().encodeToString(this.privateKey.getEncoded()));
        return json;
    }
}
