package com.tuituicoin.cli.node;

import picocli.CommandLine.Command;

@Command(name = "init")

public class NodeInitialize implements Runnable {
    @Override
    public void run() {
        try {
            com.tuituicoin.repository.DatabaseManager.initialize();
            com.tuituicoin.blockchain.Genesis.initialize();
            System.out.println("Node initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize node: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
