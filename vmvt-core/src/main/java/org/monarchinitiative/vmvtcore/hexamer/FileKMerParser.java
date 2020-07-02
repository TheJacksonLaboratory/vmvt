package org.monarchinitiative.vmvtcore.hexamer;

import org.monarchinitiative.vmvtcore.except.VmvtRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Daniel Danis
 */
public class FileKMerParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileKMerParser.class);
    /** Default location for this file is src/main/resources/org/monarchinitiative/vmvt/hexamer/hexamer-scores.tsv. */
    private static final Path HEXAMER_TSV_PATH = Paths.get(FileKMerParser.class.getResource("hexamer-scores.tsv").getPath());
    /** Default location for this file is src/main/resources/org/monarchinitiative/vmvt/hexamer/hexamer-scores.tsv. */
    private static final Path HEPTAMER_TSV_PATH = Paths.get(FileKMerParser.class.getResource("heptamer-scores.tsv").getPath());

    private final Map<String, Double> kmerMap;

    private FileKMerParser(Path kmerTsvPath) throws IOException {
        kmerMap = parseKmerTsvFile(kmerTsvPath);
    }

    public static Map<String, Double> hexamerMap() {
        try {
            return new FileKMerParser(HEXAMER_TSV_PATH).getKmerMap();
        } catch (IOException e) {
            throw new VmvtRuntimeException(String.format("Could not parse %s", HEXAMER_TSV_PATH.getFileName()));
        }
    }

    public static Map<String, Double> heptamerMap() {
        try {
            return new FileKMerParser(HEPTAMER_TSV_PATH).getKmerMap();
        } catch (IOException e) {
            throw new VmvtRuntimeException(String.format("Could not parse %s", HEPTAMER_TSV_PATH.getFileName()));
        }
    }


    private static Map<String, Double> parseKmerTsvFile(Path kmerTsvPath) throws IOException {
        Map<String, Double> kmerMap = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(kmerTsvPath)) {
            reader.lines()
                    .filter(emptyLines()).filter(comments())
                    .forEach(line -> {
                        final String[] tokens = line.split("\t");
                        if (tokens.length != 2) {
                            LOGGER.warn("Invalid line {}", line);
                            return;
                        }
                        final String seq = tokens[0].toUpperCase();

                        final double score;
                        try {
                            score = Double.parseDouble(tokens[1]);
                        } catch (NumberFormatException e) {
                            LOGGER.warn("Invalid score {} in line {}", tokens[1], line);
                            return;
                        }
                        kmerMap.put(seq, score);
                    });

        }
        return kmerMap;
    }

    private static Predicate<? super String> comments() {
        return line -> !line.startsWith("#");
    }

    private static Predicate<? super String> emptyLines() {
        return line -> !line.isEmpty();
    }

    public Map<String, Double> getKmerMap() {
        return kmerMap;
    }
}
