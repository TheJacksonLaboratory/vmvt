package org.monarchinitiative.vmvt.core.svg;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.core.jaspar.JasparMatrix;
import org.monarchinitiative.vmvt.core.jaspar.JasparParser;
import org.monarchinitiative.vmvt.core.svg.logo.JasparLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JasparGeneratorTest {

    private static List<JasparMatrix> matrixList;

    @BeforeAll
    public static void init() throws IOException {
        Path path = Paths.get("src","test", "resources","JASPAR2020_CORE_vertebrates_non-redundant_pfms_jaspar_HEAD.txt");
        File f = path.toFile();
        if (! f.exists()) {
            throw new FileNotFoundException("Could not find test JASPAR file");
        }
        String abs = f.getAbsolutePath();
        JasparParser parser = new JasparParser(abs);
        matrixList = parser.getMatrixList();
    }

    @Test @Disabled
    public void testALX3() {
        JasparMatrix alx3 = matrixList.stream()
                .filter(jm -> jm.getJasparName().equals("ALX3"))
                .findAny().orElseThrow();
        SvgSequenceLogo generator = JasparLogoGenerator.fromDoubleMatrix(alx3.getFrequencyMatrix(), false);
        String svg = generator.getSvg();
        assertNotNull(svg);
        try {
            String path = "target/alx3Logo.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
