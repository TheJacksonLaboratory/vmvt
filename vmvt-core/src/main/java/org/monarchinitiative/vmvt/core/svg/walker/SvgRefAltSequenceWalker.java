package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.svg.SvgInitializer;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;


/**
 * Base class for writing SVG sequence walkers for Splice Acceptor or Donor sequences.
 * In contrast to the original implementation of sequence walkers, this implementation
 * overlaps the refernece and alternate sequences, showing divergent bases in a grey box.
 * See also {@link SvgSingleSequenceWalker} for an implementation that is similar to the
 * original walkers.
 * @author Peter N Robinson
 */
public class SvgRefAltSequenceWalker implements SvgComponent, SvgInitializer {

    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART = SVG_STARTX;
    /** Maximum information content height of any base of the sequences. */
    private double maxIc = Double.MIN_VALUE;

    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;
    /** A coding of the String representing the reference sequence {@link #reference} using A=0,C=1,G=2,T=3. */
    private final int [] refidx;
    /** A coding of the String representing the alternate sequence {@link #alternate} using A=0,C=1,G=2,T=3. */
    private final int [] altidx;
    private final int seqlen;

    private final int width;

    private final int componentHeight;

    /**
     * Create an Svg Walker for the donor or acceptor with representation of reference sequence and alt bases
     * This constructor is used to create an SVG for a Trekker (not just the walker)
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (weight matrix)
     * @param w width of the SVG canvas
     */
    public SvgRefAltSequenceWalker(String ref, String alt, DoubleMatrix site, int w) {
        this.width = w;
        this.splicesite = site;
        this.refidx = sequenceIndex(ref);
        this.altidx = sequenceIndex(alt);
        this.seqlen = sequenceLength(ref, alt);
        this.reference = ref;
        this.alternate = alt;
        this.componentHeight = SvgConstants.Dimensions.SVG_WALKER_HEIGHT;
    }

    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     */
    protected void writeWalkerBase(Writer writer, int x, int y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        double IC = this.splicesite.get(base, pos);
        if (IC>0) {
            writer.write(String.format("<g transform='translate(%d,%d) scale(1,%f)'>\n",x,y,IC)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
        } else {
            double xpos = (double)x + (double)  LOWER_CASE_BASE_INCREMENT;
            int ypos = y+1;
            writer.write(String.format("<g transform='translate(%f,%d)  scale(1,%f)  rotate(180)'>\n",xpos,ypos, Math.abs(IC))); //
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>\n");
        }
    }


    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     */
    protected void writeWalkerAltBase(Writer writer, int x, int y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        double IC = this.splicesite.get(base, pos);
        if (Math.abs(IC) > maxIc) {
            maxIc = Math.abs(IC); // keep track of largest IC in order to calculate height of grey box
        }

        if (IC>0) {
            writer.write(String.format("<g transform='translate(%d,%d) scale(1,%f)'>\n",x,y,IC)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
        } else {
            double xpos = (double)x + (double)LOWER_CASE_BASE_INCREMENT;
            int ypos = y+1;
            writer.write(String.format("<g transform='translate(%f,%d)  scale(1,%f)  rotate(180)'>\n",xpos,ypos, Math.abs(IC))); //
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\" opacity=\"0.8\" style=\"text-shadow: 1px 1px #000000;\">%s</text>\n",color,nt));
            writer.write("</g>\n");
        }
    }

