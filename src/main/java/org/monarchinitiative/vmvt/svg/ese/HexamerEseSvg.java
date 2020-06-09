package org.monarchinitiative.vmvt.svg.ese;

import org.monarchinitiative.vmvt.hexamer.FileKMerParser;
import org.monarchinitiative.vmvt.hexamer.HexamerFeatureCalculator;

public class HexamerEseSvg extends EseSvg {

    private final static HexamerFeatureCalculator hexaCalc = new HexamerFeatureCalculator(FileKMerParser.hexamerMap());

    public HexamerEseSvg(String reference, String alternate) {
        super(hexaCalc, reference, alternate);
    }

}
