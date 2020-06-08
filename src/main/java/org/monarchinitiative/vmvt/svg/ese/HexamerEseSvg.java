package org.monarchinitiative.vmvt.svg.ese;

import org.monarchinitiative.vmvt.hexamer.FileKMerParser;
import org.monarchinitiative.vmvt.hexamer.HexamerFeatureCalculator;

public class HexamerEseSvg extends EseSvg {

    private final static HexamerFeatureCalculator hexaCalc = new HexamerFeatureCalculator(FileKMerParser.heptamerMap());
    /** Canvas width of the SVG. */
    private final static int SVG_WIDTH = 400;
    /** Canvas height of the SVG. */
    private final static int SVG_HEIGHT = 400;

    public HexamerEseSvg(String reference, String alternate) {
        super(hexaCalc, reference, alternate, SVG_WIDTH, SVG_HEIGHT);
    }

    @Override
    public String getSvg() {
        return null;
    }
}
