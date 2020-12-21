package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.SvgColors;

import java.io.IOException;
import java.io.Writer;

public class DonorRuler  extends SvgSequenceRuler {

    public DonorRuler(String ref, String alt, boolean framed) {
        super(SVG_DONOR_WIDTH, SVG_RULER_HEIGHT, ref, alt, framed);
        if (this.seqlen != DONOR_NT_LENGTH) {
            throw new VmvtRuntimeException(String.format("Sequence length must be %d for donor but was %d",
                    DONOR_NT_LENGTH, this.seqlen));
        }
    }



    @Override
    void writePositionRuler(Writer writer) throws IOException {
        int Xr = this.startX;
        final int X_NUDGE = 2;
        for (int i=0; i<seqlen; i++) {
            int j = i-2; // substract 3 for the 3 intronic positions
            j = j<=0 ? j-1 : j; // we do not have a zeroth position in this display!
            if (j==1) Xr += X_NUDGE;
            writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4) '>\n",Xr, this.startY));
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n",j));
            writer.write("</g>\n");
            Xr += LOWER_CASE_BASE_INCREMENT;
        }
        // Write a vertical line between intron and exon
        Xr = this.startX + 3*LOWER_CASE_BASE_INCREMENT;
        int Y1 = this.startY - 18;
        int Y2 = this.startY + 10;
        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n",Xr,Y1,Xr,Y2, SvgColors.RED));
    }
}
