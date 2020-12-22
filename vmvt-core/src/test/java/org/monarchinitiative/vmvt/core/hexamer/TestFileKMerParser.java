package org.monarchinitiative.vmvt.core.hexamer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.core.VmvtGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFileKMerParser {

    private static Map<String, Double> hexamerMap;
    private static Map<String, Double> heptamerMap;

    @BeforeAll
    public static void init() {
        hexamerMap = FileKMerParser.hexamerMap();
        heptamerMap = FileKMerParser.heptamerMap();
    }

    @Test
    public void testConstructor() {
        assertNotNull(hexamerMap);
    }

    @Test
    public void testHexamerSize() {
        int size = hexamerMap.size();
        // we expect 4 to the power of 6 kmers.
        int expected = (int) Math.pow(4, 6);
        assertEquals(expected, size);
    }

    @Test
    public void testHeptamerSize() {
        int size = heptamerMap.size();
        // we expect 4 to the power of 7 kmers.
        int expected = (int) Math.pow(4, 7);
        assertEquals(expected, size);
    }

    // JUST FOR DEVELOPMENT, CAN BE DELETED
    @Disabled
    @Test
    public void writeEseSvg() {
        String ref = "cctggctatat";
        String alt = "cctagctatat";
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getHexamerSvg(ref, alt);
        try {
            String path = "target/ese6.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
