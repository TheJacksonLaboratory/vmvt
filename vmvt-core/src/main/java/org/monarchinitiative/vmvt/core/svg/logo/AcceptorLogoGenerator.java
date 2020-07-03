package org.monarchinitiative.vmvt.core.svg.logo;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgCoreGenerator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Write an SVG sequence logo for a splice acceptor site with reference and alternate sequences
 * @author Peter N Robinson
 */
public class AcceptorLogoGenerator extends AbstractSvgGenerator {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;

    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorLogoGenerator(String ref, String alt) {
        super( SVG_ACCEPTOR_WIDTH, SVG_LOGO_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.acceptorHeightMatrix();
    }

    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorLogoGenerator(String ref, String alt, DoubleMatrix acceptorHeight) {
        super( SVG_ACCEPTOR_WIDTH, SVG_LOGO_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = acceptorHeight;
    }

    @Override
    public String getSvg() {
        int startX = SVG_STARTX;
        int startY = SVG_LOGO_STARTY;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator acceptorLogo = new SvgSequenceLogo(reference, alternate, this.splicesite, WIDTH, HEIGHT, startX, startY);
            acceptorLogo.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}