    protected void writeRefWalker(Writer writer, int ypos) throws IOException {
        int X = SVG_STARTX;
        for (int i=0; i<seqlen; i++) {
            writeWalkerBase(writer, X, ypos, refidx[i], i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }

    protected void writeAltWalker(Writer writer, int ypos) throws IOException {
        int X = SVG_STARTX;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writeWalkerAltBase(writer, X, ypos, altidx[i], i);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
    }

    protected void writeRefAltSeparation(Writer writer, int startY) throws IOException {
        int endX = (1+this.seqlen) * LOWER_CASE_BASE_INCREMENT;
        writer.write("<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">\n");
        writer.write(String.format("<path stroke-dasharray=\"2,2\" d=\"M%d %d L%d %d\"/>\n", XSTART, startY, endX, startY));
        writer.write("</g>\n");
    }

    /**
     * Write a grey box around the mutation, using heuristics to find the Y positions that cover
     * both the sequence logo and the sequence walker (here we use maxIc, because the height of
     * the walker bases depends on the maximum IC).
     * @param writer handle
     * @throws IOException if we cannot write the Box
     */
    private void writeBoxAroundMutation(Writer writer, int ypos) throws IOException {
        // get location of first and last index with mutated bases
        int b = Integer.MAX_VALUE;
        int e = Integer.MIN_VALUE;
        double maxPosIc = Double.MIN_VALUE;
        double minPosIc = Double.MAX_VALUE;
        double maxIc = Double.MIN_VALUE;
        for (int i=0; i<refidx.length; i++) {
            if (refidx[i] != altidx[i]) {
                if (i<b) b = i;
                if (i>e) e = i;
                double refIc =  Math.abs(this.splicesite.get(refidx[i] , i));
                double altIc = Math.abs(this.splicesite.get(altidx[i] , i));
                maxIc = Math.max(maxIc, Math.max(refIc, altIc));
                refIc =  this.splicesite.get(refidx[i] , i);
                if (refIc > 0 && refIc > maxPosIc) {
                    maxPosIc = refIc;
                } else if (refIc < 0 && refIc < minPosIc) {
                    minPosIc = refIc;
                }
                altIc = this.splicesite.get(altidx[i] , i);
                if (altIc > 0 && altIc > maxPosIc) {
                    maxPosIc = altIc;
                } else if (altIc < 0 && altIc < minPosIc) {
                    minPosIc = altIc;
                }
            }
        }
        double icRange = maxPosIc - minPosIc;
        int SCALING_FACTOR = 12;
        int boxStartY = ypos - 2*(int)Math.abs(maxPosIc)*SCALING_FACTOR;
        double X = this.XSTART + b*LOWER_CASE_BASE_INCREMENT;
        int boxwidth = LOWER_CASE_BASE_INCREMENT * (1+e-b);
        int boxheight =   (int)(SCALING_FACTOR*icRange);

        writer.write(String.format("<rect x=\"%f\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" fill-opacity=\"0.1\"" +
                        " style=\"stroke-width:1; stroke:rgb(4, 12, 4);\"/>",
                X,
                boxStartY,
                boxwidth,
                boxheight));
    }

    @Override
    public void write(Writer writer, int ypos) throws IOException {
        ypos += 35;
        writeAltWalker(writer, ypos);
        writeRefWalker(writer, ypos);
        writeRefAltSeparation(writer, ypos);
        writeBoxAroundMutation(writer, ypos);
    }



    public static SvgRefAltSequenceWalker singleDonorWalker(String sequence) {
        return SvgRefAltSequenceWalker.singleDonorWalker(sequence, DoubleMatrix.donor());
    }

    public static SvgRefAltSequenceWalker singleDonorWalker(String sequence, DoubleMatrix donor) {
        return new SvgRefAltSequenceWalker(sequence, sequence, donor, SVG_DONOR_WIDTH);
    }

    public static SvgRefAltSequenceWalker donorWalker(String reference, String alternate, DoubleMatrix donor) {
        return new SvgRefAltSequenceWalker(reference, alternate, donor, SVG_DONOR_WIDTH);
    }


    public static SvgRefAltSequenceWalker acceptorWalker(String reference, String alternate) {
        return new SvgRefAltSequenceWalker(reference, alternate, DoubleMatrix.acceptor(), SVG_ACCEPTOR_WIDTH);
    }

    public static SvgRefAltSequenceWalker acceptorWalker(String reference, String alternate, DoubleMatrix acceptor) {
        return new SvgRefAltSequenceWalker(reference, alternate, acceptor, SVG_ACCEPTOR_WIDTH);
    }


    public static SvgRefAltSequenceWalker singleAcceptorWalker(String sequence) {
       return singleAcceptorWalker(sequence, DoubleMatrix.acceptor());
    }

    public static SvgRefAltSequenceWalker singleAcceptorWalker(String sequence, DoubleMatrix acceptor) {
        return new SvgRefAltSequenceWalker(sequence, sequence, acceptor, SVG_ACCEPTOR_WIDTH);
    }


    @Override
    public int height() {
        return this.componentHeight;
    }
}
