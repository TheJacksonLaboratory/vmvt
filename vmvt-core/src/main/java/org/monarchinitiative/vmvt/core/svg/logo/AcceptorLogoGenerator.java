package org.monarchinitiative.vmvt.core.svg.logo;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

/**
 * Write an SVG sequence logo for a splice acceptor site with reference and alternate sequences
 * @author Peter N Robinson
 */
public class AcceptorLogoGenerator extends SvgSequenceLogo {

    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     */
    public AcceptorLogoGenerator(DoubleMatrix acceptorHeight, boolean framed) {
        super(acceptorHeight, SVG_ACCEPTOR_WIDTH, SVG_LOGO_HEIGHT, framed);
    }

    public AcceptorLogoGenerator(DoubleMatrix donorHeight, int startY, boolean framed) {
        super(donorHeight, SVG_ACCEPTOR_WIDTH, SVG_LOGO_HEIGHT, startY, framed);
    }


}
