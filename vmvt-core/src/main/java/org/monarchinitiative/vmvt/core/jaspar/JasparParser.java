package org.monarchinitiative.vmvt.core.jaspar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JasparParser {





    JasparParser(String absolutePath) {
        Path path = Paths.get(absolutePath);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            int N = lines.size();
            int i = 0;
            // Advance to the first ">" line -- this starts a JASPAR matrix and contains the name. Each
            // name line is followed by four nucleotide lines (ACGT).
            while (i < N && ! lines.get(i).startsWith(">")) {
                i++;
            }
            for (; i+4<N; i+=5) {
                System.out.println("TOP");
                for (int j=0;j<5;j++) {
                    System.out.println(lines.get(i+j));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
