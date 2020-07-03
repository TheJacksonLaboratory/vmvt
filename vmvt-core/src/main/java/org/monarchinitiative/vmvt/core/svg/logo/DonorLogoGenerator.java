package org.monarchinitiative.vmvt.core.svg.logo;


import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

/**
 * Write an SVG sequence logo for a splice donor site with reference and alternate sequences
 * @author Peter N Robinson
 */
public class DonorLogoGenerator extends SvgSequenceLogo {

    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     */
    public DonorLogoGenerator(DoubleMatrix donorHeight) {
        super(donorHeight, SVG_DONOR_WIDTH, SVG_LOGO_HEIGHT);
    }

}
