package com.tuituicoin.cli.chain;

import com.tuituicoin.blockchain.Block;
import com.tuituicoin.repository.SQLiteBlockRepository;

import picocli.CommandLine.Command;

@Command(name = "latest")

public class ChainLatestCommand implements Runnable {
    @Override
    public void run() {
    }
}
