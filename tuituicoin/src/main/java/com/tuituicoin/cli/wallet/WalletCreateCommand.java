package com.tuituicoin.cli.wallet;

import java.util.Arrays;
import java.util.concurrent.Callable;

import com.tuituicoin.blockchain.Wallet;
import com.tuituicoin.blockchain.WalletManager;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "create",
    description = "Create a new wallet and save it to a file"
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
    private String fileName;

    @Option(
        names = {"-o", "--overwrite"},
        description = "Overwrite existing wallet file if it exists",
        required = false,
        defaultValue = "false"
    )
    private boolean overwrite;

    @Override
    public Integer call() {
        try {
            Wallet wallet = new Wallet();

            if (fileName == null || fileName.isEmpty()) {
                fileName = WalletManager.generateWalletName();
            }

            wallet.save(fileName, new String(password), overwrite);
            System.out.println("Wallet created and saved to: " + (fileName != null ? fileName : "wallet.dat"));
            Arrays.fill(password, '\0'); // Clear password from memory
            return 0;
        } catch (Exception e) {
            System.err.println("Error creating wallet: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
