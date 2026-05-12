package com.tuituicoin.cli.wallet;

import picocli.CommandLine.Command;

@Command(
    name = "wallet",
    subcommands = {
        WalletCreateCommand.class
    }
)

public class WalletCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Wallet commands");
    }
}
