package org.monarchinitiative.vmvt.hexamer;


import java.util.Map;

public class HexamerFeatureCalculator extends KmerFeatureCalculator {

    /**
     * Since we are working with hexamers, the padding is 5bp. The remaining bp is coming from REF/ALT alleles.
     */
    private static final int PADDING = 5;

    public HexamerFeatureCalculator(Map<String, Double> kmerMap) {
        super(kmerMap);
    }

    @Override
    protected int getPadding() {
        return PADDING;
    }
}
