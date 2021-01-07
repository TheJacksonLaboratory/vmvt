package org.monarchinitiative.vmvt.core.jaspar;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;


import java.util.List;
import java.util.stream.Collectors;

public class JasparMatrix {

    private final String jasparId;
    private final String jasparName;
    private final DoubleMatrix frequencyMatrix;

    JasparMatrix(String matrixID, String name, List<Integer> aCounts, List<Integer> cCounts, List<Integer> gCounts, List<Integer> tCounts){
        this.jasparId = matrixID;
        this.jasparName = name;
        int N = aCounts.size();
        if (N != cCounts.size()) {
            throw new VmvtRuntimeException("Malformed JASPAR C array (Counts do not match A): ");
        } else  if (N != gCounts.size()) {
            throw new VmvtRuntimeException("Malformed JASPAR G array (Counts do not match A): ");
        } else  if (N != tCounts.size()) {
            throw new VmvtRuntimeException("Malformed JASPAR T array (Counts do not match A): ");
        }
        List<Double> A = aCounts.stream().map(Double::valueOf).collect(Collectors.toList());
        List<Double> C = cCounts.stream().map(Double::valueOf).collect(Collectors.toList());
        List<Double> G = gCounts.stream().map(Double::valueOf).collect(Collectors.toList());
        List<Double> T = tCounts.stream().map(Double::valueOf).collect(Collectors.toList());
        for (int i=0;i<N;i++) {
            double sum = A.get(i) + C.get(i) + G.get(i) + T.get(i);
            A.set(i, A.get(i) / sum);
            C.set(i, C.get(i) / sum);
            G.set(i, G.get(i) / sum);
            T.set(i, T.get(i) / sum);
        }
        final List<List<Double>> frequencies = List.of(A,C,G,T);
        this.frequencyMatrix = DoubleMatrix.fromJaspar(frequencies);
    }

    public String getJasparId() {
        return jasparId;
    }

    public String getJasparName() {
        return jasparName;
    }

    public DoubleMatrix getFrequencyMatrix() {
        return frequencyMatrix;
    }
}
