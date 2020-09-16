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

public class DonorTrekkerWithRiGenerator extends AbstractSvgMotifGenerator{
    /** Add some extra width to write the R_i of the sequences. */
    private static final int EXTRA_X_FOR_RI = 10;

    /** The height of a sequence ruler. */
    protected final static int SVG_RULER_HEIGHT = 110;
    private final static int SVG_DONOR_LOGO_START =  SVG_RULER_HEIGHT - 10;
    private final static int SVG_WALKER_START_Y = 20 + SVG_DONOR_LOGO_START + SVG_LOGO_HEIGHT + 50;
    /** Total height of the drawing canvas. */
    private final static int  SVG_CANONICAL_CRYPTIC_HEIGHT = 150 + SVG_WALKER_START_Y;
    private static final int BLUE_BOX_WIDTH = 150;
    private static final int BLUE_BOX_HEIGHT = 40;
    /** Position to start the blue box so that it is in the middle. */
    private final int blueBoxStart;

    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;


    public DonorTrekkerWithRiGenerator(int w, int h, String ref, String alt, DoubleMatrix site) {
        super(ref,alt,w+EXTRA_X_FOR_RI, h);
        this.splicesite = site;
        this.spliceHeightMatrix = DoubleMatrix.donorHeightMatrix();
        int middle = w/2 + SVG_STARTX/2;
        blueBoxStart = middle - (int)(0.5*BLUE_BOX_WIDTH);
    }




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


    private void writeRiChange(Writer writer, double refR_i, double altR_i, int y) throws IOException {
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
        //  String eseText = String.format("<text x='50%%' y='57' text-anchor='middle'>ΔESE: %.2f</text>", deltaESE);
        writer.write(RiString);
//        String categoryString = String.format("<text x=\"%d\" y=\"%d\" class=\"t20\">\n" +category +"</text>\n",
//                startx,y+y_increment);
//        writer.write(categoryString);
    }



    @Override
    public void write(Writer swriter) throws IOException {
        DonorRuler ruler = new DonorRuler(this.ref, this.alt);
        ruler.write(swriter);
        SvgSequenceLogo donorLogo = new DonorLogoGenerator(this.spliceHeightMatrix, SVG_DONOR_LOGO_START);
        donorLogo.write(swriter);
        System.out.println("SVG_TREKKER_WALKER_START_Y: " + SVG_WALKER_START_Y);
        AbstractSvgMotifGenerator donorWalker =
                new SvgSequenceWalker(this.ref, this.alt, this.splicesite, WIDTH, HEIGHT, SVG_WALKER_START_Y);
        donorWalker.write(swriter);
        // now show the Ri values
        double refR_i = splicesite.getIndividualSequenceInformation(this.ref);
        double altR_i = splicesite.getIndividualSequenceInformation(this.alt);
        writeRiChange(swriter, refR_i, altR_i, SVG_WALKER_START_Y+120);

    }

    public static AbstractSvgMotifGenerator donor(String ref, String alt, DoubleMatrix donor) {
        return new DonorTrekkerWithRiGenerator(SVG_DONOR_WIDTH, SVG_CANONICAL_CRYPTIC_HEIGHT, ref, alt,donor);
    }
}
