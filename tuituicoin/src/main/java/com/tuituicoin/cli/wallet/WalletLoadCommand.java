package com.tuituicoin.cli.wallet;

import java.util.Base64;

import com.tuituicoin.blockchain.Wallet;

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
        try {
            Wallet wallet = Wallet.load(fileName, new String(password));
            // Clear password from memory            
            java.util.Arrays.fill(password, '\0');
            System.out.println("Wallet:");
            System.out.println(" Name: " + fileName);
            // System.out.println( "Address: " + wallet.getAddress());
            System.out.println( "Public Key: " + Base64.getEncoder().encodeToString(wallet.getPublicKey().getEncoded()));
        } catch (Exception e) {
            System.err.println("Error loading wallet: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
