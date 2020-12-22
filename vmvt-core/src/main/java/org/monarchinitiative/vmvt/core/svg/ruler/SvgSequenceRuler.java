package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class SvgSequenceRuler extends AbstractSvgMotifGenerator {

    protected final int startX = SVG_STARTX;
    /** Height on the SVG canvas for Sequence rulers. */
    protected final static int SVG_RULER_HEIGHT = 110;
    protected final static int SVG_RULER_STARTY = 30;
    protected int startY = SVG_RULER_STARTY;

    private final static int SVG_RULER_POSITION_Y_INCREMENT = 40;

    public SvgSequenceRuler(int w, int h, String ref, String alt, boolean framed) {
        super(ref,alt, w, h, framed);
    }

    abstract void writePositionRuler(Writer writer) throws IOException;

    protected void writeRefPlain(Writer writer) throws IOException {
        int X = startX;
        int Y = startY;
        for (int i=0; i<seqlen; i++) {
            writePlainBase(writer, X, Y, refidx[i]);
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = this.startX;
        currentY = Y + Y_LINE_INCREMENT;
    }

    protected void writeAltPlain(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writePlainBase(writer, X, Y, altidx[i]);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = startX;
        currentY = Y + Y_LINE_INCREMENT;
    }


    protected void writeBoxAroundMutation(Writer writer) throws IOException {
        // get location of first and last index with mutated bases
        int b = Integer.MAX_VALUE;
        int e = Integer.MIN_VALUE;
        for (int i=0; i<refidx.length; i++) {
            if (refidx[i] != altidx[i]) {
                if (i<b) b = i;
                if (i>e) e = i;
            }
        }
        double X = this.startX + b*LOWER_CASE_BASE_INCREMENT;
        int Y = (int)(this.startY - 1.6*LETTER_BASE_HEIGHT);
        int boxwidth = LOWER_CASE_BASE_INCREMENT;
        int boxheight = (int)(LETTER_BASE_HEIGHT*4.1);
        writer.write(String.format("<rect x=\"%f\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" fill-opacity=\"0.1\"" +
                        " style=\"stroke-width:1; stroke:rgb(4, 12, 4);\"/>",
                X,
                Y,
                boxwidth,
                boxheight));

    }

    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    @Override
    public void write(Writer writer) throws IOException {
        writePositionRuler(writer);
        startY += SVG_RULER_POSITION_Y_INCREMENT;
        writeRefPlain(writer);
        writeAltPlain(writer);
        writeBoxAroundMutation(writer);
    }
}
