package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

/**
 * Write an SVG sequence walker for a splice donor variant
 * @author Peter N Robinson
 */
public class DonorWalkerGenerator extends SvgSequenceWalker {


    /**
     * Write a sequence writer for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public DonorWalkerGenerator(String ref, String alt) {
        super(ref, alt, DoubleMatrix.donor(), SVG_DONOR_WIDTH,SVG_WALKER_HEIGHT);
    }

    /**
     * Write a sequence writer for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     * @param donor Donor information content matrix
     */
    public DonorWalkerGenerator(String ref, String alt, DoubleMatrix donor) {
        super(ref, alt, donor, SVG_DONOR_WIDTH,SVG_WALKER_HEIGHT);
    }



}
