package com.tuituicoin;
import com.tuituicoin.cli.NodeCommand;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new NodeCommand()).execute(args);
        System.exit(exitCode);
    }
}
