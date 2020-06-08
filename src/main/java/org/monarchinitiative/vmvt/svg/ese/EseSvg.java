package org.monarchinitiative.vmvt.svg.ese;

import org.monarchinitiative.vmvt.hexamer.KmerFeatureCalculator;
import org.monarchinitiative.vmvt.svg.AbstractSvgGenerator;

public abstract class EseSvg extends AbstractSvgGenerator  {
    /** Reference to a hexa (6) or hepta (7) feature calculator. */
    private final KmerFeatureCalculator calculator;

    private final String reference;
    private final String alternate;

    public EseSvg(KmerFeatureCalculator calc, String reference, String alternate, int w, int h) {
        super(w,h);
        this.calculator = calc;
        this.reference = reference;
        this.alternate = alternate;
    }

    @Override
    public abstract String getSvg();
}
