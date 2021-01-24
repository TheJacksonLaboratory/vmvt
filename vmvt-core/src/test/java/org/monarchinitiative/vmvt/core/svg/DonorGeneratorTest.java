package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.VmvtGenerator;
import org.junit.jupiter.api.Test;

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
    public void testCtor() {
        VmvtGenerator donor = new VmvtGenerator();
        assertNotNull(donor);
    }

    @Test
    public void testWriteDonorSvgWalker() {
        VmvtGenerator donor = new VmvtGenerator(true);
        String svg = donor.getRefAltDonorWalkerSvg(ref,alt);
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
    public void testWriteCHRNESvgWalker() {
        VmvtGenerator donor = new VmvtGenerator(true);
        String col1a2Ref = "CAGGTGAAG";
        String cik1a2Alt = "CATGTGAAG";
        String svgRef = donor.getDonorWalkerSvg(col1a2Ref);
        String svgAlt = donor.getDonorWalkerSvg(cik1a2Alt);
        String svgBars = donor.getDonorIcBarsWithRi(col1a2Ref, cik1a2Alt);
        assertNotNull(svgRef);
        assertNotNull(svgAlt);
        assertNotNull(svgBars);
        try {
            String path = "target/donorCHRNERefWalker.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svgRef);
            writer.close();
            path = "target/donorCHRNEAltWalker.svg";
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(svgAlt);
            writer.close();
            path = "target/donorCHRNEbars.svg";
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(svgBars);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testWriteDonorSvgLogo() {
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
    public void testWriteDonorTrekker() {
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
    public void testWriteDonorTrekkerBack() {
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorTrekkerSvg(alt,ref);
        assertNotNull(svg);
        try {
            String path = "target/donorTrekkerBack.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testWriteDonorTrekkerMinusOnePosition() {
        final String ref = "AAGGTCAGA";
        final String alt = "AACGTCAGA";
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorTrekkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorTrekkerMinus1.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWriteDonorTrekkerMinusTwoPosition() {
        final String ref = "AAGGTCAGA";
        final String alt = "ATGGTCAGA";
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorTrekkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorTrekkerMinus2.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteDonorTrekker2NtVar() {
        final String ref = "AAGGTCAGA";
        final String alt = "ATCGTCAGA";
        VmvtGenerator donor = new VmvtGenerator();
        String svg = donor.getDonorTrekkerSvg(ref,alt);
        assertNotNull(svg);
        try {
            String path = "target/donorTrekker2nt.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDonorRuler(){
        VmvtGenerator donor = new VmvtGenerator(true);
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

    @Test
    public void testGetDonorRulerWithOffset(){
        VmvtGenerator donor = new VmvtGenerator(true);
        String svg = donor.getDonorSequenceRulerAndBarChartWithOffset(ref,alt, 5);
        assertNotNull(svg);
        try {
            String path = "target/donorRulerOffset.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHexamer(){
        String svg = getHexamerSvg();
        assertNotNull(svg);
        try {
            String path = "target/hexamer.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHeptamer(){
        String svg = getHeptamerSvg();
        assertNotNull(svg);
        try {
            String path = "target/heptamer.svg";
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
    public void writeAllSvgsToReadTheDocs() {
        if (42==2) {
            assertEquals(2,2);
            return;
        }
        final String acceptorRef = "cctggctggcggcaccgggtgccagGT";
        /* chr10-90768644-A-G, -2 position */
        final String acceptorAlt = "cctggctggcggcaccgggtgccggGT";

        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDonorSequenceRuler(ref,alt);
        writeToRTDdirectory("donorRuler.svg" , svg);
        svg = vmvt.getDonorTrekkerSvg(ref,alt);
        writeToRTDdirectory("donorTrekker.svg" , svg);
        svg = vmvt.getDonorLogoSvg();
        writeToRTDdirectory("donorLogo.svg" , svg);
        svg = vmvt.getRefAltDonorWalkerSvg(ref,alt);
        writeToRTDdirectory("donorWalker.svg" , svg);
        svg = vmvt.getRefAltAcceptorWalkerSvg(acceptorRef, acceptorAlt);
        writeToRTDdirectory("acceptorWalker.svg" , svg);
        svg = getHexamerSvg();
        writeToRTDdirectory("hexamer.svg", svg);
        svg = getHeptamerSvg();
        writeToRTDdirectory("heptamer.svg", svg);
        svg = vmvt.getDelta(ref, alt);
        writeToRTDdirectory("deltaDonor.svg", svg);
        final String ref = "CAGGTTGGT";
        final String alt = "TAGGTTGGT";
        vmvt = new VmvtGenerator();
        svg = vmvt.getDonorTrekkerWithRi(ref, alt);
        writeToRTDdirectory("donorWithRi.svg", svg);
        final String ref2 = "CTGGCAGGT";
        final String alt2 = "CTGGTAGGT";
        vmvt = new VmvtGenerator();
        svg = vmvt.getDonorTrekkerWithRi(ref2, alt2);
        writeToRTDdirectory("donorWithRiCryptic.svg", svg);
    }

    @Test
    public void testDeltaDonor() {
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
    }

    @Test
    public void testDonorWithRi() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDonorTrekkerWithRi(ref, alt);

        try {
            String path = "target/donorWithRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void largeDeltaTest() {
        VmvtGenerator vmvt = new VmvtGenerator();
        final String svg = vmvt.getDonorDistributionSvg("TTAgtaagt", "TTAtaagtg");
        try {
            String path = "target/donorWith2Nucleotides.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
