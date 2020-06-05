package org.monarchinitiative.vmvt.dist;


import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.delta.DeltaSvg;

public class TestDistributionCalculator {

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
        DistributionCalculator dcal = new DistributionCalculator(DoubleMatrix.donor());
        DeltaSvg dsvg = new DeltaSvg(dcal.getDeltas());
        dsvg.dump();
    }
}
