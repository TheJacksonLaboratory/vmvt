package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Colors.RED;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.ACCEPTOR_NT_LENGTH;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.DONOR_NT_LENGTH;

/**
 * Create an SVG graphic represening a splice-acceptor sequence ruler
 * @author Peter N Robinson
 */
public class AcceptorRuler extends SvgSequenceRuler {

    /**
     * This constructor should be used to show a sequence ruler for a canonical acceptor splice site, i.e., with
     * offset zero
     * @param ref reference sequence of a splice site
     * @param alt alternate sequence of a splice site
     */
    public AcceptorRuler(String ref, String alt) {
        this(ref, alt, 0);
    }

    /**
     * This constructor should be used for cryptic splice sites in order to display the offset to the
     * canonical acceptor splice site.
     * @param ref reference sequence of a splice site
     * @param alt alternate sequence of a splice site
     * @param offset offset of this (cryptic) site site to the corresponding canonical splice site
     */
    public AcceptorRuler(String ref, String alt, int offset) {
        super(ref, alt, offset);
        if (this.seqlen != ACCEPTOR_NT_LENGTH) {
            throw new VmvtRuntimeException(String.format("Sequence length must be %d for acceptor but was %d",
                    ACCEPTOR_NT_LENGTH, this.seqlen));
        }
    }

    @Override
    void writePositionRuler(Writer writer, int starty) throws IOException {
        int xpos = SVG_STARTX;
        final int X_NUDGE = 2;
        for (int i = 0; i < seqlen; i++) {
            int j = i - 24;
            j = j <= 0 ? j - 1 : j; // we do not have a zeroth position in this display!
            if (j == 1) xpos += X_NUDGE;
            if (j == -25 || j == -20 || j == -15 || j > -10) {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", xpos, starty));
                writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n", j));
            } else {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", xpos + X_NUDGE, starty));
                writer.write("<text x=\"0\" y=\"0\" fill=\"black\">.</text>\n");
            }
            writer.write("</g>");
            xpos += LOWER_CASE_BASE_INCREMENT;
        }
        // Write a vertical line between intron and exon
        xpos = SVG_STARTX + 25 * LOWER_CASE_BASE_INCREMENT;
        int Y1 = starty - 8;
        int Y2 = starty + 4;
        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n", xpos, Y1, xpos, Y2, RED));
    }

    @Override
    void writeOffsetPositionRuler(Writer writer, int starty) throws IOException {
        int xpos = SVG_STARTX;
        final int X_NUDGE = 2;
        for (int i = 0; i < seqlen; i++) {
            int j = i - 24 + offset;
            j = j<=0 ? j-1 : j; // we do not have a zeroth position in this display!
            if (j==1) xpos += X_NUDGE;
            if (i%5 == 0) {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", xpos, starty));
                writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n", j));
                writer.write("</g>");
            }
            int ypos = starty + 4;
            writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", xpos + X_NUDGE, ypos));
            writer.write("<text x=\"0\" y=\"0\" fill=\"black\">.</text>\n");
            writer.write("</g>");
            xpos += LOWER_CASE_BASE_INCREMENT;
        }
        // Write a vertical line between intron and exon
        xpos = SVG_STARTX + 25 * LOWER_CASE_BASE_INCREMENT;
        int Y1 = starty - 8;
        int Y2 = starty + 4;
        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n", xpos, Y1, xpos, Y2, RED));
    }

}
