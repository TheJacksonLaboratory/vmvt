package org.monarchinitiative.vmvt.core.svg.icbar;

import org.monarchinitiative.vmvt.core.svg.SvgInitializer;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.SvgColors;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;

public class DeltaRiBox implements SvgInitializer, SvgComponent {

    private final double refR_i;
    private final double altR_i;
    private final int middle;

    public DeltaRiBox(String ref, String alt, DoubleMatrix splicesite, int width) {
        this.refR_i = splicesite.getIndividualSequenceInformation(ref);
        this.altR_i = splicesite.getIndividualSequenceInformation(alt);
        this.middle = width/2 + SVG_STARTX/2;
    }


    /**
     * Write text such as Ri :7.00 -> -1.96 to show the effect of a mutation
     * on the individual information content of a splice sequence.
     * @param writer file handle
     * @param y vertical start position.
     * @throws IOException if we cannot write to the handle
     */
    private void writeRiChange(Writer writer, int y) throws IOException {
        int blueBoxStart = middle - (int)(0.5*BLUE_BOX_WIDTH);
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


    @Override
    public void write(Writer writer, int starty) throws IOException {
        writeRiChange(writer, starty);
    }

    @Override
    public int height() {
        return SVG_RI_BOX_HEIGHT;
    }

    public static DeltaRiBox donor(String ref, String alt, DoubleMatrix donor) {
        return new DeltaRiBox(ref, alt, donor, SVG_DONOR_WIDTH);
    }


    public static DeltaRiBox acceptor(String ref, String alt, DoubleMatrix acceptor) {
        return new DeltaRiBox(ref, alt, acceptor, SVG_ACCEPTOR_WIDTH);
    }
}
