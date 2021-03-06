package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.VmvtGenerator;
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
    public void testWriteSvgAcceptorWalker() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getRefAltAcceptorWalkerSvg(ref,alt);
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
    public void testWriteSvgAcceptorLogo() {
        VmvtGenerator donor = new VmvtGenerator(true);
        String svg = donor.getAcceptorLogoSvg();
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
    public void testWriteSvgAcceptorTrekker() {
        VmvtGenerator donor = new VmvtGenerator(true);
        String svg = donor.getAcceptorTrekkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/acceptorTrekker.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAcceptorRuler(){
        VmvtGenerator accceptor = new VmvtGenerator();
        String svg = accceptor.getAcceptorSequenceRuler(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/acceptorRuler.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAcceptorRulerOffset(){
        VmvtGenerator accceptor = new VmvtGenerator(true);
        String svg = accceptor.getAcceptorSequenceRulerAndBarChartWithOffset(ref,alt, -7);
        assertNotNull(svg);
        try {
            String path = "target/acceptorRulerOffset.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptorWithRi() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String canRef =     "gtgttttgtcatgtgtaTgctcaagGG";
        String crypticRef = "gtgttttgtcatgtgtaAgctcaagGG";
        String svg = vmvt.getAcceptorTrekkerWithRi(canRef, crypticRef);
        assertNotNull(svg);
        try {
            String path = "target/acceptorWithRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
