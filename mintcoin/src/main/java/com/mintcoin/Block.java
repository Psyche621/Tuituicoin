package com.mintcoin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import com.mintcoin.util.Hash;

public class Block {
    private String prevHash;
    private Transaction transaction;
    private Instant timestamp = Instant.now();
    private byte[] signature = null;

    private long nonce = Math.round(Math.random() * 999999999);

    public Block(String prevHash, Transaction transaction, byte[] signature) {
        this.prevHash = prevHash;
        this.transaction = transaction;
        setSignature(signature);
    }

    private void setSignature(byte[] signature) {
        if (this.signature == null) {
            this.signature = signature;
        }
    }

    public static String hash() throws NoSuchAlgorithmException {
        return Hash.hash("SHA256");
    }
}
