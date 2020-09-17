package org.monarchinitiative.vmvt.cli;

/**
 * Main entry point to the command-line interface of vmvt.
 */

import org.monarchinitiative.vmvt.cli.commands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "vmvt", mixinStandardHelpOptions = true, version = "vmvt 0.8.0",
        description = "Variant-motif visualization tool.")
public class Main implements Callable<Integer> {

    public static void main(String[] args) {
        CommandLine cline = new CommandLine(new Main()).
                addSubcommand("logo", new LogoCommand()).
                addSubcommand("ruler", new RulerCommand()).
                addSubcommand("walker", new WalkerCommand()).
                addSubcommand("ese", new EseCommand()).
                addSubcommand("delta", new DeltaCommand());
        cline.setToggleBooleanFlags(false);
        int exitCode = cline.execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        // work done in subcommands
        return 0;
    }
}