package org.monarchinitiative.vmvt.core.hexamer;


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
    public int getPadding() {
        return PADDING;
    }

    /** @return length of the hexamers, i.e., 6*/
    @Override
    public int getKmerLength() {
        return 6;
    }
}
