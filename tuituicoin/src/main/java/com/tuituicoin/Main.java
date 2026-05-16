package com.tuituicoin;

import java.io.IOException;

import com.tuituicoin.cli.chain.ChainCommand;
import com.tuituicoin.cli.mine.MineCommand;
import com.tuituicoin.cli.node.NodeInitialize;
import com.tuituicoin.cli.transaction.TransactionCommand;
import com.tuituicoin.cli.wallet.WalletCommand;
import com.tuituicoin.config.LoggingConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "tuiranode",
    mixinStandardHelpOptions = true,
    version = "1.0",
    subcommands = {
        WalletCommand.class,
        ChainCommand.class,
        MineCommand.class,
        TransactionCommand.class,
        NodeInitialize.class
    }
)

public class Main implements Runnable {
    @Override
    public void run() {
        System.out.println("Node commands");
    }

    public static void main(String[] args) throws IOException {
        LoggingConfig.configure();
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
