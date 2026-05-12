package com.tuituicoin.cli.chain;

import com.tuituicoin.blockchain.Block;
import com.tuituicoin.blockchain.Chain;

import picocli.CommandLine.Command;

@Command(name = "latest")

public class ChainLatestCommand implements Runnable {
    @Override
    public void run() {
        try {
            //Block latest = blockRepository.findLatest()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
