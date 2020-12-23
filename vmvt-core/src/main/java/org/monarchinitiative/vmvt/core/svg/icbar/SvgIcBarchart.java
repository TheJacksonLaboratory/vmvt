package org.monarchinitiative.vmvt.core.svg.icbar;

import org.monarchinitiative.vmvt.core.SvgInitializer;
import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

import org.monarchinitiative.vmvt.core.svg.SvgConstants;
import org.monarchinitiative.vmvt.core.svg.SvgHeaderFooter;
import org.monarchinitiative.vmvt.core.svg.SvgWriter;
import org.monarchinitiative.vmvt.core.svg.ruler.AcceptorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.DonorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.SvgSequenceRuler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;


public class SvgIcBarchart implements SvgInitializer, SvgHeaderFooter, SvgWriter {


    private final int midbarYpos = 150;

    private final String reference;
    private final String alternate;
    private final int width;
    private final int height;
    private final int seqlen;
    private final boolean framed;
    /** A coding of the String representing the reference sequence {@link #reference} using A=0,C=1,G=2,T=3. */
    private final int [] refidx;
    /** A coding of the String representing the alternate sequence {@link #alternate} using A=0,C=1,G=2,T=3. */
    private final int [] altidx;
    private final DoubleMatrix splicesite;
    private final SvgConstants.MotifType mtype;


    private SvgIcBarchart(String ref, String alt, int width, int height, DoubleMatrix splicesite, boolean framed, SvgConstants.MotifType type){
        this.reference = ref;
        this.alternate = alt;
        this.width = width;
        this.height = height;
        this.framed = framed;
        this.splicesite = splicesite;
        this.refidx = sequenceIndex(ref);
        this.altidx = sequenceIndex(alt);
        this.seqlen = sequenceLength(ref, alt);
        this.mtype = type;
    }


    public static SvgIcBarchart donorBarChart(String ref, String alt, DoubleMatrix splicesite, boolean framed) {
        return new SvgIcBarchart(ref, alt, SVG_DONOR_WIDTH, SVG_BARCHART_HEIGHT, splicesite, framed, SvgConstants.MotifType.DONOR);
    }

    public static SvgIcBarchart acceptorBarChart(String ref, String alt, DoubleMatrix splicesite, boolean framed) {
        return new SvgIcBarchart(ref, alt, SVG_ACCEPTOR_WIDTH, SVG_BARCHART_HEIGHT, splicesite, framed, SvgConstants.MotifType.ACCEPTOR);
    }


    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            //(Writer writer, int width, int height, String SVG_FONTS, boolean framed)
            writeHeader(swriter, this.width, this.height, this.framed);
            write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }


    protected void writeRefAltSeparation(Writer writer) throws IOException {
        int endX = (1+this.seqlen) * LOWER_CASE_BASE_INCREMENT;
        writer.write("<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">\n");
        writer.write(String.format("<path stroke-dasharray=\"2,2\" d=\"M%d %d L%d %d\"/>\n", SVG_STARTX, this.midbarYpos, endX, this.midbarYpos));
        writer.write("</g>\n");
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
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    x, y-boxHeight, width, boxHeight, color);
        } else {
            boxHeight = -IC * BOX_HEIGHT_UNIT;
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%d\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    x, y, width, boxHeight, color);
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
        double width = 0.5*LOWER_CASE_BASE_INCREMENT;
        double BOX_HEIGHT_UNIT = 10;
        double refBoxHeight = ICref * BOX_HEIGHT_UNIT;
        String rect;
        if (ICref>0) {
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    x, y-refBoxHeight, width, refBoxHeight, refcolor);
        } else {
            refBoxHeight *= -1;
            rect = String.format("<rect x=\"%d\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    x, y, width, refBoxHeight, refcolor);
        }
        writer.write(rect);
        double altBoxHeight = ICalt * BOX_HEIGHT_UNIT;
        double x2 = x + width;
        if (ICalt>0) {
            rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    x2, y-altBoxHeight, width, altBoxHeight, altcolor);
        } else {
            altBoxHeight *= -1;
            rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    x2, y, width, altBoxHeight, altcolor);
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
    public void write(Writer writer) throws IOException {
        writeRefAltSeparation(writer);
        writeIcBars(writer, this.midbarYpos);
        SvgSequenceRuler ruler;
        if (mtype == SvgConstants.MotifType.DONOR) {
            ruler = new DonorRuler(reference, alternate, false);
        } else if (mtype == SvgConstants.MotifType.ACCEPTOR) {
            ruler = new AcceptorRuler(reference, alternate, false);
        } else {
            // should not happen
            throw new VmvtRuntimeException("Only donor/acceptor supported");
        }
        ruler.write(writer);
        writeRefAltSeparation(writer);
        writeIcBars(writer, this.midbarYpos);

    }
}
