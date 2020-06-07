package org.monarchinitiative.vmvt.dist;




import org.monarchinitiative.vmvt.pssm.DoubleMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * We will model the distribution of scores of the R_i as a normal distribution. Here,
 * we will collect all possible R_i scores for R_i. We will write 10,000 of these to a file
 * for visualization, and we will also calculate the mean and stddev
 * @author Peter N Robinson
 */
public class DistributionCalculator  {

    private final int seqlen;
    private final static int A_BASE = 0;
    private final static int C_BASE = 1;
    private final static int G_BASE = 2;
    private final static int T_BASE = 3;
    private final DoubleMatrix splicesite;

    private final List<Double> values;
    private final List<Double> deltas;


    private final Random random;


    private final int [] currentIndices;
    /** This is used to create a variant sequence that differs from the
     * original sequence in one of multiple nucleotides.
     */
    private final int [] variantIndices;

    private final double mean;

    private final static int DEFAULT_NUM_SAMPLES = 100_000;

    public DistributionCalculator(DoubleMatrix site) {
        this(site, DEFAULT_NUM_SAMPLES);
    }

    /**
     * @param site A representating of the donor/acceptor
     * @param n_samples Number of samples to take to estimate distribution of differences for SNV
     */
    public DistributionCalculator(DoubleMatrix site, int n_samples) {
        this.splicesite = site;
        values = new ArrayList<>();
        deltas = new ArrayList<>();
        seqlen = site.getMotifLength();
        currentIndices = new int[seqlen]; // if all indices are zero, we have AAAAAAAA etc.
        variantIndices = new int[seqlen];
        random = new Random();
        long max = (long)Math.pow(4, seqlen);
        if (seqlen==9){
            // i.e., donor sequence, we can calculate everything
            for (long i = 0; i < max; i++) {
                double R_i = getR_i(i);
                values.add(R_i);
                createVariantSequence(); // makes a modified copy of current sequence
                double var_r_i = this.splicesite.getIndividualSequenceInformation(this.variantIndices);
                double delta = R_i - var_r_i;
                deltas.add(delta);
                if (i % 10_000_000 == 0) {
                    double perc = 100.0 * (double) i / (double) max;
                    System.out.printf("%d/%d (%.1f%%).\n", i, max, perc);
                }
            }
            mean = values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
            System.out.printf("Total %d mean %f.\n", values.size(), mean);
        } else {
            // acceptor sequence, too long to calculate everything, let's just sample
            // 100 thousand times
            for (int i = 0; i<100_000; i++) {
                long j = (long) (Math.random() * max);
                double R_i = getR_i(j);
                values.add(R_i);
                createVariantSequence(); // makes a modified copy of current sequence
                double var_r_i = this.splicesite.getIndividualSequenceInformation(this.variantIndices);
                double delta = R_i - var_r_i;
                deltas.add(delta);
                if (i % 1_000_000 == 0) {
                    double perc = 100.0 * (double) i / (double) 10_000_000;
                    System.out.printf("%d/%d (%.1f%%).\n", i, 10_000_000, perc);
                }
            }
            mean = values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
            System.out.printf("Total %d mean %f.\n", values.size(), mean);
        }
    }

    public List<Double> getValues() {
        return values;
    }

    public List<Double> getDeltas() {
        return deltas;
    }

    private double getR_i(long seq) {
        for (int i=0;i<seqlen;i++) {
            int remainder = (int)(seq % 4);
            currentIndices[i] = remainder;
            seq /= 4;
        }
        return splicesite.getIndividualSequenceInformation(currentIndices);
    }

    /**
     * There are four bases. This function chooses a base that is different from 'current' at random
     * @param current current reference base
     * @return one of the three bases other than current, at random
     */
    private int getRandomIndex(int current) {
        int r = random.nextInt(4);
        while (current==r){
            r = random.nextInt(4);
        }
        return r;
    }

    /**
     * This function creates a random single-nucleotide change in compared to the sequence
     * that is currently represented in {@link #currentIndices} and places the variant
     * sequence into {@link #variantIndices}. It choose a random position within the sequence
     * and then choose a different base at random.
     */
    private void createVariantSequence() {
        System.arraycopy(this.currentIndices, 0, this.variantIndices, 0, seqlen);
        int i = random.nextInt(seqlen);
        int randomBase = getRandomIndex(this.currentIndices[i]);
        variantIndices[i] = randomBase;
    }


    /**
     * Write the R_i scores of the examined sequences (from values) and write the delta scores.
     * This function can be used to write out results for plotting in R, but is not needed for
     * creating SVG files.
     * @param fname Name of the output file for the R_i values
     * @param deltaFname Name of the output file for the delta-R_i values.
     */
    public void writeVals(String fname, String deltaFname) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fname))) {
            for (Double v : values) {
                writer.write(v + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(deltaFname))) {
            for (Double v : deltas) {
                writer.write(v + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
