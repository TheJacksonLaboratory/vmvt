package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.VmvtGenerator;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.core.svg.delta.DeltaSvg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testWriteDonorTrekker() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorTrekkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorTrekker.svg";
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

    private String getHexamerSvg() {
        String ref = "CCTGGAGGGGA";
        String alt = "CCTGGCGGGGA";
        VmvtGenerator vmvt = new VmvtGenerator();
        return vmvt.getHexamerSvg(ref, alt);
    }

    private String getHeptamerSvg() {
        String ref = "TCCTGGAGGGGAA";
        String alt = "TCCTGGCGGGGAA";
        VmvtGenerator vmvt = new VmvtGenerator();
        return vmvt.getHeptamerSvg(ref, alt);
    }


    private void writeToRTDdirectory(String filename, String svg) {
        String directory = "../docs/img/";
        try {
            File f = Paths.get(directory, filename).toFile();
            System.out.printf("Writing %s\n", f.getAbsolutePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a convenience function that writes all of the donor SVGs to the read the doc's
     * directory. It should not be run except if there is an update to the SVGs that
     * we want to show for the documentation.
     */
    @Test
    void writeAllSvgsToReadTheDocs() throws  IOException {
        if (2==2) {
            assertEquals(2,2);
            return;
        }
        final String acceptorRef = "cctggctggcggcaccgggtgccagGT";
        /** chr10-90768644-A-G, -2 position */
        final String acceptorAlt = "cctggctggcggcaccgggtgccggGT";

        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDonorTrekkerSvg(ref,alt);
        writeToRTDdirectory("donorTrekker.svg" , svg);
        svg = vmvt.getDonorLogoSvg();
        writeToRTDdirectory("donorLogo.svg" , svg);
        svg = vmvt.getDonorWalkerSvg(ref,alt);
        writeToRTDdirectory("donorWalker.svg" , svg);
        svg = vmvt.getAcceptorWalkerSvg(acceptorRef, acceptorAlt);
        writeToRTDdirectory("acceptorWalker.svg" , svg);
        svg = getHexamerSvg();
        writeToRTDdirectory("hexamer.svg", svg);
        svg = getHeptamerSvg();
        writeToRTDdirectory("heptamer.svg", svg);
    }




    @Test
    void testDeltaDonor() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDelta(ref, alt);

        try {
            String path = "target/donorDelta.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (42==41) {
            writeToRTDdirectory("deltaDonor.svg", svg);
        }
    }




}
