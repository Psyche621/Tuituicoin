package com.tuituicoin;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.tuituicoin.util.Hash;

public class Block {
    private String hash;
    private String prevHash;
    private List<Transaction> transactions = new ArrayList<>();
    private Instant timestamp = Instant.now();
    private byte[] signature = null;
    private long nonce = 0;

    public Block(String prevHash, Transaction transaction, byte[] signature) {
        this.prevHash = prevHash;
        transactions.add(transaction);
        this.hash = calculateHash();
        setSignature(signature);
    }

    public String calculateHash() {
        String input = prevHash + timestamp.toString() + transactions.toString() + nonce;
        return hash(input); 
    }

    private void setSignature(byte[] signature) {
        if (this.signature == null) {
            this.signature = signature;
        }
    }

    public static String hash(String input) {
        return Hash.hash(input, "SHA256");
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void mine(int difficulty) throws NoSuchAlgorithmException {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }
}
