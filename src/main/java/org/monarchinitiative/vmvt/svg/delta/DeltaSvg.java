package org.monarchinitiative.vmvt.svg.delta;



import org.monarchinitiative.vmvt.except.VmvtRuntimeException;

import java.util.List;

public class DeltaSvg {

    private final List<Double> deltavals;
    private final double min;
    private final double max;

    private static final int BIN_COUNT = 51;
    private final int [] bins;


    public DeltaSvg(List<Double> deltas) {
        this.deltavals = deltas;
        this.min = deltavals.stream().mapToDouble(Double::doubleValue).min().orElseThrow();
        this.max = deltavals.stream().mapToDouble(Double::doubleValue).max().orElseThrow();
        double span = max - min;
        if (span==0.0) {
            throw new VmvtRuntimeException("min == max in DeltaSvg");
        }
        bins = new int[BIN_COUNT];
        for (Double d : deltavals) {
            double normalized = (d-min)/span;
            int i = (int)Math.ceil((BIN_COUNT-1) * normalized);
            bins[i]++;
        }
    }


    public void dump() {
        for (int i=0;i<bins.length;i++) {
            System.out.printf("%d: %d\n", i, bins[i]);
        }
    }


}
