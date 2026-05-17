package com.tuituicoin.cli.wallet;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.tuituicoin.blockchain.Wallet;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "create",
    description = "Create a new wallet"
)

public class WalletCreateCommand implements Callable<Integer> {
    @Option(
        names = {"-p", "--password"},
        description = "Password to encrypt the wallet file",
        required = true,
        interactive = true,
        arity = "0..1"
    )
    private char[] password;

    @Option(
        names = {"-f", "--file"},
        description = "Filename to save the wallet (default: wallet.dat)",
        required = false
    )
    private String filename;

    @Override
    public Integer call() {
        try {
            Wallet wallet = new Wallet();
            wallet.save(filename, new String(password));
            System.out.println("Wallet created and saved to: " + (filename != null ? filename : "wallet.dat"));
            Arrays.fill(password, '\0'); // Clear password from memory
            return 0;
        } catch (Exception e) {
            System.err.println("Error creating wallet: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
