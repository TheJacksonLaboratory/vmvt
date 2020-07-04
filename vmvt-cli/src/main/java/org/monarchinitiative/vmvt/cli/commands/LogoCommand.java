package org.monarchinitiative.vmvt.cli.commands;

import org.monarchinitiative.vmvt.core.VmvtGenerator;
import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Print an SVG with the donor or acceptor logo.
 * Note that this only shows the wildtype sequence and so the user
 * does not need to pass sequences
 */
@CommandLine.Command(name = "logo", aliases = {"L"}, mixinStandardHelpOptions = true, description = "Create sequence logo")
public class LogoCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-d","--donor"})
    boolean isDonor;
    @CommandLine.Option(names = {"-a","--acceptor"})
    boolean isAcceptor;
    @CommandLine.Option(names = {"-o","--out"})
    String outname = "logo.svg";

    @Override
    public Integer call() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg;
        if (isAcceptor && isDonor) {
            System.err.println("[ERROR] Enter only one of -d/--donor or -a/--acceptor!");
        }
        if (isDonor) {
            svg = vmvt.getDonorLogoSvg();
        } else {
            svg = vmvt.getAcceptorLogoSvg();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outname))) {
            writer.write(svg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
