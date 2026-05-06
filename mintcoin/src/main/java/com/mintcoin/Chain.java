package com.mintcoin;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Chain {
    private static Chain instance = new Chain();
    private ArrayList<Block> chain;

    public Chain() {
        chain.add(new Block(null, new Transaction(100, "Mike", "Bob")));
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Transaction transaction, String senderPublicKey) throws NoSuchAlgorithmException {
        Block block = new Block(getLastBlock().hash(), transaction);
        chain.add(block);
    }
}
