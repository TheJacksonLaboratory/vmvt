package org.monarchinitiative.vmvt.core.svg.ese;

import org.monarchinitiative.vmvt.core.hexamer.FileKMerParser;
import org.monarchinitiative.vmvt.core.hexamer.HeptamerFeatureCalculator;
import org.monarchinitiative.vmvt.core.hexamer.KmerFeatureCalculator;

public class HeptamerEseSvg extends EseSvg {
    private final static KmerFeatureCalculator heptaCalc = new HeptamerFeatureCalculator(FileKMerParser.heptamerMap());


    public HeptamerEseSvg(String reference, String alternate, boolean framed) {
        super(heptaCalc, reference, alternate, framed);
    }

}
