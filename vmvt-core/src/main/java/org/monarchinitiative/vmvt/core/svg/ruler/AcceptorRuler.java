package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.SvgColors;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.LOWER_CASE_BASE_INCREMENT;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.SVG_ACCEPTOR_WIDTH;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.ACCEPTOR_NT_LENGTH;

public class AcceptorRuler extends SvgSequenceRuler {

    public AcceptorRuler(String ref, String alt) {
        super(SVG_ACCEPTOR_WIDTH, SVG_RULER_HEIGHT, ref, alt);
        if (this.seqlen != ACCEPTOR_NT_LENGTH) {
            throw new VmvtRuntimeException(String.format("Sequence length must be %d for donor but was %d",
                    ACCEPTOR_NT_LENGTH, this.seqlen));
        }
    }

    @Override
    void writePositionRuler(Writer writer, int starty) throws IOException {
        int Xr = this.startX;
        final int X_NUDGE = 2;
        for (int i = 0; i < seqlen; i++) {
            int j = i - 24; // substract 3 for the 3 intronic positions
            j = j <= 0 ? j - 1 : j; // we do not have a zeroth position in this display!
            if (j == 1) Xr += X_NUDGE;
            if (j == -25 || j == -20 || j == -15 || j > -10) {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", Xr, starty));
                writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n", j));
            } else {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", Xr + X_NUDGE, starty));
                writer.write("<text x=\"0\" y=\"0\" fill=\"black\">.</text>\n");
            }
            writer.write("</g>");
            Xr += LOWER_CASE_BASE_INCREMENT;
        }
        // Write a vertical line between intron and exon
        Xr = this.startX + 25 * LOWER_CASE_BASE_INCREMENT;
        int Y1 = starty - 8;
        int Y2 = starty + 4;
        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n", Xr, Y1, Xr, Y2, SvgColors.RED));
    }

    @Override
    public int height() {
        return SvgConstants.Dimensions.SVG_RULER_HEIGHT;
    }
}
