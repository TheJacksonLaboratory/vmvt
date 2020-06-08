package org.monarchinitiative.vmvt.hexamer;

import java.util.Map;

/**
 * This scorer uses SMS scores as described in <a href="https://pubmed.ncbi.nlm.nih.gov/29242188">Ke et al.</a>.
 */
public class HeptamerFeatureCalculator extends KmerFeatureCalculator {

    /**
     * Since we are working with septamers, the padding is 6bp. The remaining bp is coming from REF/ALT alleles.
     */
    private static final int PADDING = 6;

    public HeptamerFeatureCalculator(Map<String, Double> septamerMap) {
        super(septamerMap);
    }

    @Override
    protected int getPadding() {
        return PADDING;
    }

}
