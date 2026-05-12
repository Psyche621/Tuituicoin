package com.tuituicoin.blockchain;

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

    public void sendMoney(long amount, Block block, PublicKey payeePublicKey) throws Exception {
        /* Transaction transaction = new Transaction(amount, block.getHash(), this.publicKey, payeePublicKey);
        byte[] signature = sign(transaction.serialize(), this.privateKey);
        Chain.getInstance().addBlock(transaction, this.publicKey, transaction); */

        Transaction transaction = new Transaction(amount, block.getHash(), this.publicKey, payeePublicKey);
        transaction.sign(this.privateKey);
        Chain.getInstance().addBlock(transaction);
    }
}
