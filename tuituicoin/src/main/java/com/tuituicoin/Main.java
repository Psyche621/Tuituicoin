package com.tuituicoin;
import java.util.logging.Logger;

import com.tuituicoin.cli.NodeCommand;

import picocli.CommandLine;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting Tuituicoin node.");
        int exitCode = new CommandLine(new NodeCommand()).execute(args);
        System.exit(exitCode);
    }
}
