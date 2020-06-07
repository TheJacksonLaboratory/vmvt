package org.monarchinitiative.vmvt.dist;


import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.VmvtGenerator;
import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.delta.DeltaSvg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDistributionCalculator {

    private final String ref = "AAGGTCAGA";
    private final String alt = "AAGATCAGA";

    @Test
    void testDonorDist() {
        DistributionCalculator dcal = new DistributionCalculator(DoubleMatrix.donor());
        String donorOutfile = "donorvals.txt";
        String donorDeltasOutfile = "donordeltas.txt";
        dcal.writeVals(donorOutfile,donorDeltasOutfile);
    }

    @Test
    void testAcceptorDist() {
        DistributionCalculator dcal = new DistributionCalculator(DoubleMatrix.acceptor());
        String acceptorOutfile = "acceptorvals.txt";
        String acceptorDeltasOutfile = "acceptordeltas.txt";
        dcal.writeVals(acceptorOutfile, acceptorDeltasOutfile);
    }


    @Test
    void testDump() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDonorDistributionSvg(ref, alt);
        assertNotNull(svg);
        try {
            String path = "target/donorHistogram.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
