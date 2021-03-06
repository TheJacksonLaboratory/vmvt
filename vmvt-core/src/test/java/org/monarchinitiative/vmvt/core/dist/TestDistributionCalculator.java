package org.monarchinitiative.vmvt.core.dist;


import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.VmvtGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDistributionCalculator {

    private final String ref = "AAGGTCAGA";
    private final String alt = "AAGATCAGA";

    @Test
    public void testDonorDist() {
        DistributionCalculator dcal = new DistributionCalculator(DoubleMatrix.donor());
        String donorOutfile = "target/donorvals.txt";
        String donorDeltasOutfile = "target/donordeltas.txt";
        dcal.writeVals(donorOutfile,donorDeltasOutfile);
    }

    @Test
    public void testAcceptorDist() {
        DistributionCalculator dcal = new DistributionCalculator(DoubleMatrix.acceptor());
        String acceptorOutfile = "target/acceptorvals.txt";
        String acceptorDeltasOutfile = "target/acceptordeltas.txt";
        dcal.writeVals(acceptorOutfile, acceptorDeltasOutfile);
    }


    @Test
    public void testDump() {
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
