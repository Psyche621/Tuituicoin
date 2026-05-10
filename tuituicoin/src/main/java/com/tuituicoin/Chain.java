package com.tuituicoin;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;

public class Chain {
    private static Chain instance;
    private ArrayList<Block> chain = new ArrayList<>();
    private Wallet genesisWallet;

    public Chain() throws NoSuchAlgorithmException {
        genesisWallet = new Wallet();
        getInstance();
        // Transaction genesistTransaction = new Transaction(100, null, )
        // chain.add(new Block("0", new Transaction(100, "Mike", "Bob"), null));
    }

    public static synchronized Chain getInstance() {
        try {
            if (instance == null) {
                instance = new Chain();
            }
            
            return instance;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm in creating chain instance");
        }

        return instance;
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Transaction transaction, PublicKey senderPublicKey, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(senderPublicKey);
        String data = transaction.toString();
        verifier.update(data.getBytes(StandardCharsets.UTF_8));
        if (verifier.verify(signature)) { 
            Block block = new Block(getLastBlock().getHash(), transaction, signature);
            chain.add(block);
        }
    }

    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getHash().equals(current.calculateHash())) {
                return false;
            }

            if (!current.getPrevHash().equals(previous.getHash())) {
                return false;
            }
        }

        return true;
    }
}
