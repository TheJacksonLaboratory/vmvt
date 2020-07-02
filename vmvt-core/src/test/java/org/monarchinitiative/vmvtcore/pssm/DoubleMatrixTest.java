package org.monarchinitiative.vmvtcore.pssm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DoubleMatrixTest {

    private final static double EPSILON = 0.000001;

    @Test
    public void if_ctor_not_null_then_ok() {
        DoubleMatrix dm = new DoubleMatrix(2, 3);
        assertNotNull(dm);
    }

    @Test
    public void if_ctor_initializes_to_zero_then_ok() {
        DoubleMatrix dm = new DoubleMatrix(2, 3);
        assertEquals(0d, dm.get(1,1), EPSILON);
    }

    /**
     * The first base in the intron is almost always G
     * Its probability should be very close to 1.0
     */
    @Test
    void if_pos_1_is_G_then_ok() {
        DoubleMatrix donor  = DoubleMatrix.mapToDoubleMatrix(SpliceDonorMatrix.get());
        double g_prob = donor.get(2,3); // G at position 4 (first intronic pos)
        assertEquals(1.0, g_prob, 0.01);
    }

    /**
     * The first base in the intron is almost always G
     * Its IC should should be very close to 2.0
     */
    @Test
    void if_IC_pos_1_is_G_then_ok() {
        DoubleMatrix donor  = DoubleMatrix.mapToDoubleMatrix(SpliceDonorMatrix.get());
        DoubleMatrix icDonor = DoubleMatrix.createICMatrix(donor);
        double g_ic = icDonor.get(2,3); // G at position 4 (first intronic pos)
        assertEquals(2.0, g_ic, 0.01);
    }
}
