package com.tuituicoin.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/* Configuration for logging to file */
public class LoggingConfig {
    public static void configure() throws IOException {
        LogManager.getLogManager().reset();

        Logger rootLogger = Logger.getLogger("");

        FileHandler fileHandler = new FileHandler("tuiranode.log", true);
        fileHandler.setFormatter(new SimpleFormatter()); // Format subject to change in the future

        rootLogger.addHandler(fileHandler);
        rootLogger.setLevel(Level.INFO);
    }
}
