package org.monarchinitiative.vmvt.svg.walker;

import org.monarchinitiative.vmvt.svg.ruler.PositionRuler;
import org.monarchinitiative.vmvt.svg.ruler.SequenceRuler;
import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.AbstractSvgCoreGenerator;
import org.monarchinitiative.vmvt.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Write an SVG sequence walker for a splice acceptor variant
 * @author Peter N Robinson
 */
public class AcceptorWalkerGenerator extends AbstractSvgGenerator {

    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;

    private final static int SVG_WIDTH = 500;
    private final static int SVG_HEIGHT = 400;

    /**
     * Write a sequence writer for a splice acceptor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorWalkerGenerator(String ref, String alt) {
        super(SVG_WIDTH,SVG_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.acceptor();
    }

    /**
     * Write a sequence writer for a splice acceptor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorWalkerGenerator(String ref, String alt, DoubleMatrix acceptor) {
        super(SVG_WIDTH,SVG_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = acceptor;
    }

    private AcceptorWalkerGenerator(int width, int height, String ref, String alt, DoubleMatrix donor) {
        super(width,height);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = donor;
    }

    public static AcceptorWalkerGenerator sequenceRuler(String ref, String alt, DoubleMatrix donor) {
        int width = 450;
        int height = 110;
        return new AcceptorWalkerGenerator(width, height, ref, alt, donor);
    }


    @Override
    public String getSvg() {
        int startX = 20;
        int startY = 60;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator posRuler = new PositionRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            posRuler.write(swriter);
            startY += posRuler.getYincrement();
            AbstractSvgCoreGenerator sequenceRuler = new SequenceRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            sequenceRuler.write(swriter);
            startY += sequenceRuler.getYincrement();
            AbstractSvgCoreGenerator acceptorWalker =
                    new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, startX, startY);
            acceptorWalker.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    public String getSequenceRulerSvg() {
        int startX = 20;
        int startY = 25;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator posRuler = new PositionRuler(reference, alternate, WIDTH, HEIGHT, startX, startY);
            posRuler.write(swriter);
            startY += posRuler.getYincrement();
            AbstractSvgCoreGenerator sequenceRuler = new SequenceRuler(reference, alternate, WIDTH, HEIGHT, startX, startY);
            sequenceRuler.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

}
