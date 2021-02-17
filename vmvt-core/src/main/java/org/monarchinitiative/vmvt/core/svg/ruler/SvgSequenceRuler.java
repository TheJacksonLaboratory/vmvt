package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;
import org.monarchinitiative.vmvt.core.svg.SvgInitializer;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.DONOR_NT_LENGTH;

public abstract class SvgSequenceRuler implements SvgComponent, SvgInitializer {

    protected final int seqlen;
    /** A coding of the String representing the reference sequence using A=0,C=1,G=2,T=3. */
    private final int [] refidx;
    /** A coding of the String representing the alternate sequence using A=0,C=1,G=2,T=3. */
    private final int [] altidx;
    /** This is used for cryptic splice sites. An offset of -2 incidates, for instance, that the
     * cryptic splice site in shifted by 2 nucleotides in 5' direction. For the canonical splice site,
     * the offset is zero.
     */
    protected final int offset;

    /**
     * This constructor should be used for cryptic splice sites in order to display the offset to the
     * canonical splice site.
     * @param ref reference sequence of a splice site
     * @param alt alternate sequence of a splice site
     * @param offset offset of this (cryptic) site site to the corresponding canonical splice site
     */
    public SvgSequenceRuler(String ref, String alt, int offset) {
        this.seqlen = sequenceLength(ref, alt);
        this.refidx = sequenceIndex(ref);
        this.altidx = sequenceIndex(alt);
        this.offset = offset;
    }

    abstract void writePositionRuler(Writer writer, int starty) throws IOException;
    abstract void writeOffsetPositionRuler(Writer writer, int startY) throws IOException;

    protected void writeRefPlain(Writer writer, int ypos) throws IOException {
        int X = SVG_STARTX;
        for (int i=0; i<seqlen; i++) {
            writePlainBase(writer, X, ypos, refidx[i]);
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }

    protected void writeAltPlain(Writer writer, int ypos) throws IOException {
        int X = SVG_STARTX;
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
        double X = SVG_STARTX + b*LOWER_CASE_BASE_INCREMENT;
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
        if (this.offset == 0) {
            writePositionRuler(writer, ypos + 10);
        } else {
            writeOffsetPositionRuler(writer, starty + 10);
        }
        ypos += SVG_RULER_POSITION_Y_INCREMENT;
        int Y_LINE_INCREMENT = 20;
        writeRefPlain(writer, ypos);
        writeAltPlain(writer, ypos+Y_LINE_INCREMENT);
        writeBoxAroundMutation(writer,ypos);
    }


    @Override
    public int height() {
        return SvgConstants.Dimensions.SVG_RULER_HEIGHT;
    }

    public static SvgSequenceRuler donor(String ref, String alt) {
        return new DonorRuler(ref, alt);
    }

    public static SvgSequenceRuler donorWithOffset(String ref, String alt, int offset) {
        return new DonorRuler(ref, alt, offset);
    }

    public static SvgSequenceRuler acceptor(String ref, String alt) {
        return new AcceptorRuler(ref, alt);
    }

    public static SvgSequenceRuler acceptorWithOffset(String ref, String alt, int offset) {
        return new AcceptorRuler(ref, alt, offset);
    }
}
