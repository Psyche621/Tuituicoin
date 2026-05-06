package com.mintcoin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class Wallet {
    private String publicKeyString;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKeyString = keyPair.getPublic().toString();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void sendMoney(long amount, PublicKey payeePublicKey) throws Exception {
        Transaction transaction = new Transaction(amount, this.publicKey.toString(), payeePublicKey.toString());
        byte[] signature = sign(transaction.toString(), this.privateKey);
        Chain.getInstance().addBlock(transaction, payeePublicKey, signature);
    }

    private static byte[] sign(String input, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(input.getBytes());
        return sign.sign();
    }
}
