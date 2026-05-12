package com.tuituicoin.blockchain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class Wallet {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Wallet() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void sendMoney(long amount, Block block, PublicKey payeePublicKey) throws Exception {
        Transaction transaction = new Transaction(amount, block.getHash(), this.publicKey, payeePublicKey);
        transaction.sign(this.privateKey);
        Chain.getInstance().addBlock(transaction);
    }

    public JSONPObject toJSON() {
        // TODO: implement for saving wallet to file
        return null;
    }
}
