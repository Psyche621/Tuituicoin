package com.tuituicoin.cli.wallet;

import com.tuituicoin.blockchain.Wallet;

import picocli.CommandLine.Command;

@Command(name = "create")

public class WalletCreateCommand implements Runnable {
    @Override
    public void run() {
        try {
            Wallet wallet = new Wallet();
            System.out.println("Wallet created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
