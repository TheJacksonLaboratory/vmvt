package org.jax.svgwalker.svg;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
        try {
            String path = "/home/peter/data/test.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
