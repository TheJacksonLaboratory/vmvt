package org.monarchinitiative.vmvt.core.svg.logo;


import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.combo.AcceptorVmvtGenerator;
import org.monarchinitiative.vmvt.core.svg.combo.DonorVmvtGenerator;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * This class writes a sequence logo as an SVG element (which needs to be included in a complete SVG, which is
 * done in {@link DonorLogoGenerator}, {@link AcceptorLogoGenerator}, {@link AcceptorVmvtGenerator},
 * or {@link DonorVmvtGenerator}.
 * @author Peter N Robinson
 */
public class SvgSequenceLogo extends AbstractSvgMotifGenerator {
    /** Maximum height of the letters in the sequence logo. Needs to be adjusted together with {@link #FUDGE_FACTOR}.*/
    private final double LOGO_COLUMN_HEIGHT = 20.0;
    /** This is a magic number that places the letters in the correct vertical position. Works with {@link #LOGO_COLUMN_HEIGHT}.*/
    private final double FUDGE_FACTOR = 1.14;
    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART;
    /** Position where we will start to write things from the top of the SVG */
    protected final int YSTART;
    /** Amount to shift down after sequence logo */
    private final static int Y_SKIP = 50;

    /**
     * Create an Svg Logo for the donor or acceptor with representation of reference sequence and alt bases
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (height matrix)
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     */
    public SvgSequenceLogo(String ref, String alt, DoubleMatrix site, int w, int h, int x, int y) {
        super(ref,alt,site, w, h);
        this.XSTART = x;
        this.YSTART = y + 20; // Note that the logo is relatively high, so we want to start lower down!
   }




    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     */
    protected void writeLogoBaseColumn(Writer writer, int x, int y, int pos) throws IOException {
        Map<String, Double> sortedIcMap = this.splicesite.getIcValuesColumn(pos);
        double ypos = (double)y - LOGO_COLUMN_HEIGHT;
        for (Map.Entry<String, Double> entry : sortedIcMap.entrySet()) {
            String nt = entry.getKey();
            double ic = entry.getValue();
            String color = getBaseColorFromChar(nt);
            writer.write(String.format("<g transform='translate(%d,%f) scale(1,%f)'>\n",x,ypos,ic)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
            // The total ic should be 2.0
            // increment the Y value back up
            ypos -= (ic/FUDGE_FACTOR) * LOGO_COLUMN_HEIGHT;
        }
    }


    @Override
    public void write(Writer writer) throws IOException {
        int X = this.XSTART;
        for (int i=0; i<seqlen; i++) {
            writeLogoBaseColumn(writer, X, this.YSTART, i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }

    @Override
    public int getYincrement() {
        return Y_SKIP;
    }


}
