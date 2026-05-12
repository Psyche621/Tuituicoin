package com.tuituicoin.blockchain;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.tuituicoin.util.Hash;

public class Block {
    private String hash;
    private int height;
    private String prevHash;
    private List<Transaction> transactions = new ArrayList<>();
    private Instant timestamp = Instant.now();
    private int nonce = 0;

    /* Constructor for loaded blocks. 
     * Used for reconstructing a block found in an SQL query */
    public Block(String hash, int height, String prevHash, List<Transaction> transactions, Instant timestamp, int nonce) {
        this.hash = hash;
        this.height = height;
        this.prevHash = prevHash;
        this.transactions = transactions;
        this.timestamp = timestamp;
        this.nonce = nonce;
    }

    /* Constructor how creating a new block.
     * Attaches the previous hash and associated transaction */
    public Block(int height, String prevHash, Transaction transaction) {
        this.height = height;
        this.prevHash = prevHash;
        transactions.add(transaction);
        this.hash = calculateHash();
    }

    /* Creates a hash based on the variables in the object. */
    public String calculateHash() {
        String input = prevHash + timestamp.toString() + transactions.toString() + nonce;
        return Hash.hash(input, "SHA256"); 
    }

    public int getHeight() {
        return height;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void mine(int difficulty) throws NoSuchAlgorithmException {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }
}
