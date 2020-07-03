package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.VmvtGenerator;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DonorGeneratorTest {
    private final String ref = "AAGGTCAGA";
    private final String alt = "AAGATCAGA";


    @Test
    void testCtor() {
        VmvtGenerator donor = new VmvtGenerator();
        assertNotNull(donor);
    }

    @Test
    void testWriteDonorSvgWalker() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorWalkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorWalker.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriteDonorSvgLogo() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorLogoSvg();
        assertNotNull(svg);
        try {
            String path = "target/donorLogo.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriteDonorSvgCombo() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorVmvtSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorVmvt.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetDonorRuler(){
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorSequenceRuler(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorRuler.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
