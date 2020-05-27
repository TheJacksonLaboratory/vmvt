package org.jax.svgwalker.svg.walker;

import org.jax.svgwalker.except.SvgwalkerRuntimeException;
import org.jax.svgwalker.pssm.DoubleMatrix;
import org.jax.svgwalker.svg.AbstractSvgWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Base class for writing SVGs of Splice Acceptor or Donor sequences
 */
public abstract class SvgSequenceWalker extends AbstractSvgWriter {

    protected final static int LETTER_WIDTH = 10;
    /** Height of a letter before scaling */
    protected final static int LETTER_BASE_HEIGHT = 12;

    protected final static int MAX_LETTER_HEIGHT = 5 * LETTER_BASE_HEIGHT;


    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART = 10;
    /** Position where we will start to write things from the top of the SVG */
    protected final int YSTART = 60;
    /** Amount of horizontal space to be taken up by one base character. */
    private final int LOWER_CASE_BASE_INCREMENT = LETTER_WIDTH + 5;

    private final double HALF_A_BASE = (double)LOWER_CASE_BASE_INCREMENT/2.0;
    /** Amount to shift down between ref and alt sequence lines */
    private final int Y_LINE_INCREMENT = 20;

    protected int currentX;
    protected int currentY;


    /**
     * Create an Svg Walker for the donor or acceptor with representation of reference sequence and alt bases
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (weight matrix)
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     */
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, int h) {
        super(ref,alt,site,w,h);
    }

    /**
     * @return An SVG representation of the sequence walker with variant base or bases.
     */
    public abstract String getWalker();


    protected void initXYpositions() {
        this.currentX = XSTART;
        this.currentY = YSTART;
    }

    /**
     * Add some extra vertical space (one {@link #Y_LINE_INCREMENT}).
     */
    protected void incrementYposition() {
        this.currentY += Y_LINE_INCREMENT;
    }


    /**
     * Write one lower case nucleotide (a, c, g, t) to show the sequence. This goes above the actual walker logo
     * and is just a plain letter but with one of the four base colors
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     * @throws IOException if we cannot write
     */
    protected void writePlainBase(Writer writer, int x, int y, int base) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        String basestring = String.format("<text x=\"%d\" y=\"%d\" fill=\"%s\">%s</text>",x,y,color,nt);
        writer.write(basestring + "\n");
    }

    protected void writeRefPlain(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            writePlainBase(writer, X, Y, refidx[i]);
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
        currentY = Y + Y_LINE_INCREMENT;
    }

    protected void writeAltPlain(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writePlainBase(writer, X, Y, altidx[i]);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
        currentY = Y + Y_LINE_INCREMENT;
    }


    protected void writeBoxAroundMutation(Writer writer) throws IOException {
        // get location of first and last index with mutated bases
        int b = Integer.MAX_VALUE;
        int e = Integer.MIN_VALUE;
        for (int i=0; i<refidx.length; i++) {
            if (refidx[i] != altidx[i]) {
                if (i<b) b = i;
                if (i>e) e = i;
            }
        }
        double startX = b*LOWER_CASE_BASE_INCREMENT + HALF_A_BASE;
        double endX = (1+e)*LOWER_CASE_BASE_INCREMENT - HALF_A_BASE;
        int startY = YSTART - LETTER_BASE_HEIGHT;
        int endY = YSTART + LETTER_BASE_HEIGHT;
        writer.write(String.format("<rect x=\"%f\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" fill-opacity=\"0.1\"/>",
                startX,
                startY,
                LOWER_CASE_BASE_INCREMENT,
                LETTER_BASE_HEIGHT*3));

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
            writer.write(String.format("<g transform='translate(%f,%d)  scale(1,%f)  rotate(180)'>\n",x+HALF_A_BASE,y, Math.abs(IC))); //
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>\n");
        }
    }

    protected void writeRefWalker(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        int startX = X;
        for (int i=0; i<seqlen; i++) {
            writeWalkerBase(writer, X, Y, refidx[i], i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }

    protected void writeRefAltSeparation(Writer writer) throws IOException {
        //currentY += (double)Y_LINE_INCREMENT/5.0;
        int endX = this.seqlen * LOWER_CASE_BASE_INCREMENT;
        writer.write("<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">\n");
        writer.write(String.format("<path stroke-dasharray=\"2,2\" d=\"M%d %d L%d %d\"/>\n", XSTART, currentY, endX, currentY));
        writer.write("</g>\n");
    }

    protected void writeAltWalker(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writeWalkerBase(writer, X, Y, altidx[i], i);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }

}
