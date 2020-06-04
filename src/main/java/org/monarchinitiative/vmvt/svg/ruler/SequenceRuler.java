package org.monarchinitiative.vmvt.svg.ruler;

import org.monarchinitiative.vmvt.svg.AbstractSvgCoreWriter;

import java.io.IOException;
import java.io.Writer;
/**
 * Write an SVG element for the sequence of the reference sequence. Also show the deviating alternate bases and
 * draw a box around them.
 * @author Peter N Robinson
 */
public class SequenceRuler extends AbstractSvgCoreWriter {
    /** Initial X position. */
    protected final int startX;
    /** Initial Y position. */
    protected final int startY;
    /** An additional amount of Y distance to skip after we finish with the Sequence Ruler. */
    private final int Y_SKIP = 40;

    public SequenceRuler(String ref, String alt, int w, int h, int X, int Y) {
        super(ref, alt, w, h);
        this.startX = X;
        this.startY = Y;
    }

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
                        " style=\"stroke-width:2; stroke:rgb(4, 12, 4);\"/>",
                X,
                Y,
                boxwidth,
                boxheight));

    }

    @Override
    public void write(Writer writer) throws IOException {
        writeRefPlain(writer);
        writeAltPlain(writer);
        writeBoxAroundMutation(writer);
    }

    @Override
    public int getYincrement() {
        return this.Y_SKIP + this.currentY - this.startY;
    }
}
