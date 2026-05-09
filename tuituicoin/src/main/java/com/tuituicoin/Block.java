package com.tuituicoin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import com.tuituicoin.util.Hash;

public class Block {
    private String hash;
    private String prevHash;
    private Transaction transaction;
    private Instant timestamp = Instant.now();
    private byte[] signature = null;

    private long nonce = Math.round(Math.random() * 999999999);

    public Block(String prevHash, Transaction transaction, byte[] signature) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        this.prevHash = prevHash;
        this.transaction = transaction;
        this.hash = calculateHash();
        setSignature(signature);
    }

    public String calculateHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String input = prevHash + timestamp.toString() + transaction.toString();
        return hash(input); 
    }

    private void setSignature(byte[] signature) {
        if (this.signature == null) {
            this.signature = signature;
        }
    }

    public static String hash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return Hash.hash(input, "SHA256");
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
