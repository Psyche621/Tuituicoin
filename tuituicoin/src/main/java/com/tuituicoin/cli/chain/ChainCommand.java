package com.tuituicoin.cli.chain;

import picocli.CommandLine.Command;

@Command(
    name = "chain",
    subcommands = {
        ChainLatestCommand.class
    }
)

public class ChainCommand {

}
