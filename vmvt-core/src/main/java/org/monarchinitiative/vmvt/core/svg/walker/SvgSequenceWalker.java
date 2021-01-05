package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.SvgInitializer;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgColors;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;


/**
 * Base class for writing SVG sequence walkers for Splice Acceptor or Donor sequences
 * @author Peter N Robinson
 */
public class SvgSequenceWalker implements SvgComponent, SvgInitializer {

    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART = SVG_STARTX;
    /** If true, write Ri change associated with the variant */
    private final boolean writeRi;
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
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, boolean writeRi) {
        this.width = w;
        this.splicesite = site;
        this.refidx = sequenceIndex(ref);
        this.altidx = sequenceIndex(alt);
        this.seqlen = sequenceLength(ref, alt);
        this.reference = ref;
        this.alternate = alt;
        this.writeRi = writeRi;
        if (writeRi) {
            this.componentHeight = SvgConstants.Dimensions.SVG_TREKKER_WITH_RI_HEIGHT;
        } else {
            this.componentHeight = SvgConstants.Dimensions.SVG_WALKER_HEIGHT;
        }
    }

    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w) {
        this(ref, alt, site, w, false);
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


//    protected void writeRefAltSeparation(Writer writer) throws IOException {
//        writeRefAltSeparation(writer, currentY);
//    }

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
        if (this.writeRi) {
            writeRiChange(writer, ypos);
        }
    }


    /**
     * Write text such as Ri :7.00 -> -1.96 to show the effect of a mutation
     * on the individual information content of a splice sequence.
     * @param writer file handle
     * @param y vertical start position.
     * @throws IOException if we cannot write to the handle
     */
    protected void writeRiChange(Writer writer, int y) throws IOException {
        double refR_i = splicesite.getIndividualSequenceInformation(this.reference);
        double altR_i = splicesite.getIndividualSequenceInformation(this.alternate);
        int middle = this.width/2 + SVG_STARTX/2;
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

    public static SvgSequenceWalker singleDonorWalker(String sequence) {
        return SvgSequenceWalker.singleDonorWalker(sequence, DoubleMatrix.donor());
    }

    public static SvgSequenceWalker singleDonorWalker(String sequence, DoubleMatrix donor) {
        return new SvgSequenceWalker(sequence, sequence, donor, SVG_DONOR_WIDTH);
    }

    public static SvgSequenceWalker donorWalker(String reference, String alternate, DoubleMatrix donor) {
        return new SvgSequenceWalker(reference, alternate, donor, SVG_DONOR_WIDTH);
    }

    public static SvgSequenceWalker donorWalkerWithRi(String reference, String alternate, DoubleMatrix donor) {
        boolean showRi = true;
        return new SvgSequenceWalker(reference, alternate, DoubleMatrix.donor(), SVG_DONOR_WIDTH, showRi);
    }

    public static SvgSequenceWalker acceptorWalker(String reference, String alternate) {
        return new SvgSequenceWalker(reference, alternate, DoubleMatrix.acceptor(), SVG_ACCEPTOR_WIDTH);
    }

    public static SvgSequenceWalker acceptorWalker(String reference, String alternate, DoubleMatrix acceptor) {
        return new SvgSequenceWalker(reference, alternate, acceptor, SVG_ACCEPTOR_WIDTH);
    }

    public static SvgSequenceWalker acceptorWalkerWithRi(String reference, String alternate, DoubleMatrix acceptor) {
        boolean showRi = true;
        return new SvgSequenceWalker(reference, alternate, acceptor, SVG_ACCEPTOR_WIDTH, showRi);
    }

    public static SvgSequenceWalker singleAcceptorWalker(String sequence) {
       return singleAcceptorWalker(sequence, DoubleMatrix.acceptor());
    }

    public static SvgSequenceWalker singleAcceptorWalker(String sequence, DoubleMatrix acceptor) {
        return new SvgSequenceWalker(sequence, sequence, acceptor, SVG_ACCEPTOR_WIDTH);
    }


    @Override
    public int height() {
        return this.componentHeight;
    }
}
