package com.tuituicoin.blockchain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class WalletManager {
    private static WalletManager instance;
    private static final Path WALLET_DIR = Paths.get("wallets");

    private static final Logger LOGGER = Logger.getLogger(WalletManager.class.getName());

    static {
        try {
            Files.createDirectories(WALLET_DIR);
        } catch (IOException e) {
            LOGGER.severe("Failed to create wallets directory: " + e.getMessage());
            throw new RuntimeException("Unable to initialize WalletManager", e);
        }
    }

    private WalletManager() {}

    public static synchronized WalletManager getInstance() {
        if (instance == null) {
            LOGGER.info("No existing WalletManager instance found. Creating new instance.");
            instance = new WalletManager();
        }

        return instance;
    }

    /* If wallet name is set to default (wallet.dat) generate new name to avoid overriding (such as wallet-1.dat) */
    public static String generateWalletName() {
        LOGGER.info("Generating wallet name...");

        String baseName = "wallet";
        String extension = ".dat";
        int counter = 1;

        // Create wallet names until we find one that doesn't exist
        while (true) {
            String walletName = baseName + (counter == 1 ? "" : "-" + counter) + extension;

            if (!exists(walletName)) {
                LOGGER.info("Generated wallet name: " + walletName);
                return walletName;
            }

            counter++;
        }
    }

    public static Path getWalletPath(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }

        return WALLET_DIR.resolve(fileName);
    }

    public static boolean exists(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        Path path = WALLET_DIR.resolve(fileName);
        return path.toFile().exists();
    }
}
