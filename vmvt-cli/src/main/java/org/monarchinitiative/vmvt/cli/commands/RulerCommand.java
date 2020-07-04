package org.monarchinitiative.vmvt.cli.commands;

import org.monarchinitiative.vmvt.core.VmvtGenerator;
import picocli.CommandLine;

import java.io.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Print an SVG with the donor or acceptor ruler.
 * @author Peter N Robinson
 */
@CommandLine.Command(name = "ruler", aliases = {"R"}, mixinStandardHelpOptions = true, description = "Create sequence ruler")
public class RulerCommand extends AbstractSequenceComparisonCommand implements Callable<Integer> {


    @Override
    public Integer call() throws Exception {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg="";
        initSequences();
        if (seqlen == 9) {
            // donor
            svg= vmvt.getDonorSequenceRuler(this.reference, this.alternate);
        } else {
            svg = vmvt.getAcceptorSequenceRuler(this.reference, this.alternate);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outname))) {
            writer.write(svg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
