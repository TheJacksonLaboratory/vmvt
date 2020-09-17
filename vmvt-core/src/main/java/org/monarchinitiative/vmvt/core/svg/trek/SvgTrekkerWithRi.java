package org.monarchinitiative.vmvt.core.svg.trek;


import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgColors;
import org.monarchinitiative.vmvt.core.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.ruler.DonorRuler;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This class generates a Trekker graphic that additionally shows the change in Ri from ref to alt.
 * It is intended to be used to display cryptic splice mutations where there is typically a change in
 * the canonical individual sequence information (R_i) as well as the cryptic splice site (which
 * is located at a different location but can overlap the canonical site, so that the mutation
 * affects different positions of canonical and cryptic splice sites.
 *
 * In this case, wew envision that one SVG each would be created for canonical and cryptic splice sites.
 * @author Peter Robinson
 */
public abstract class SvgTrekkerWithRi extends AbstractSvgMotifGenerator {
    /** Add some extra width to write the R_i of the sequences. */
    private static final int EXTRA_X_FOR_RI = 10;
    /** The height of a sequence ruler. */
    private final static int SVG_RULER_HEIGHT = 110;
    /** Y start position of the logo, which is underneath the ruler. */
    protected final static int SVG_DONOR_LOGO_START =  SVG_RULER_HEIGHT - 10;
    /** Y start position of the walker, which is underneath the logo. */
    protected final static int SVG_WALKER_START_Y = 20 + SVG_DONOR_LOGO_START + SVG_LOGO_HEIGHT + 50;
    /** Total height of the drawing canvas. */
    private final static int SVG_TREKKER_WITH_RI_HEIGHT = 150 + SVG_WALKER_START_Y;
    /** Width of the 'blue box', which is used as a background for the Ri text. */
    private static final int BLUE_BOX_WIDTH = 150;
    private static final int BLUE_BOX_HEIGHT = 40;
    /** Position to start the blue box so that it is in the middle. */
    private final int blueBoxStart;
    /** A representation of the IC of the splice site (donor or acceptor). */
    protected final DoubleMatrix splicesite;
    /** A representation of the height matrix. */
    protected final DoubleMatrix spliceHeightMatrix;

    public SvgTrekkerWithRi(String ref, String alt, int w,  DoubleMatrix site, DoubleMatrix heightMatrix) {
        super(ref,alt,w+EXTRA_X_FOR_RI, SVG_TREKKER_WITH_RI_HEIGHT);
        splicesite = site;
        this.spliceHeightMatrix = heightMatrix;
        int middle = w/2 + SVG_STARTX/2;
        blueBoxStart = middle - (int)(0.5*BLUE_BOX_WIDTH);
    }
    @Override
    public abstract void write(Writer swriter) throws IOException;

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



    /**
     * Write text such as Ri :7.00 -> -1.96 to show the effect of a mutation
     * on the individual information content of a splice sequence.
     * @param writer file handle
     * @param refR_i individual information content of the ref sequence
     * @param altR_i individual information content of the alt sequence
     * @param y vertical start position.
     * @throws IOException if we cannot write to the handle
     */
    protected void writeRiChange(Writer writer, double refR_i, double altR_i, int y) throws IOException {
        int startx = blueBoxStart+10;
        int texty = y;
        int blueBoxFudge = 20; // move back up by this amount to be in the right place
        int x_increment = 0;

        String blueRect = String.format("<rect x=\"%d\" y=\"%d\" rx=\"3\" ry=\"3\" width=\"%d\" height=\"%d\" style=\"stroke: none; fill: %s;fill-opacity: 0.1\"></rect>",
                blueBoxStart,
                y-blueBoxFudge,
                BLUE_BOX_WIDTH,
                BLUE_BOX_HEIGHT,
                SvgColors.BLUE);
        writer.write(blueRect);

        String RiString = String.format("<text x=\"%d\" y=\"%d\" class=\"t20\">R" +
                        "<tspan dy=\"1\" font-size=\"12\">i</tspan></text>\n" +
                        "<text x=\"%d\" y=\"%d\" class=\"t14\">:%.2f &#129074; %.2f</text>\n",
                startx+x_increment,texty,startx+18+x_increment,texty,refR_i, altR_i);
        writer.write(RiString);
    }
}
