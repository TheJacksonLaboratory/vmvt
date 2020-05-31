package org.jax.vmvt.svg;

import org.jax.vmvt.VmvtWriter;
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
        VmvtWriter donor = new VmvtWriter();
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
        VmvtWriter donor = new VmvtWriter();
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

    @Test
    void testWriteSvgAcceptorVmvt() {
        VmvtWriter donor = new VmvtWriter();
        String svg = donor.getAcceptorVmvtSvg(ref,alt);
        assertNotNull(svg);
        System.out.println(svg);
        try {
            String path = "acceptorVmvt.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
