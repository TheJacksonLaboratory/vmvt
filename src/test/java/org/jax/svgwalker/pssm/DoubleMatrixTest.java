package org.jax.svgwalker.pssm;

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
}
