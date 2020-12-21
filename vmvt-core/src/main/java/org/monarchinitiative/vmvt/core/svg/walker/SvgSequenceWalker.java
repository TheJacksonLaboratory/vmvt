package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Base class for writing SVG sequence walkers for Splice Acceptor or Donor sequences
 * @author Peter N Robinson
 */
public class SvgSequenceWalker extends AbstractSvgMotifGenerator {

    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART = SVG_STARTX;
    /** Position where we will start to write things from the top of the SVG */
    protected final int YSTART;

    protected int currentX;
    protected int currentY;

    private final int boxMidPointY;

    private double maxIc = Double.MIN_VALUE;

    /**
     * When we write the grey box around the position of the mutation, we start here. Note that this position
     * is different for an SVG that just contains the Walker as compared to an SVG that contains Logo+walker
     * (i.e., Trekker).
     */
    private final int TOP_Y_COORDINATE_OF_BOX;


    /**
     * Create an Svg Walker for the donor or acceptor with representation of reference sequence and alt bases
     * This constructor is used to create an SVG for JUST the walker (not a Trekker)
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (weight matrix)
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     */
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, int h, boolean framed) {
        super(ref,alt,site,w,h, framed);
        this.YSTART = SVG_WALKER_STARTY;
        this.currentX = this.XSTART;
        this.currentY =  this.YSTART;
        this.boxMidPointY = this.currentY;
        this.TOP_Y_COORDINATE_OF_BOX = 15;
    }

    /**
     * Create an Svg Walker for the donor or acceptor with representation of reference sequence and alt bases
     * This constructor is used to create an SVG for a Trekker (not just the walker)
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (weight matrix)
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     * @param ystart Used to indicate the position to start for a composite SVG
     */
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, int h, int ystart,boolean framed) {
        super(ref,alt,site,w,h, framed);
        this.YSTART = ystart;
        this.currentX = this.XSTART;
        this.currentY = this.YSTART;
        this.boxMidPointY = this.currentY;
        this.TOP_Y_COORDINATE_OF_BOX = ystart;

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
            double xpos = (double)x + (double)LOWER_CASE_BASE_INCREMENT;
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

    protected void writeRefWalker(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            writeWalkerBase(writer, X, Y, refidx[i], i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }

    protected void writeRefAltSeparation(Writer writer) throws IOException {
        writeRefAltSeparation(writer, currentY);
    }

    protected void writeRefAltSeparation(Writer writer, int startY) throws IOException {
        int endX = (1+this.seqlen) * LOWER_CASE_BASE_INCREMENT;
        writer.write("<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">\n");
        writer.write(String.format("<path stroke-dasharray=\"2,2\" d=\"M%d %d L%d %d\"/>\n", XSTART, startY, endX, startY));
        writer.write("</g>\n");
    }

    protected void writeAltWalker(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writeWalkerAltBase(writer, X, Y, altidx[i], i);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }


    /**
     * Write a grey box around the mutation, using heuristics to find the Y positions that cover
     * both the sequence logo and the sequence walker (here we use maxIc, because the height of
     * the walker bases depends on the maximum IC).
     * @param writer handle
     * @throws IOException if we cannot write the Box
     */
    private void writeBoxAroundMutationOld(Writer writer) throws IOException {
        // get location of first and last index with mutated bases
        int b = Integer.MAX_VALUE;
        int e = Integer.MIN_VALUE;
        double maxIc = Double.MIN_VALUE;
        for (int i=0; i<refidx.length; i++) {
            if (refidx[i] != altidx[i]) {
                if (i<b) b = i;
                if (i>e) e = i;
                double refIc =  Math.abs(this.splicesite.get(refidx[i] , i));
                double altIc = Math.abs(this.splicesite.get(altidx[i] , i));
                maxIc = Math.max(maxIc, Math.max(refIc, altIc));
            }
        }
        double X = this.XSTART + b*LOWER_CASE_BASE_INCREMENT;
        int boxwidth = LOWER_CASE_BASE_INCREMENT * (1+e-b);
        int boxheight =  SVG_WALKER_STARTY + (int)(VARIANT_BOX_SCALING_FACTOR*maxIc);
        writer.write(String.format("<rect x=\"%f\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" fill-opacity=\"0.1\"" +
                        " style=\"stroke-width:1; stroke:rgb(4, 12, 4);\"/>",
                X,
                this.TOP_Y_COORDINATE_OF_BOX,
                boxwidth,
                boxheight));

    }

    /**
     * Write a grey box around the mutation, using heuristics to find the Y positions that cover
     * both the sequence logo and the sequence walker (here we use maxIc, because the height of
     * the walker bases depends on the maximum IC).
     * @param writer handle
     * @throws IOException if we cannot write the Box
     */
    private void writeBoxAroundMutation(Writer writer) throws IOException {
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
        int boxStartY = this.boxMidPointY - 2*(int)Math.abs(maxPosIc)*SCALING_FACTOR;
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

    @Override
    public void write(Writer writer) throws IOException {
        writeAltWalker(writer);
        writeRefWalker(writer);
        writeRefAltSeparation(writer);
        writeBoxAroundMutation(writer);
    }


    public static SvgSequenceWalker donor(String ref, String alt, boolean framed) {
        return new SvgSequenceWalker(ref, alt, DoubleMatrix.donor(), SVG_DONOR_WIDTH,SVG_WALKER_HEIGHT, framed);
    }

    /**
     * Write a sequence writer for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     * @param donor Donor information content matrix
     */
    public static SvgSequenceWalker donor(String ref, String alt, DoubleMatrix donor, boolean framed) {
        return new SvgSequenceWalker(ref, alt, donor, SVG_DONOR_WIDTH,SVG_WALKER_HEIGHT, framed);
    }

    public static SvgSequenceWalker singleDonorWalker(String sequence, int ystart, boolean framed) {
        return SvgSequenceWalker.singleDonorWalker(sequence, DoubleMatrix.donor(), ystart, framed);
    }

    public static SvgSequenceWalker singleDonorWalker(String sequence, DoubleMatrix donor, int ystart, boolean framed) {
        return new SvgSequenceWalker(sequence, sequence, donor, SVG_DONOR_WIDTH, SVG_WALKER_HEIGHT, ystart, framed);
    }

    public static SvgSequenceWalker singleAcceptorWalker(String sequence, int ystart, boolean framed) {
       return singleAcceptorWalker(sequence, DoubleMatrix.acceptor(), ystart, framed);
    }

    public static SvgSequenceWalker singleAcceptorWalker(String sequence, DoubleMatrix acceptor, int ystart, boolean framed) {
        return new SvgSequenceWalker(sequence, sequence, acceptor, SVG_ACCEPTOR_WIDTH, SVG_WALKER_HEIGHT, ystart,framed);
    }



}
