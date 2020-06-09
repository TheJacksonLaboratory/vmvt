package org.monarchinitiative.vmvt.svg.ese;

import org.monarchinitiative.vmvt.hexamer.FileKMerParser;
import org.monarchinitiative.vmvt.hexamer.HeptamerFeatureCalculator;

import org.monarchinitiative.vmvt.hexamer.KmerFeatureCalculator;

public class HeptamerEseSvg extends EseSvg {
    private final static KmerFeatureCalculator heptaCalc = new HeptamerFeatureCalculator(FileKMerParser.heptamerMap());


    public HeptamerEseSvg(String reference, String alternate) {
        super(heptaCalc, reference, alternate);
    }

}
