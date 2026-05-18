package com.tuituicoin.cli.wallet;

import picocli.CommandLine.Command;

@Command(
    name = "wallet",
    description = "Commands related to wallet management",
    subcommands = {
        WalletCreateCommand.class,
        WalletLoadCommand.class
    }
)

public class WalletCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Wallet commands");
    }
}
