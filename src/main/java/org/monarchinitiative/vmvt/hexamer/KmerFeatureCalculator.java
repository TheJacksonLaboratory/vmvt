package org.monarchinitiative.vmvt.hexamer;

import com.google.common.collect.ImmutableMap;


import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class KmerFeatureCalculator {

    protected final ImmutableMap<String, Double> kmerMap;

    protected KmerFeatureCalculator(Map<String, Double> kmerMap) {
        this.kmerMap = ImmutableMap.copyOf(kmerMap);
    }

    /**
     * Calculate score for given nucleotide sequence.
     *
     * @param sequence String with sequence to be scored
     * @return score for the sequence or {@link Double#NaN} if there is invalid nucleotide character present, or if
     * length of the {@code sequence} is less than {@link #getPadding()}+1
     */
    double scoreSequence(String sequence) {
        return slidingWindow(sequence.toUpperCase(), getPadding() + 1)
                .map(kmer -> kmerMap.getOrDefault(kmer, Double.NaN))
                .reduce(Double::sum)
                .orElse(Double.NaN);
    }

    public abstract int getPadding();
    /** @return 6 or 7 (hexa or hepta) */
    public abstract int getKmerLength();


    /**
     * Create subsequences/windows of size <code>'ws'</code> from nucleotide <code>sequence</code>.
     *
     * @param sequence {@link String} with nucleotide sequence to generate subsequences from
     * @param ws       window size
     * @return {@link Stream} of {@link String}s - subsequences of given <code>sequence</code> with length
     * <code>ws</code> or empty {@link Stream}, if '<code>ws</code> > <code>sequence.length()</code>'
     */
    public static Stream<String> slidingWindow(String sequence, int ws) {
        return ws > sequence.length()
                ? Stream.empty()
                : IntStream.range(0, sequence.length() - ws + 1)
                .boxed()
                .map(idx -> sequence.substring(idx, idx + ws));
    }



    public double[] kmerScoreArray(String sequence) {
        int len = getKmerLength();
        double [] scores = new double[len];
        for (int i=0;i<scores.length;i++) {
            String subseq = sequence.substring(i,i+len);
            scores[i] = kmerMap.getOrDefault(subseq,0.0);
        }
        return scores;
    }

    public double delta(String reference, String alternate) {
        double refScore = scoreSequence(reference);
        double altScore = scoreSequence(alternate);
        // subtract total alt from total ref
        // the score should be high if the alt allele abolishes ESE element in ref allele
        return refScore - altScore;
    }
}
