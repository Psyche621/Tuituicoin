package com.mintcoin;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;

import com.mintcoin.util.Hash;

public class Chain {
    private static Chain instance = new Chain();
    private ArrayList<Block> chain;

    public Chain() {
        chain.add(new Block(null, new Transaction(100, "Mike", "Bob"), null));
    }

    public static synchronized Chain getInstance() {
        return instance;
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Transaction transaction, PublicKey senderPublicKey, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(senderPublicKey);
        verifier.update(signature);
        if (verifier.verify(signature)) { 
            Block block = new Block(getLastBlock().hash(), transaction, signature);
            chain.add(block);
        }
    }

    public void mine(long nonce) throws NoSuchAlgorithmException {
        long solution = 1;

        while (true) {
            String hash = Hash.hash("MD5");
            
        }
    }
}
