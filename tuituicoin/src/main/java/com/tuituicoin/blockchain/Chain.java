package com.tuituicoin.blockchain;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.tuituicoin.repository.DatabaseManager;
import com.tuituicoin.repository.SQLiteBlockRepository;

public class Chain {
    private static Chain instance;
    private Wallet genesisWallet;
    private SQLiteBlockRepository blockRepository;

    private static final Logger LOGGER = Logger.getLogger(Chain.class.getName());

    private Chain() throws NoSuchAlgorithmException {
        genesisWallet = new Wallet();
        blockRepository = new SQLiteBlockRepository();
        try {
            DatabaseManager.initialize();
        } catch (SQLException e) {
            LOGGER.severe("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static synchronized Chain getInstance() {
        if (instance == null) {
            LOGGER.info("No existing chain instance found. Creating new instance.");
            try {
                instance = new Chain();
                LOGGER.info("Chain instance created successfully.");
            } catch (NoSuchAlgorithmException e) {
                LOGGER.severe("Failed to initialize chain: " + e.getMessage());
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
            LOGGER.warning("Invalid transaction signature for transaction: " + transaction);
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
            LOGGER.warning("Blockchain is empty. No blocks to validate.");
            return true;
        }

        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);

            // Verify hash
            String recalculatedHash = current.calculateHash();

            LOGGER.info("Validating block at height: " + current.getHeight());

            if (!current.getHash().equals(recalculatedHash)) {
                LOGGER.warning("Invalid hash at block height: " + current.getHeight());
                return false;
            }

            // Skip check for genesis block 
            if (i == 0) {
                LOGGER.info("Genesis block found. Skipping linkage and height checks for genesis block.");
                continue;
            }

            Block previous = chain.get(i - 1);

            // Verify linkage
            if (!current.getPrevHash().equals(previous.getHash())) {
                LOGGER.warning("Broken chain linkage at block height: " + current.getHeight());
                return false;
            }

            // Verify height sequence
            if (current.getHeight() != previous.getHeight() + 1) {
                LOGGER.warning("Invalid block height sequence at " + current.getHeight());
                return false;
            }

            // Verify proof-of-work
            if (!current.getHash().startsWith("0000")) {
                LOGGER.warning("Invalid proof-of-work at block height: " + current.getHeight());
                return false;
            }

            LOGGER.info("Block at height " + current.getHeight() + " is valid.");
        }

        LOGGER.info("Blockchain is valid with " + chain.size() + " blocks.");
        return true;
    }
}
