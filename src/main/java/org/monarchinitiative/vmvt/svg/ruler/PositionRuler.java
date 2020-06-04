package org.monarchinitiative.vmvt.svg.ruler;

import org.monarchinitiative.vmvt.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.svg.AbstractSvgCoreGenerator;

import java.io.IOException;
import java.io.Writer;

/**
 * Write an SVG element for a "ruler", being the positions in the intron and exon
 * @author Peter N Robinson
 */
public class PositionRuler extends AbstractSvgCoreGenerator {
    /** Initial X position. */
    protected final int startX;
    /** Initial Y position. */
    protected final int startY;

    protected final int Y_DISTANCE_FOR_RULER = 40;
    /** True if the current element is a splice donor, false if the element is an acceptor. */
    boolean isDonor;

    public PositionRuler(String ref, String alt, int w, int h, int X, int Y) {
        super(ref,alt, w, h);
      this.startX = X;
      this.startY = Y;
      if (seqlen == 9) {
          isDonor = true;
      } else if (seqlen == 27) {
          isDonor = false;
      } else {
          throw new VmvtRuntimeException(String.format("Length was %d but must be either 9 (donor) or 27 (acceptor)",
                  seqlen));
      }
    }


    private void writeDonor(Writer writer) throws IOException {
        int Xr = this.startX;
        int Yr = this.startY;
        final int X_NUDGE = 2;
        for (int i=0; i<seqlen; i++) {
            int j = i-2; // substract 3 for the 3 intronic positions
            j = j<=0 ? j-1 : j; // we do not have a zeroth position in this display!
            if (j==1) Xr += X_NUDGE;
            writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4) '>\n",Xr, Yr));
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n",j));
            writer.write("</g>\n");
            Xr += LOWER_CASE_BASE_INCREMENT;
        }
        // Write a vertical line between intron and exon
        Xr = this.startX + 3*LOWER_CASE_BASE_INCREMENT;
        int Y1 = this.startY - 18;
        int Y2 = this.startY + 10;
        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"red\"/>\n",Xr,Y1,Xr,Y2));

    }

    private void writeAcceptor(Writer writer) throws IOException {
        int Xr = this.startX;
        int Yr = this.startY;
        final int X_NUDGE = 2;
        for (int i=0; i<seqlen; i++) {
            int j = i - 24; // substract 3 for the 3 intronic positions
            j = j <= 0 ? j - 1 : j; // we do not have a zeroth position in this display!
            if (j==1) Xr += X_NUDGE;
            if(j==-25 || j==-20 || j==-15 || j>-10) {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", Xr, Yr));
                writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n", j));
            } else {
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'>\n", Xr+X_NUDGE, Yr));
                writer.write("<text x=\"0\" y=\"0\" fill=\"black\">.</text>\n");
            }
            writer.write("</g>");
            Xr += LOWER_CASE_BASE_INCREMENT;
        }
        // Write a vertical line between intron and exon
        Xr = this.startX + 25*LOWER_CASE_BASE_INCREMENT;
        int Y1 = this.startY - 18;
        int Y2 = this.startY + 10;
        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"red\"/>\n",Xr,Y1,Xr,Y2));
    }


    @Override
    public void write(Writer writer) throws IOException {
        if (isDonor) {
            writeDonor(writer);
        } else {
            writeAcceptor(writer);
        }
    }



    @Override
    public int getYincrement() {
        return Y_DISTANCE_FOR_RULER;
    }
}
