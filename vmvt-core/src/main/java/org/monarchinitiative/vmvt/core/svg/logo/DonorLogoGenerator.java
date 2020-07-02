package org.monarchinitiative.vmvt.core.svg.logo;


import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgCoreGenerator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.ruler.PositionRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.SequenceRuler;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Write an SVG sequence logo for a splice donor site with reference and alternate sequences
 * @author Peter N Robinson
 */
public class DonorLogoGenerator extends AbstractSvgGenerator {

    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;


    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public DonorLogoGenerator(String ref, String alt) {
        super(SVG_DONOR_WIDTH, SVG_LOGO_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.donorHeightMatrix();
    }

    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public DonorLogoGenerator(String ref, String alt, DoubleMatrix donorHeight) {
        super(SVG_DONOR_WIDTH, SVG_LOGO_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = donorHeight;
    }

    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator donorLogo = new SvgSequenceLogo(reference,
                    alternate,
                    this.splicesite,
                    WIDTH,
                    HEIGHT,
                    SVG_STARTX,
                    SVG_LOGO_STARTY);
            donorLogo.write(swriter);
           writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
