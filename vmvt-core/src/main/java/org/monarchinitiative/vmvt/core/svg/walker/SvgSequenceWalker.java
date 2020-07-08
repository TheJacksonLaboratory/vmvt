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
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, int h) {
        super(ref,alt,site,w,h);
        this.YSTART = SVG_WALKER_STARTY;
        this.currentX = this.XSTART;
        this.currentY =  this.YSTART;
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
     */
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, int h, int ystart) {
        super(ref,alt,site,w,h);
        this.YSTART = ystart;
        this.currentX = this.XSTART;
        this.currentY = this.YSTART;
        this.TOP_Y_COORDINATE_OF_BOX = 5;
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
        int endX = (1+this.seqlen) * LOWER_CASE_BASE_INCREMENT;
        writer.write("<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">\n");
        writer.write(String.format("<path stroke-dasharray=\"2,2\" d=\"M%d %d L%d %d\"/>\n", XSTART, currentY, endX, currentY));
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
    private void writeBoxAroundMutation(Writer writer) throws IOException {
        // get location of first and last index with mutated bases
        int b = Integer.MAX_VALUE;
        int e = Integer.MIN_VALUE;
        for (int i=0; i<refidx.length; i++) {
            if (refidx[i] != altidx[i]) {
                if (i<b) b = i;
                if (i>e) e = i;
            }
        }
        double X = this.XSTART + b*LOWER_CASE_BASE_INCREMENT;
        int boxwidth = LOWER_CASE_BASE_INCREMENT * (1+e-b);
        int boxheight =  YSTART + (int)(VARIANT_BOX_SCALING_FACTOR*maxIc);
        writer.write(String.format("<rect x=\"%f\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" fill-opacity=\"0.1\"" +
                        " style=\"stroke-width:1; stroke:rgb(4, 12, 4);\"/>",
                X,
                this.TOP_Y_COORDINATE_OF_BOX,
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


    public static SvgSequenceWalker donor(String ref, String alt) {
        return new SvgSequenceWalker(ref, alt, DoubleMatrix.donor(), SVG_DONOR_WIDTH,SVG_WALKER_HEIGHT);
    }

    /**
     * Write a sequence writer for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     * @param donor Donor information content matrix
     */
    public static SvgSequenceWalker donor(String ref, String alt, DoubleMatrix donor) {
        return new SvgSequenceWalker(ref, alt, donor, SVG_DONOR_WIDTH,SVG_WALKER_HEIGHT);
    }

    public static SvgSequenceWalker singleDonorWalker(String sequence, int ystart) {
        return SvgSequenceWalker.singleDonorWalker(sequence, DoubleMatrix.donor(), ystart);
    }

    public static SvgSequenceWalker singleDonorWalker(String sequence, DoubleMatrix donor, int ystart) {
        return new SvgSequenceWalker(sequence, sequence, donor, SVG_DONOR_WIDTH, SVG_WALKER_HEIGHT, ystart);
    }

    public static SvgSequenceWalker singleAcceptorWalker(String sequence, int ystart) {
       return singleAcceptorWalker(sequence, DoubleMatrix.acceptor(), ystart);
    }

    public static SvgSequenceWalker singleAcceptorWalker(String sequence, DoubleMatrix acceptor, int ystart) {
        return new SvgSequenceWalker(sequence, sequence, acceptor, SVG_ACCEPTOR_WIDTH, SVG_WALKER_HEIGHT, ystart);
    }



}
