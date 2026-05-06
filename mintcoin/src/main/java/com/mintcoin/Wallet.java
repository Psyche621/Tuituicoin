package com.mintcoin;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Wallet {
    private String publicKey;
    private String privateKey;

    public Wallet() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic().toString();
        privateKey = keyPair.getPrivate().toString();
    }

    public void sendMoney(long amount, String payeePublicKey) {
        Transaction transaction = new Transaction(amount, this.publicKey, payeePublicKey);
        String sign = 
    }

    private static String sign(String input, String privateKeyPEM) throws Exception {
        String cleanKey = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                                        .replace("-----END PRIVATE KEY-----", "")
                                        .replace("\n", "");
        
        byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(kf.generatePrivate(spec));
        
        signature.update(input.getBytes("UTF-8"));

        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}
