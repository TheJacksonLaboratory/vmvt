package org.jax.vmvt.svg.walker;

import org.jax.vmvt.VmtVisualizer;
import org.jax.vmvt.pssm.DoubleMatrix;
import org.jax.vmvt.svg.AbstractSvgCoreWriter;
import org.jax.vmvt.svg.AbstractSvgMotifWriter;
import org.jax.vmvt.svg.AbstractSvgWriter;
import org.jax.vmvt.svg.ruler.SequenceRuler;

import java.io.IOException;
import java.io.StringWriter;

public class AcceptorWalkerWriter extends AbstractSvgWriter implements VmtVisualizer {

    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;

    /**
     * Write a sequence writer for a splice acceptor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorWalkerWriter(String ref, String alt) {
        super(500,400);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.acceptor();
    }

    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        int rulerStartX = 20;
        int rulerStartY = 40;
        // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
        AbstractSvgCoreWriter sequenceRuler = new SequenceRuler(reference, alternate,WIDTH, HEIGHT, rulerStartX, rulerStartY);
        AbstractSvgCoreWriter acceptorWalker =
                new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT,rulerStartX,rulerStartY);
        try {
            writeHeader(swriter);
            sequenceRuler.write(swriter);
//            initXYpositions();
//            writeRefPlain(swriter);
//            writeAltPlain(swriter);
//            writeBoxAroundMutation(swriter);
//            incrementYposition();
//            writeRefWalker(swriter);
//            writeRefAltSeparation(swriter);
//            writeAltWalker(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
