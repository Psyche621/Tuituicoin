package com.tuituicoin.blockchain;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import com.tuituicoin.repository.DatabaseManager;
import com.tuituicoin.repository.SQLiteBlockRepository;

public class Chain {
    private static Chain instance;
    private Wallet genesisWallet;
    private SQLiteBlockRepository blockRepository;

    private Chain() throws NoSuchAlgorithmException {
        genesisWallet = new Wallet();
        blockRepository = new SQLiteBlockRepository();
        try {
            DatabaseManager.initialize();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static synchronized Chain getInstance() {
        if (instance == null) {
            try {
                instance = new Chain();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Unable to initialize chain", e);
            }
        }

        return instance;
    }

    public Block getLastBlock() {
        return blockRepository.findLatest();
    }

    public void addBlock(Transaction transaction) throws Exception {
        if (!transaction.verify()) {
            throw new SecurityException("Invalid transaction signature");
        }

        Block lastBlock = getLastBlock();
        Block block;

        if (lastBlock == null) {
            block = new Block(0, "0", transaction);
        } else {
            block = new Block(lastBlock.getHeight() + 1, lastBlock.getHash(), transaction);
        }

        block.mine(4);

        blockRepository.save(block);
    }

    public boolean isValid() {
        List<Block> chain = blockRepository.findAll();

        if (chain == null || chain.isEmpty()) {
            return true;
        }

        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);

            // Verify hash
            String recalculatedHash = current.calculateHash();

            if (!current.getHash().equals(recalculatedHash)) {
                System.out.println("Invalid hash at block height: " + current.getHeight());
                return false;
            }

            // Skip check for genesis block 
            if (i == 0) {
                continue;
            }

            Block previous = chain.get(i - 1);

            // Verify linkage
            if (!current.getPrevHash().equals(previous.getHash())) {
                System.out.println("Broken chain linkage at block height: " + current.getHeight());
                return false;
            }

            // Verify height sequence
            if (current.getHeight() != previous.getHeight() + 1) {
                System.out.println("Invalid block height sequence at " + current.getHeight());
                return false;
            }

            // Verify proof-of-work
            if (!current.getHash().startsWith("0000")) {
                System.out.println("Invalid proof-of-work at block height: " + current.getHeight());
                return false;
            }
        }

        return true;
    }
}
