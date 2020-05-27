package org.jax.svgwalker.svg.walker;

import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;

public class AcceptorWalkerWriter extends SvgSequenceWalker {

    /**
     * Write a sequence writer for a splice acceptor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorWalkerWriter(String ref, String alt) {
        super(ref, alt, DoubleMatrix.acceptor(),500,400);
    }

    @Override
    public String getWalker() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            initXYpositions();
            writeRefPlain(swriter);
            writeAltPlain(swriter);
            writeBoxAroundMutation(swriter);
            incrementYposition();
            writeRefWalker(swriter);
            writeRefAltSeparation(swriter);
            writeAltWalker(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
