package com.tuituicoin.cli.mine;

import picocli.CommandLine.Command;

@Command(
    name = "mine",
    subcommands = {}
)

public class MineCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Mine commands");
    }
}
