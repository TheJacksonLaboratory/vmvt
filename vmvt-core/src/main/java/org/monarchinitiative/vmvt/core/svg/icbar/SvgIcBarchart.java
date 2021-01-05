package org.monarchinitiative.vmvt.core.svg.icbar;

import org.monarchinitiative.vmvt.core.SvgInitializer;
import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

import org.monarchinitiative.vmvt.core.svg.SvgComponent;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;
import org.monarchinitiative.vmvt.core.svg.SvgHeaderFooter;
import org.monarchinitiative.vmvt.core.svg.SvgWriter;
import org.monarchinitiative.vmvt.core.svg.ruler.AcceptorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.DonorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.SvgSequenceRuler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Colors.NEARLYBLACK;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;


public class SvgIcBarchart implements SvgInitializer, SvgComponent {


    private final int midbarYpos = 150;

    private final String reference;
    private final String alternate;
    private final int width;
    private final int seqlen;
    /** A coding of the String representing the reference sequence {@link #reference} using A=0,C=1,G=2,T=3. */
    private final int [] refidx;
    /** A coding of the String representing the alternate sequence {@link #alternate} using A=0,C=1,G=2,T=3. */
    private final int [] altidx;
    private final DoubleMatrix splicesite;
    private final SvgConstants.MotifType mtype;

    private final int ICBOX_WIDTH = LOWER_CASE_BASE_INCREMENT - 2;
    private final int Y_JUMP = 2; // jump across the 'y axis' for IC +/1 zero


    private SvgIcBarchart(String ref, String alt, int width, DoubleMatrix splicesite, SvgConstants.MotifType type){
        this.reference = ref;
        this.alternate = alt;
        this.width = width;
        this.splicesite = splicesite;
        this.refidx = sequenceIndex(ref);
        this.altidx = sequenceIndex(alt);
        this.seqlen = sequenceLength(ref, alt);
        this.mtype = type;
    }


    public static SvgIcBarchart donorBarChart(String ref, String alt, DoubleMatrix splicesite) {
        return new SvgIcBarchart(ref, alt, SVG_DONOR_WIDTH, splicesite, SvgConstants.MotifType.DONOR);
    }

    public static SvgIcBarchart acceptorBarChart(String ref, String alt, DoubleMatrix splicesite) {
        return new SvgIcBarchart(ref, alt, SVG_ACCEPTOR_WIDTH, splicesite, SvgConstants.MotifType.ACCEPTOR);
    }

    protected void writeRefAltSeparation(Writer writer) throws IOException {
        double endX = (0.6+this.seqlen) * LOWER_CASE_BASE_INCREMENT;
        String line = String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"%s\"/>\n",
                SVG_STARTX,this.midbarYpos, endX, this.midbarYpos, NEARLYBLACK);
        writer.write(line);
    }

    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     */
    private void writeRefBaseBox(Writer writer, int x, double y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        double IC = this.splicesite.get(base, pos);
        int width = LOWER_CASE_BASE_INCREMENT;
        double BOX_HEIGHT_UNIT = 10;
        double boxHeight;
        String rect;
        if (IC>0) {
            boxHeight = IC * BOX_HEIGHT_UNIT;
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\"  " +
                            "style=\"fill:%s\" />\n",
                    x, y-boxHeight, ICBOX_WIDTH, boxHeight, color);
        } else {
            boxHeight = -IC * BOX_HEIGHT_UNIT;
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\"  " +
                            "style=\"fill:%s\" />\n",
                    x, y+Y_JUMP, ICBOX_WIDTH, boxHeight, color);
        }
        writer.write(rect);
    }

    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param refbase index of the reference base
     * @param altbase index of the alternate base
     */
    private void writeRefAltDuplexBox(Writer writer, int x, double y, int refbase, int altbase, int pos) throws IOException {
        String refcolor = getBaseColor(refbase);
        String altcolor = getBaseColor(altbase);
        double ICref = this.splicesite.get(refbase, pos);
        double ICalt = this.splicesite.get(altbase, pos);
        double BOX_HEIGHT_UNIT = 10;
        double refBoxHeight = ICref * BOX_HEIGHT_UNIT;
        String rect;
        if (ICref>0) {
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\" " +
                            "style=\"stroke:%s;stroke-width:1;fill:%s;fill-opacity:0.15\" />\n",
                    x, y-refBoxHeight, ICBOX_WIDTH, refBoxHeight-1,  refcolor,refcolor);
        } else {
            refBoxHeight *= -1;
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\"  " +
                            "style=\"stroke:%s;stroke-width:1;fill:%s;fill-opacity:0.15\" />\n",
                    x, y+1+Y_JUMP, ICBOX_WIDTH, refBoxHeight-1,  refcolor,refcolor);
        }
        writer.write(rect);
        double altBoxHeight = ICalt * BOX_HEIGHT_UNIT;
        if (ICalt>0) {
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\"  " +
                            "style=\"fill:%s\"  />\n",
                    x, y-altBoxHeight, ICBOX_WIDTH, altBoxHeight, altcolor);
        } else {
            altBoxHeight *= -1;
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\"  " +
                            "style=\"fill:%s\" />\n",
                    x, y+Y_JUMP, ICBOX_WIDTH, altBoxHeight, altcolor);
        }
        writer.write(rect);
    }


    private void writeIcBars(Writer writer, int y) throws IOException {
        int X = SVG_STARTX;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] == this.altidx[i]) {
                writeRefBaseBox(writer, X, y, refidx[i], i);
            } else {
                writeRefAltDuplexBox(writer, X, y, refidx[i], altidx[i], i);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }


    @Override
    public void write(Writer writer, int starty) throws IOException {
        writeIcBars(writer, this.midbarYpos);
        SvgSequenceRuler ruler;
        if (mtype == SvgConstants.MotifType.DONOR) {
            ruler = new DonorRuler(reference, alternate);
        } else if (mtype == SvgConstants.MotifType.ACCEPTOR) {
            ruler = new AcceptorRuler(reference, alternate);
        } else {
            // should not happen
            throw new VmvtRuntimeException("Only donor/acceptor supported");
        }
        ruler.write(writer, SVG_Y_TOP_MARGIN);
        // writeRefAltSeparation(writer);
        writeIcBars(writer, this.midbarYpos);

    }

    @Override
    public int height() {
        return SVG_BARCHART_HEIGHT;
    }
}
