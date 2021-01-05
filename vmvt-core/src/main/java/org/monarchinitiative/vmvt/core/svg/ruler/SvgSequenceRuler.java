package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.svg.SvgInitializer;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;

public abstract class SvgSequenceRuler implements SvgComponent, SvgInitializer {

    protected final int startX = SVG_STARTX;
    /** Height on the SVG canvas for Sequence rulers. */
    protected final static int SVG_RULER_HEIGHT = 110;
    protected final static int SVG_RULER_STARTY = 30;
    protected int startY = SVG_RULER_STARTY;

    protected final int seqlen;
    /** A coding of the String representing the reference sequence using A=0,C=1,G=2,T=3. */
    private final int [] refidx;
    /** A coding of the String representing the alternate sequence using A=0,C=1,G=2,T=3. */
    private final int [] altidx;

    private final int width;


    private final static int SVG_RULER_POSITION_Y_INCREMENT = 40;

    public SvgSequenceRuler(int w, int h, String ref, String alt) {
        this.seqlen = sequenceLength(ref, alt);
        this.refidx = sequenceIndex(ref);
        this.altidx = sequenceIndex(alt);
        this.width = w;
    }

    abstract void writePositionRuler(Writer writer, int starty) throws IOException;

    protected void writeRefPlain(Writer writer, int ypos) throws IOException {
        int X = startX;
        for (int i=0; i<seqlen; i++) {
            writePlainBase(writer, X, ypos, refidx[i]);
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }

    protected void writeAltPlain(Writer writer, int ypos) throws IOException {
        int X = startX;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writePlainBase(writer, X, ypos, altidx[i]);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }


    protected void writeBoxAroundMutation(Writer writer, int ypos) throws IOException {
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
        int Y = (int)(ypos - 1.6*LETTER_BASE_HEIGHT);
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
    public void write(Writer writer, int starty) throws IOException {
        int ypos = starty;
        writePositionRuler(writer, ypos);
        ypos += SVG_RULER_POSITION_Y_INCREMENT;
        int Y_LINE_INCREMENT = 20;
        writeRefPlain(writer, ypos);
        writeAltPlain(writer, ypos+Y_LINE_INCREMENT);
        writeBoxAroundMutation(writer,ypos);
    }

    public static SvgSequenceRuler donor(String ref, String alt) {
        return new DonorRuler(ref, alt);
    }

    public static SvgSequenceRuler acceptor(String ref, String alt) {
        return new AcceptorRuler(ref, alt);
    }
}
