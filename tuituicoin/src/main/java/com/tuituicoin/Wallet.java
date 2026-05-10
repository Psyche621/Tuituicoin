package com.tuituicoin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class Wallet {
    private final String publicKeyString;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()); 
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void sendMoney(long amount, PublicKey payeePublicKey) throws Exception {
        Transaction transaction = new Transaction(amount, this.publicKey, payeePublicKey);
        byte[] signature = sign(transaction.serialize(), this.privateKey);
        Chain.getInstance().addBlock(transaction, this.publicKey, signature);
    }

    private static byte[] sign(String input, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(input.getBytes());
        return sign.sign();
    }
}
