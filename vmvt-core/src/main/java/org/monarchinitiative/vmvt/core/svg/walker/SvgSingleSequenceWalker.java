package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;
import org.monarchinitiative.vmvt.core.svg.SvgInitializer;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.SVG_ACCEPTOR_WIDTH;

/**
 * Base class for writing SVG sequence walkers for Splice Acceptor or Donor sequences.
 *
 * @author Peter N Robinson
 */
public class SvgSingleSequenceWalker implements SvgComponent, SvgInitializer {

    /**
     * Position where we will start to write things from the left side of the SVG.
     */
    protected final int XSTART = SVG_STARTX;
    /**
     * Maximum information content height of any base of the sequences.
     */
    private double maxIc = Double.MIN_VALUE;

    private final String sequence;
    private final DoubleMatrix splicesite;
    /**
     * A coding of the String representing the reference sequence {@link #sequence} using A=0,C=1,G=2,T=3.
     */
    private final int[] refidx;

    private final int seqlen;

    /** Where to start writing the bits .*/
    private final int svgTextStart;

    private final int componentHeight;

    private final double bits;

    /**
     * Create an Svg Walker for the donor or acceptor with representation of reference sequence and alt bases
     * This constructor is used to create an SVG for a Trekker (not just the walker)
     *
     * @param seq  reference sequence
     * @param site Representation of the splice site (weight matrix)
     * @param w    width of the SVG canvas
     */
    public SvgSingleSequenceWalker(String seq,  DoubleMatrix site, int w) {
        this.svgTextStart = w;
        this.splicesite = site;
        this.refidx = sequenceIndex(seq);
        this.seqlen = seq.length();
        this.sequence = seq;
        this.bits = site.getIndividualSequenceInformation(refidx);
        this.componentHeight = SvgConstants.Dimensions.SVG_SINGLE_WALKER_HEIGHT;
    }

    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     *
     * @param writer A string writer
     * @param x      x position
     * @param y      y position
     * @param base   index of the base
     */
    protected void writeWalkerBase(Writer writer, int x, int y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        double IC = this.splicesite.get(base, pos);
        if (IC > 0) {
            writer.write(String.format("<g transform='translate(%d,%d) scale(1,%f)'>\n", x, y, IC)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n", color, nt));
            writer.write("</g>");
        } else {
            double xpos = (double) x + (double) LOWER_CASE_BASE_INCREMENT;
            int ypos = y + 1;
            writer.write(String.format("<g transform='translate(%f,%d)  scale(1,%f)  rotate(180)'>\n", xpos, ypos, Math.abs(IC))); //
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n", color, nt));
            writer.write("</g>\n");
        }
    }



    private void writeRefWalker(Writer writer, int ypos) throws IOException {
        int X = SVG_STARTX;
        for (int i = 0; i < seqlen; i++) {
            writeWalkerBase(writer, X, ypos, refidx[i], i);
            X += LETTER_WIDTH + 2;
        }
    }



    protected void writeBits(Writer writer, int ypos) throws IOException {
        writer.write(String.format("<text x=\"%d\" y=\"%d\" class=\"t14\">%.1f bits</text>\n", this.svgTextStart, ypos, this.bits));
    }



    @Override
    public void write(Writer writer, int ypos) throws IOException {
        ypos += 35;
        writeRefWalker(writer, ypos);
        writeBits(writer, ypos);
    }


    public static SvgSingleSequenceWalker singleDonorWalker(String sequence) {
        return SvgSingleSequenceWalker.singleDonorWalker(sequence, DoubleMatrix.donor());
    }

    public static SvgSingleSequenceWalker singleDonorWalker(String sequence, DoubleMatrix donor) {
        return new SvgSingleSequenceWalker(sequence, donor, SVG_DONOR_SINGLE_WALKER_WIDTH);
    }

    public static SvgSingleSequenceWalker donorWalker(String sequence, DoubleMatrix donor) {
        return new SvgSingleSequenceWalker(sequence,  donor, SVG_DONOR_SINGLE_WALKER_WIDTH);
    }


    public static SvgSingleSequenceWalker acceptorWalker(String sequence) {
        return new SvgSingleSequenceWalker(sequence, DoubleMatrix.acceptor(), SVG_DONOR_SINGLE_ACCEPTOR_WIDTH);
    }

    public static SvgSingleSequenceWalker acceptorWalker(String sequence, DoubleMatrix acceptor) {
        return new SvgSingleSequenceWalker(sequence, acceptor, SVG_DONOR_SINGLE_ACCEPTOR_WIDTH);
    }


    public static SvgSingleSequenceWalker singleAcceptorWalker(String sequence) {
        return singleAcceptorWalker(sequence, DoubleMatrix.acceptor());
    }

    public static SvgSingleSequenceWalker singleAcceptorWalker(String sequence, DoubleMatrix acceptor) {
        return new SvgSingleSequenceWalker(sequence,  acceptor, SVG_DONOR_SINGLE_ACCEPTOR_WIDTH);
    }

    @Override
    public int height() {
        return this.componentHeight;
    }
}
