package com.tuituicoin.cli.mine;

import picocli.CommandLine.Command;

@Command(
    name = "mine",
    description = "Commands related to mining new blocks"
)

public class MineCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Mine commands");
    }
}
