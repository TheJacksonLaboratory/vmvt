package org.jax.svgwalker.svg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DonorWriterTest {
    private final String ref = "AAGGTCAGA";
    private final String alt = "AAGATCAGA";


    @Test
    void testCtor() {
        SvgWriter donor = new DonorWriter(ref,alt);
        assertNotNull(donor);
    }

    @Test
    void testWriteSvg() {
        SvgWriter donor = new DonorWriter(ref,alt);
        String svg = donor.getSvg();
        assertNotNull(svg);
        System.out.println(svg);
    }
}
