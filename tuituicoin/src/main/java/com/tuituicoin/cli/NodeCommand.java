package com.tuituicoin.cli;

import com.tuituicoin.cli.chain.ChainCommand;
import com.tuituicoin.cli.mine.MineCommand;
import com.tuituicoin.cli.transaction.TransactionCommand;
import com.tuituicoin.cli.wallet.WalletCommand;

import picocli.CommandLine.Command;

@Command(
    name = "tuiranode",
    mixinStandardHelpOptions = true,
    version = "1.0",
    subcommands = {
        WalletCommand.class,
        ChainCommand.class,
        MineCommand.class,
        TransactionCommand.class
    }
)

public class NodeCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Node commands");
    }
}
