package org.monarchinitiative.vmvt.cli;

/**
 * Main entry point to the command-line interface of vmvt.
 */

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "vmvt", mixinStandardHelpOptions = true, version = "vmvt 0.8.0",
        description = "Variant-motif visualization tool.")
public class Main implements Callable<Integer> {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        System.out.printf("HI");
        return 0;
    }
}