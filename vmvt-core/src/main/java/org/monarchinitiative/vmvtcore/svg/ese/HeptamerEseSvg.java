package org.monarchinitiative.vmvtcore.svg.ese;

import org.monarchinitiative.vmvtcore.hexamer.FileKMerParser;
import org.monarchinitiative.vmvtcore.hexamer.HeptamerFeatureCalculator;

import org.monarchinitiative.vmvtcore.hexamer.KmerFeatureCalculator;

public class HeptamerEseSvg extends EseSvg {
    private final static KmerFeatureCalculator heptaCalc = new HeptamerFeatureCalculator(FileKMerParser.heptamerMap());


    public HeptamerEseSvg(String reference, String alternate) {
        super(heptaCalc, reference, alternate);
    }

}
