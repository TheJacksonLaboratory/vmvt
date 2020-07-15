package org.monarchinitiative.vmvt.core.hexamer;



import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Daniel Danis
 */
public class FileKMerParser {


    /**
     * Default location for this file is src/main/resources/org/monarchinitiative/vmvt/hexamer/hexamer-scores.tsv.
     */
    private static final Map<String, Double> HEXAMER_MAP = parseKmerTsvFile("hexamer-scores.tsv");

    /**
     * Default location for this file is src/main/resources/org/monarchinitiative/vmvt/hexamer/hexamer-scores.tsv.
     */
    private static final Map<String, Double> HEPTAMER_MAP = parseKmerTsvFile("heptamer-scores.tsv");

    private FileKMerParser() {
    }

    /**
     * @return map with hexamer scores or an empty map if an error occurred when parsing the file
     */
    public static Map<String, Double> hexamerMap() {
        return HEXAMER_MAP;
    }

    /**
     * @return map with heptamer scores or an empty map if an error occurred when parsing the file
     */
    public static Map<String, Double> heptamerMap() {
        return HEPTAMER_MAP;
    }


    /**
     * Read the TSV file with k-mer scores from TSV file bundled within the vmvt JAR.
     *
     * @param resourcePath path to TSV file with k-mer scores
     * @return map with k-mer -> score, or an empty map if an error occurs during reading the file
     */
    private static Map<String, Double> parseKmerTsvFile(String resourcePath) {
        Map<String, Double> kmerMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileKMerParser.class.getResourceAsStream(resourcePath)))) {
            reader.lines()
                    .filter(emptyLines()).filter(comments())
                    .forEach(line -> {
                        final String[] tokens = line.split("\t");
                        if (tokens.length != 2) {
                            System.err.printf("Invalid line %s in Kmer TSV.\n", line);
                            return;
                        }
                        final String seq = tokens[0].toUpperCase();

                        final double score;
                        try {
                            score = Double.parseDouble(tokens[1]);
                        } catch (NumberFormatException e) {
                            System.err.printf("Invalid score %s in line %s", tokens[1], line);
                            return;
                        }
                        kmerMap.put(seq, score);
                    });

        } catch (IOException e) {
            throw new VmvtRuntimeException("Error parsing k-mer file:" +e.getMessage());
        }
        return kmerMap;
    }

    private static Predicate<? super String> comments() {
        return line -> !line.startsWith("#");
    }

    private static Predicate<? super String> emptyLines() {
        return line -> !line.isEmpty();
    }
}
