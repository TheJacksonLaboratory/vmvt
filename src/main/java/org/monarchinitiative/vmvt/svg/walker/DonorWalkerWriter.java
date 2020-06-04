package org.monarchinitiative.vmvt.svg.walker;

import org.monarchinitiative.vmvt.svg.ruler.PositionRuler;
import org.monarchinitiative.vmvt.svg.ruler.SequenceRuler;
import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.AbstractSvgCoreWriter;
import org.monarchinitiative.vmvt.svg.AbstractSvgWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Write an SVG sequence walker for a splice donor variant
 * @author Peter N Robinson
 */
public class DonorWalkerWriter extends AbstractSvgWriter {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;

    private final static int SVG_WIDTH = 400;
    private final static int SVG_HEIGHT = 400;

    /**
     * Write a sequence writer for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public DonorWalkerWriter(String ref, String alt) {
        super(SVG_WIDTH,SVG_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.donor();
    }

    @Override
    public String getSvg() {
        int startX = 20;
        int startY = 60;

        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreWriter posRuler = new PositionRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            posRuler.write(swriter);
            startY += posRuler.getYincrement();
            AbstractSvgCoreWriter sequenceRuler = new SequenceRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            sequenceRuler.write(swriter);
            startY += sequenceRuler.getYincrement();
            AbstractSvgCoreWriter donorWalker =
                    new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, startX, startY);
            donorWalker.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }


}
