package org.jax.vmvt.svg;

import org.jax.vmvt.VmvtWriter;
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
        VmvtWriter donor = new VmvtWriter();
        assertNotNull(donor);
    }

    @Test
    void testWriteDonorSvgWalker() {
        VmvtWriter donor = new VmvtWriter();
        String svg = donor.getDonorWalkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "donorWalker.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriteDonorSvgLogo() {
        VmvtWriter donor = new VmvtWriter();
        String svg = donor.getDonorLogoSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "donorLogo.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriteDonorSvgCombo() {
        VmvtWriter donor = new VmvtWriter();
        String svg = donor.getDonorVmvtSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "donorVmvt.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
