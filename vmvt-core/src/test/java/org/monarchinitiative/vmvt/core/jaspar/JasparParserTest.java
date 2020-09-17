package org.monarchinitiative.vmvt.core.jaspar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JasparParserTest {
    private static List<JasparMatrix> matrixList;

    @BeforeAll
    static void init() throws IOException  {
        Path path = Paths.get("src","test", "resources","JASPAR2020_CORE_vertebrates_non-redundant_pfms_jaspar_HEAD.txt");
        File f = path.toFile();
        if (! f.exists()) {
            throw new FileNotFoundException("Could not find test JASPAR file");
        }
        String abs = f.getAbsolutePath();
        JasparParser parser = new JasparParser(abs);
        matrixList = parser.getMatrixList();
    }

    @Test
    void testCtor() {
        int expectedMatrixCount = 7;
        assertEquals(expectedMatrixCount, matrixList.size());
    }


}
