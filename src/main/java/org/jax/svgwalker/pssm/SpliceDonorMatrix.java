package org.jax.svgwalker.pssm;

import java.util.List;

/**
 * For convenience and speed, this class contains the frequency matrix
 * of the Splice done, from position -3 to +6.
 */
public class SpliceDonorMatrix {
    private static final List<Double> A = List.of(0.332, 0.638, 0.097, 0.002, 0.001, 0.597, 0.683, 0.091, 0.179);
    private static final List<Double> C = List.of(0.359, 0.107, 0.028, 0.001, 0.012, 0.031, 0.079, 0.060, 0.152);
    private static final List<Double> G = List.of(0.186, 0.117, 0.806, 0.996, 0.001, 0.339, 0.122, 0.771, 0.191);
    private static final List<Double> T = List.of(0.123, 0.139, 0.069, 0.001, 0.986, 0.032, 0.116, 0.079, 0.478);
    private static final List<List<Double>> donor
            = List.of(A,C,G,T);


  public static List<List<Double>> get() {
      return donor;
  }


}
