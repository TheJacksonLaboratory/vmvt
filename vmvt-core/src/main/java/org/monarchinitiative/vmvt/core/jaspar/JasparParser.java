package org.monarchinitiative.vmvt.core.jaspar;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JasparParser {

    List<JasparMatrix> matrixList;



    JasparParser(String absolutePath) {
        Path path = Paths.get(absolutePath);
        matrixList = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            int N = lines.size();
            int i = 0;
            // Advance to the first ">" line -- this starts a JASPAR matrix and contains the name. Each
            // name line is followed by four nucleotide lines (ACGT).
            while (i < N && ! lines.get(i).startsWith(">")) {
                i++;
            }
            for (; i<N; i+=5) {
                if (N-i<0) {
                    System.err.printf("Malformed JASPAR file at line %d. Skipping rest of file.",i);
                    break;
                }
                String headerline = lines.get(i);
                String Aline      = lines.get(i+1);
                String Cline      = lines.get(i+2);
                String Gline      = lines.get(i+3);
                String Tline      = lines.get(i+4);
                JasparMatrix mat = fromLines(headerline, Aline, Cline, Gline, Tline);
                matrixList.add(mat);
            }
            System.out.printf("[INFO] We extracted %d JASPAR matrices.\n", matrixList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<JasparMatrix> getMatrixList() {
        return matrixList;
    }

    private List<Integer> getCountsLine(String line, String expectedBase) {
        if (! line.startsWith(expectedBase)) {
            throw new VmvtRuntimeException("Malformed "+ expectedBase+ " line: " + line);
        }
        // extract the string within the square brackets
        int i = line.indexOf("[");
        if (i < 0) {
            throw new VmvtRuntimeException("Malformed counts line, no '[': " + line);
        }
        int j = line.indexOf("]");
        if (j < 0) {
            throw new VmvtRuntimeException("Malformed counts line, no ']': " + line);
        }
        String countsLine = line.substring(i+1, j).trim();
        String [] fields = countsLine.split("\\s+");
        List<Integer> counts = Arrays.stream(fields)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return counts;
    }




    JasparMatrix fromLines(String header, String Aline, String Cline, String Gline, String Tline) {
        // Header line, e.g., >MA0634.1	ALX3
        if (! header.startsWith(">")) {
            throw new VmvtRuntimeException("Malformed header line: " + header);
        }

        String [] fields = header.split("\t");
        if (fields.length != 2) {
            String msg = String.format("Malformed header line with %d fields (expected 2): %s", fields.length, header);
            throw new VmvtRuntimeException(msg);
        }
        String matrixID = fields[0];
        String name = fields[1];
        // counts, e.g., A  [  1251    987    794   7877   7877     76    697   7877   2597   2759 ]
        List<Integer> aCounts = getCountsLine(Aline, "A");
        List<Integer> cCounts = getCountsLine(Cline, "C");
        List<Integer> gCounts = getCountsLine(Gline, "G");
        List<Integer> tCounts = getCountsLine(Tline, "T");

        return new JasparMatrix(matrixID, name, aCounts, cCounts, gCounts, tCounts);
    }


}
