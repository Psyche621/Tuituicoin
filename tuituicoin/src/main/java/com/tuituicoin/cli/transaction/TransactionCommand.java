package com.tuituicoin.cli.transaction;

import picocli.CommandLine.Command;

@Command(
    name = "tx",
    description = "Commands related to transactions",
    subcommands = {}
)

public class TransactionCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Transaction commands");
    }
}
