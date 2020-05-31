package org.jax.svgwalker.svg;

import org.jax.svgwalker.WalkerWriter;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AcceptorWriterTest {
    private final String ref = "cctggctggcggcaccgggtgccagGT";
    /** chr10-90768644-A-G, -2 position */
    private final String alt = "cctggctggcggcaccgggtgccggGT";


    @Test
    void testWriteSvgAcceptorWalker() {
        WalkerWriter donor = new WalkerWriter();
        String svg = donor.getAcceptorWalkerSvg(ref,alt);
        assertNotNull(svg);
        System.out.println(svg);
        try {
            String path = "acceptorWalker.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testWriteSvgAcceptorLogo() {
        WalkerWriter donor = new WalkerWriter();
        String svg = donor.getAcceptorLogoSvg(ref,alt);
        assertNotNull(svg);
        System.out.println(svg);
        try {
            String path = "acceptorLogo.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
