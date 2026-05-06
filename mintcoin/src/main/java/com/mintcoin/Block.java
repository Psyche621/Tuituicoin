package com.mintcoin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class Block {
    private String prevHash;
    private Transaction transaction;
    private Instant timestamp = Instant.now();

    public Block(String prevHash, Transaction transaction) {
        this.prevHash = prevHash;
        this.transaction = transaction;
    }

    public String hash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
