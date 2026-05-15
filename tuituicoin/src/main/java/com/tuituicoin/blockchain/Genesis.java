package com.tuituicoin.blockchain;

import java.util.logging.Logger;

public class Genesis {
    private static final Logger LOGGER = Logger.getLogger(Genesis.class.getName());

    public Genesis() {}

    public static void initialize() {
        try {
            LOGGER.info("Creating genesis block.");

            Wallet genesisWalletA = new Wallet();
            Wallet genesisWalletB = new Wallet();

            Block genesisBlock = new Block(0, "0", new Transaction(1000, "0", genesisWalletA.getPublicKey(), genesisWalletB.getPublicKey()));
            genesisWalletA.sendMoney(1000, genesisBlock, genesisWalletB.getPublicKey());

            LOGGER.info("Genesis block created successfully with hash: " + genesisBlock.getHash());
        } catch (Exception e) {
            LOGGER.severe("Failed to create genesis block: " + e.getMessage());
            throw new RuntimeException("Failed to create genesis block", e);
        }
    }
}
