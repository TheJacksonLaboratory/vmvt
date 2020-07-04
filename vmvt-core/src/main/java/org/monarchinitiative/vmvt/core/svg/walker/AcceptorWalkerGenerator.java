package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

/**
 * Write an SVG sequence walker for a splice acceptor variant
 * @author Peter N Robinson
 */
public class AcceptorWalkerGenerator extends SvgSequenceWalker {

    /**
     * Write a sequence writer for a splice acceptor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorWalkerGenerator(String ref, String alt) {
        super(ref, alt,DoubleMatrix.acceptor(),SVG_ACCEPTOR_WIDTH,SVG_WALKER_HEIGHT);
    }

    /**
     * Write a sequence writer for a splice acceptor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorWalkerGenerator(String ref, String alt, DoubleMatrix acceptor) {
        super(ref, alt, acceptor, SVG_ACCEPTOR_WIDTH,SVG_WALKER_HEIGHT);
    }

}
