package org.monarchinitiative.vmvt.svg.ese;

import org.monarchinitiative.vmvt.hexamer.FileKMerParser;
import org.monarchinitiative.vmvt.hexamer.HeptamerFeatureCalculator;

import org.monarchinitiative.vmvt.hexamer.KmerFeatureCalculator;

public class HeptamerEseSvg extends EseSvg {
    private final static KmerFeatureCalculator heptaCalc = new HeptamerFeatureCalculator(FileKMerParser.heptamerMap());
    /** Canvas width of the SVG. */
    private final static int SVG_WIDTH = 400;
    /** Canvas height of the SVG. */
    private final static int SVG_HEIGHT = 400;

    public HeptamerEseSvg(String reference, String alternate) {
        super(heptaCalc, reference, alternate, SVG_WIDTH, SVG_HEIGHT);
    }

    @Override
    public String getSvg() {
        return null;
    }
}
