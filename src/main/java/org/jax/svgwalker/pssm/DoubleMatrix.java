package org.jax.svgwalker.pssm;

import org.jax.svgwalker.except.SvgwalkerRuntimeException;

import java.util.*;

public class DoubleMatrix {

    private final double[] vals;
    private final int nRows;
    private final int nCols;

    private final static double LOG_TWO = Math.log(2);

    public DoubleMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        vals = new double[nRows*nCols];
    }

    public DoubleMatrix(double[][] othervals) {
        this.nRows = othervals.length;
        this.nCols = othervals[0].length;
        vals = new double[nRows*nCols];
        for (int i=0; i<nRows; i++) {
            for (int j=0; j<nCols; j++) {
                vals[i*nCols +j] = othervals[i][j];
            }
        }
    }

    public void put(int i, int j, double val) {
        this.vals[i*nCols + j] = val;
    }

    /**
     *
     * @param i Column index (ranges from 0 to seqlen-1)
     * @param j Row index (ranges from 0=A,1=C,2=G,3=T).
     * @return value of corresponding table cell (usually frequency or IC)
     */
    public double get(int i, int j) {
        return this.vals[i*nCols + j];
    }

    /**
     * Convert {@link DoubleMatrix} with nucleotide frequencies into array of information content values.
     * <p>
     * Basically, {@link #calculateIC(double)} method is mapped element-wise to given <code>freqMatrix</code>.
     *
     * @param freqMatrix {@link DoubleMatrix} containing nucleotide frequencies at positions of splice site
     * @return {@link DoubleMatrix} containing information content of nucleotides at positions of splice site with the
     * same shape as <code>freqMatrix</code>
     */
     static DoubleMatrix createICMatrix(DoubleMatrix freqMatrix) {
        DoubleMatrix icm = new DoubleMatrix(freqMatrix.nRows, freqMatrix.nCols);
        int len = freqMatrix.nRows * freqMatrix.nCols;
        for (int i = 0; i < len; i++) {
           // iterate through all nucleotides
            // we do not need to care about row or column
            icm.vals[i] = calculateIC(freqMatrix.vals[i]);
            System.out.println(icm.vals[i]);
        }
        return icm;
    }


    public static DoubleMatrix heightMatrix(DoubleMatrix freqMatrix) {
        DoubleMatrix heightMatrix = new DoubleMatrix(freqMatrix.nRows, freqMatrix.nCols);
        for (int col=0;col<freqMatrix.nCols;col++) {
            // first get the Rsequence for this column
            double H = 0.0;
            for (int base=0;base<4;base++) {
                double f = freqMatrix.get(base,col);
                H -= f * Math.log(f) / LOG_TWO;
            }
            double Rsequence = 2.0 -H;
            // The height of base b at position l = f(b,l) * Rseqence(l)
            for (int base=0;base<4;base++) {
                double f = freqMatrix.get(base,col);
                double height = f * Rsequence;
                heightMatrix.put(base, col, height);
            }
        }
        return heightMatrix;
    }





    /**
     * Calculate information content of the nucleotide from the frequency using formula 1 (Rogan paper from class
     * description). Correction factor is ignored, I assume that the sample size used to calculate the nucleotide
     * frequency is large enough. In case of the splice sites it was ~220000 sites.
     *
     * @param freq {@link Double} frequency of nucleotide occurence at its position from range <0, 1>
     * @return {@link Double} with information content value
     */
    private static double calculateIC(double freq) {
        return 2d - (-Math.log(freq) / Math.log(2));
    }

    /**
     * As a sanity check, we look to see that the frequencies add up to one.
     * The frequencies are given with 3 decimal places, so our epsilon needs
     * to be 0.01
     * @param vals List of base frequencies
     * @return A Double matrix of frequencies
     */
    static DoubleMatrix mapToDoubleMatrix(List<List<Double>> vals) {
        double epsilon = 0.01;
        return mapToDoubleMatrix(vals, epsilon);
    }

    /**
     * Map InputStreamBasedPositionalWeightMatrixParser.PositionWeightMatrix to {@link DoubleMatrix} and perform sanity checks:
     * <ul>
     * <li>entries for all 4 nucleotides must be present</li>
     * <li>entries for all nucleotides must have the same size</li>
     * <li>probabilities/frequencies at each position must sum up to 1</li>
     * </ul>
     *
     * @param vals    This list should contain another four lists. Each inner list represents one of the nucleotides
     *                A, C, G, T in this order
     * @param epsilon Tolerance when checking that probabilities sum up to 1
     * @return {@link DoubleMatrix} with data from <code>io</code>
     */
    static DoubleMatrix mapToDoubleMatrix(List<List<Double>> vals, double epsilon) {
        if (vals == null)
            throw new IllegalArgumentException("Unable to create matrix with 0 rows");
        if (vals.size() != 4)
            throw new IllegalArgumentException("Matrix does not have 4 rows for 4 nucleotides");
        // all four lists must have the same size
        int size = vals.get(0).size();
        if (vals.stream().anyMatch(inner -> inner.size() != size))
            throw new IllegalArgumentException("Rows of the matrix do not have the same size");
        // probabilities at each position of donor and acceptor matrices sum up to 1 and issue a warning when
        // the difference is larger than allowed in the EPSILON} parameter
        for (int pos_idx = 0; pos_idx < size; pos_idx++) {
            double sum = 0;
            for (int nt_idx = 0; nt_idx < 4; nt_idx++) {
                sum += vals.get(nt_idx).get(pos_idx);
            }
            if (Math.abs(sum - 1D) > epsilon)
                throw new IllegalArgumentException(String.format("Probabilities do not sum up to 1 at column %d", pos_idx));
        }
        // checks are done
        DoubleMatrix dm = new DoubleMatrix(vals.size(), vals.get(0).size());
        for (int rowIdx = 0; rowIdx < vals.size(); rowIdx++) {
            List<Double> row = vals.get(rowIdx);
            for (int colIdx = 0; colIdx < row.size(); colIdx++) {
                dm.put(rowIdx, colIdx, row.get(colIdx));
            }
        }
        return dm;
    }

    private String getBase(int j) {
        switch (j) {
            case 0:
                return "A";
            case 1:
                return "C";
            case 2:
                return "G";
            case 3:
                return "T";
            default:
                // should never happen
                throw new SvgwalkerRuntimeException("Unrecognized base index");
        }
    }

    /**
     * Get a map with the IC values for all bases in a column.
     * Order the map by descending value of information content
     * @param i column index
     * @return ordered map of IC values for the four bases ("A", "C", "G", "T")
     */
    public Map<String, Double> getIcValuesColumn(int i) {
        Map<String, Double> unSortedMap = new HashMap<>();
        for (int j = 0; j<4; j++) {
            String base = getBase(j);
            double ic = get(j,i);
            if (ic < 0) {
                throw new RuntimeException("IC="+ic);
            }
            unSortedMap.put(base, ic);
        }
        // now sort the bases according to IC
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }

    /**
     * @return A matrix representing the information content of the splice donor site
     */
    public static DoubleMatrix donor() {
        DoubleMatrix dm = DoubleMatrix.mapToDoubleMatrix(SpliceDonorMatrix.get());
        return DoubleMatrix.createICMatrix(dm);
    }
    /**
     * @return A matrix representing the information content of the splice acceptor site
     */
    public static DoubleMatrix acceptor() {
        DoubleMatrix dm = DoubleMatrix.mapToDoubleMatrix(SpliceAcceptorMatrix.get());
        return DoubleMatrix.createICMatrix(dm);
    }

    /**
     * @return A matrix representing the information content of the splice donor site
     */
    public static DoubleMatrix donorHeightMatrix() {
        DoubleMatrix dm = DoubleMatrix.mapToDoubleMatrix(SpliceDonorMatrix.get());
        return DoubleMatrix.heightMatrix(dm);
    }
    /**
     * @return A matrix representing the information content of the splice acceptor site
     */
    public static DoubleMatrix acceptorHeightMatrix() {
        DoubleMatrix dm = DoubleMatrix.mapToDoubleMatrix(SpliceAcceptorMatrix.get());
        return DoubleMatrix.heightMatrix(dm);
    }
}
