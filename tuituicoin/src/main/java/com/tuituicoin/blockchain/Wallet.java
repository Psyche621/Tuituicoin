package com.tuituicoin.blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Logger;
import org.json.JSONObject;

public class Wallet {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    private static final Logger LOGGER = Logger.getLogger(Wallet.class.getName());

    public Wallet() throws NoSuchAlgorithmException {
        LOGGER.info("Generating new wallet key pair.");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        LOGGER.info("Wallet key pair generated successfully.");
    }

    public void sendMoney(long amount, Block block, PublicKey payeePublicKey) throws Exception {
        LOGGER.info("Creating transaction to send " + amount + " coins to recipient.");
        Transaction transaction = new Transaction(amount, block.getHash(), this.publicKey, payeePublicKey);
        transaction.sign(this.privateKey);
        LOGGER.info("Sending money from wallet.");
        Chain.getInstance().addBlock(transaction);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void save() {
        // TODO: Implement wallet saving to file logic
    }

    private JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("public_key", Base64.getEncoder().encodeToString(this.publicKey.getEncoded()));
        json.put("private_key", Base64.getEncoder().encodeToString(this.privateKey.getEncoded()));
        return json;
    }
}
