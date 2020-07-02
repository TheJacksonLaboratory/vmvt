package org.monarchinitiative.vmvtcore.svg.ese;

import org.monarchinitiative.vmvtcore.hexamer.FileKMerParser;
import org.monarchinitiative.vmvtcore.hexamer.HexamerFeatureCalculator;

public class HexamerEseSvg extends EseSvg {

    private final static HexamerFeatureCalculator hexaCalc = new HexamerFeatureCalculator(FileKMerParser.hexamerMap());

    public HexamerEseSvg(String reference, String alternate) {
        super(hexaCalc, reference, alternate);
    }

}
