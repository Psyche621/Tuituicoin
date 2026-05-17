package com.tuituicoin.cli.wallet;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "load",
    description = "Load an existing wallet"
)

public class WalletLoadCommand implements Runnable {
    @Option(
        names = {"-n", "--name"},
        description = "Name of the wallet to load, load default wallet if not specified"
    )
    private String fileName;

    @Option(
        names = {"-p", "--password"},
        description = "Password to decrypt the wallet file",
        required = true,
        interactive = true,
        arity = "0..1"
    )
    private char[] password;

    @Override
    public void run() {
        System.out.println("This will load your chosen wallet eventually");
    }
}
