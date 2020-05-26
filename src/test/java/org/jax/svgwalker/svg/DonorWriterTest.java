package org.jax.svgwalker.svg;

import org.jax.svgwalker.WalkerWriter;
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
        WalkerWriter donor = new WalkerWriter();
        assertNotNull(donor);
    }

    @Test
    void testWriteSvg() {
        WalkerWriter donor = new WalkerWriter();
        String svg = donor.getDonorWalkerSvg(ref,alt);
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

    @Test
    void testWriteSvgLogo() {
        WalkerWriter donor = new WalkerWriter();
        String svg = donor.getDonorLogoSvg(ref,alt);
        assertNotNull(svg);
        System.out.println(svg);
        try {
            String path = "/home/peter/data/testLOGO.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
