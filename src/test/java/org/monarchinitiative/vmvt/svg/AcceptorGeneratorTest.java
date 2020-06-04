package org.monarchinitiative.vmvt.svg;

import org.monarchinitiative.vmvt.VmvtGenerator;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AcceptorGeneratorTest {
    private final String ref = "cctggctggcggcaccgggtgccagGT";
    /** chr10-90768644-A-G, -2 position */
    private final String alt = "cctggctggcggcaccgggtgccggGT";


    @Test
    void testWriteSvgAcceptorWalker() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getAcceptorWalkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/acceptorWalker.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testWriteSvgAcceptorLogo() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getAcceptorLogoSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/acceptorLogo.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testWriteSvgAcceptorVmvt() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getAcceptorVmvtSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/acceptorVmvt.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
