package com.tuituicoin.cli.chain;

import com.tuituicoin.blockchain.Block;
import com.tuituicoin.repository.SQLiteBlockRepository;

import picocli.CommandLine.Command;

@Command(name = "latest")

public class ChainLatestCommand implements Runnable {
    @Override
    public void run() {
        SQLiteBlockRepository blockRepository = new SQLiteBlockRepository();
        Block latestBlock = blockRepository.findLatest();

        if (latestBlock == null) {
            System.out.println("No blocks found in the chain.");
        } else {
            System.out.println("Latest Block:");
            System.out.println("Hash: " + latestBlock.getHash());
            System.out.println("Height: " + latestBlock.getHeight());
            System.out.println("Previous Hash: " + latestBlock.getPrevHash());
            System.out.println("Timestamp: " + latestBlock.getTimestamp());
            System.out.println("Nonce: " + latestBlock.getNonce());
            System.out.println("Transactions:");
            latestBlock.getTransactions().forEach(tx -> {
                System.out.println("- " + tx);
            });
        }
    }
}
