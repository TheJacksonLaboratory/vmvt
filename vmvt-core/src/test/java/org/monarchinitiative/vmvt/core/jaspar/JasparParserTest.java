package org.monarchinitiative.vmvt.core.jaspar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JasparParserTest {

    static String abs;
    @BeforeAll
    static void init() throws IOException  {
        Path path = Paths.get("src","test", "resources","JASPAR2020_CORE_vertebrates_non-redundant_pfms_jaspar_HEAD.txt");
        File f = path.toFile();
        if (! f.exists()) {
            throw new FileNotFoundException("Could not find test JASPAR file");
        }
        abs = f.getAbsolutePath();


    }

    @Test
    void testCtor() {
        JasparParser parser = new JasparParser(abs);
        assertNotNull(abs);
    }


}
