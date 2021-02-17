package org.monarchinitiative.vmvt.core.svg.logo;


import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;
/**
 * This class writes a sequence logo as an SVG element.
 * @author Peter N Robinson
 */
public class SvgSequenceLogo implements SvgComponent {
    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART;
    /** Representation of the Splice donor/acceptor IC matrix. */
    protected final DoubleMatrix splicesite;

    /**
     * Create an Svg Logo for the donor or acceptor with representation of reference sequence and alt bases
     * This constructor should be used to create composite SVGs with multiple subelements, whereby
     * the LOGO is started at y=startY
     * @param site Representation of the splice site (height matrix)
     * @param w width of the SVG canvas
     */
    public SvgSequenceLogo(DoubleMatrix site, int w) {
        splicesite = site;
        this.XSTART = SvgConstants.Dimensions.SVG_STARTX;
    }




    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     */
    protected void writeLogoBaseColumn(Writer writer, int x, int y, int pos) throws IOException {
        Map<String, Double> sortedIcMap = this.splicesite.getIcValuesColumn(pos);
        double ypos = (double)y - SvgConstants.Fonts.LOGO_COLUMN_HEIGHT;
        for (Map.Entry<String, Double> entry : sortedIcMap.entrySet()) {
            String nt = entry.getKey();
            double ic = entry.getValue();
            String color = getBaseColorFromChar(nt);
            writer.write(String.format("<g transform='translate(%d,%f) scale(1,%f)'>\n",x,ypos,ic)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
            // The total ic should be 2.0
            // increment the Y value back up
            ypos -= (ic/ SvgConstants.Fonts.VERTICAL_SCALING_FACTOR) * SvgConstants.Fonts.LOGO_COLUMN_HEIGHT;
        }
    }

    /**
     * Write the logo itself to be a component of another SVG, i.e., this method
     * does not write the header/footer. It is called by the vmvt-combo methods
     * @param swriter a file handle (String writer)
     * @throws IOException if we cannot write the logo
     */
    @Override
    public void write(Writer swriter, int starty) throws IOException {
        int X = this.XSTART;
        int seqlen = this.splicesite.getMotifLength();
        int ypos = starty + SvgConstants.Dimensions.SVG_LOGO_STARTY;
        for (int i=0; i<seqlen; i++) {
            writeLogoBaseColumn(swriter, X, ypos, i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }


    @Override
    public int height() {
        return SVG_LOGO_HEIGHT;
    }



    public static SvgSequenceLogo donor(DoubleMatrix donorHeight) {
        return new SvgSequenceLogo(donorHeight, SVG_DONOR_WIDTH);
    }

    public static SvgSequenceLogo acceptor(DoubleMatrix acceptorHeight) {
        return new SvgSequenceLogo(acceptorHeight, SVG_ACCEPTOR_WIDTH);
    }
}
