package org.jax.svgwalker.pssm;

import java.util.List;

public class DoubleMatrix {

    private final double[] vals;
    private final int nRows;
    private final int nCols;

    public DoubleMatrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        vals = new double[nRows*nCols];
    }

    public DoubleMatrix(double othervals[][]) {
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

    public double get(int i, int j) {
        return this.vals[i*nCols + j];
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
}
