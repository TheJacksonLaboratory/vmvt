package org.monarchinitiative.vmvt.core.svg.ese;

import org.monarchinitiative.vmvt.core.hexamer.FileKMerParser;
import org.monarchinitiative.vmvt.core.hexamer.HexamerFeatureCalculator;

public class HexamerEseSvg extends EseSvg {

    private final static HexamerFeatureCalculator hexaCalc = new HexamerFeatureCalculator(FileKMerParser.hexamerMap());

    public HexamerEseSvg(String reference, String alternate) {
        super(hexaCalc, reference, alternate);
    }

}
