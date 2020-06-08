package org.monarchinitiative.vmvt.hexamer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class TestFileKMerParser {

    private static Map<String, Double> hexamerMap;
    private static Map<String, Double> heptamerMap;

    @BeforeAll
    static void init() {
        hexamerMap = FileKMerParser.hexamerMap();
        heptamerMap = FileKMerParser.heptamerMap();
    }

    @Test
    void testConstructor() {
        assertNotNull(hexamerMap);
    }

    @Test
    void testHexamerSize() {
        int size = hexamerMap.size();
        // we expect 4 to the power of 6 kmers.
        int expected = (int)Math.pow(4,6);
        assertEquals(expected,size);
    }

    @Test
    void testHeptamerSize() {
        int size = heptamerMap.size();
        // we expect 4 to the power of 7 kmers.
        int expected = (int)Math.pow(4,7);
        assertEquals(expected,size);
    }

    // JUST FOR DEVELOPMENT, CAN BE DELETED
    @Test
    void writeEseSvg() {
        String ref = "cctggctatat";
        String alt = "cctagctatat";

    }
}
