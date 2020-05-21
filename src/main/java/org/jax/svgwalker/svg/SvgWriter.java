package org.jax.svgwalker.svg;

import org.jax.svgwalker.except.SvgwalkerRuntimeException;
import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Base class for writing SVGs of Splice Acceptor or Donor sequences
 */
public abstract class SvgWriter {

    protected final StringWriter swriter;
    protected final String ref;
    protected final String alt;
    protected final int seqlen;

    protected final static String BLUE ="#4dbbd5";
    protected final static String RED ="#e64b35";
    protected final static String BROWN="#7e6148";
    protected final static String DARKBLUE = "#3c5488";
    protected final static String VIOLET = "#8491b4";
    protected final static String ORANGE = "#ff9900";
    protected final static String BLACK = "#000000";
    protected final static String GREEN = "#00A087";
    protected final static String BRIGHT_GREEN = "#00a087";
    /** A blue color for Adenine */
    protected final static String A_COLOR = "#4dbbd5";
    /** A red color for Cytosine */
    protected final static String C_COLOR = "#e64b35";
    /** A green color for Guanine */
    protected final static String G_COLOR = "#00A087";
    /** A yellow color for Thymine */
    protected final static String T_COLOR = "#ffdf00";


    protected final static int A_BASE = 0;
    protected final static int C_BASE = 1;
    protected final static int G_BASE = 2;
    protected final static int T_BASE = 3;
    /**
     * A coding of the String representing the reference sequence {@link #ref} using A=0,C=1,G=2,T=3
     */
    protected final int [] refidx;
    /**
     * A coding of the String representing the alternate sequence {@link #ref} using A=0,C=1,G=2,T=3
     */
    protected final int [] altidx;

    /** Position where we will start to write things from the left side of the SVG. */
    private final int XSTART = 10;
    /** Position where we will start to write things from the top of the SVG */
    private final int YSTART = 10;
    /** Amount of horizontal space to be taken up by one base character. */
    private final int LOWER_CASE_BASE_INCREMENT = 15;
    /** Amount to shift down between ref and alt sequence lines */
    private final int Y_LINE_INCREMENT = 20;

    protected int currentX;
    protected int currentY;

    /** Representation of the Splice donor/acceptor IC matrix. */
    private final DoubleMatrix splicesite;

    public SvgWriter(String reference, String alternate, DoubleMatrix site) {
        this.swriter = new StringWriter();
        this.ref = reference;
        this.alt = alternate;
        this.splicesite = site;
        if (ref.length() != alt.length()) {
            throw new SvgwalkerRuntimeException(String.format("ref (%s) and alt (%s) have different lengths!", ref, alt));
        }
        this.seqlen = ref.length();
        this.refidx = new int[seqlen];
        this.altidx = new int[seqlen];
        for (int i=0; i<seqlen; i++) {
            switch (ref.charAt(i)) {
                case 'a':
                case 'A':
                    refidx[i] = A_BASE;
                    break;
                case 'c':
                case 'C':
                    refidx[i] = C_BASE;
                    break;
                case 'g':
                case 'G':
                    refidx[i] = G_BASE;
                    break;
                case 't':
                case 'T':
                    refidx[i] = T_BASE;
                    break;
                default:
                    throw new SvgwalkerRuntimeException(String.format("Bad nucleotide in ref (%s): Only ACGT/acgt allowed!"));
            }
        }
        for (int i=0; i<seqlen; i++) {
            switch (alt.charAt(i)) {
                case 'a':
                case 'A':
                    altidx[i] = A_BASE;
                    break;
                case 'c':
                case 'C':
                    altidx[i] = C_BASE;
                    break;
                case 'g':
                case 'G':
                    altidx[i] = G_BASE;
                    break;
                case 't':
                case 'T':
                    altidx[i] = T_BASE;
                    break;
                default:
                    throw new SvgwalkerRuntimeException(String.format("Bad nucleotide in alt (%s): Only ACGT/acgt allowed!"));
            }
        }
    }

    abstract public String getSvg();

    protected void writeHeader(Writer writer, int width, int height) throws IOException {
        writer.write("<svg width=\""+width+"\" height=\""+height+"\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n");
        writer.write("<!-- Created by svgwalker -->\n");
        writer.write("<g>\n");
    }

    protected void writeFooter(Writer writer) throws IOException {
        writer.write("</g>\n</svg>\n");
    }

    private String getBaseColor(int b) {
        switch (b) {
            case A_BASE:
                return A_COLOR;
            case C_BASE:
                return C_COLOR;
            case G_BASE:
                return G_COLOR;
            case T_BASE:
                return T_COLOR;
            default:
                // should never happen
                throw new SvgwalkerRuntimeException("Unrecognized color: " + b);
        }
    }

    /**
     * Get the lower case character for this base
     * @param b index (0,1,2,3)
     * @return corresponding lower case character for the base (a,c,g,t)
     */
    private String getBaseCharLC(int b) {
        switch (b) {
            case A_BASE:
                return "a";
            case C_BASE:
                return "c";
            case G_BASE:
                return "g";
            case T_BASE:
                return "t";
            default:
                // should never happen
                throw new SvgwalkerRuntimeException("Unrecognized color: " + b);
        }
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

    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     * @throws IOException if we cannot write
     */
    protected void writeWalkerBase(Writer writer, int x, int y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        double IC = this.splicesite.get(base, pos);
        String basestring = String.format("<text x=\"%d\" y=\"%d\" fill=\"%s\">%s</text>",x,y,color,nt);
        String viewbox = String.format("<g viewbox=\"0 0 20 100\" preserveAspectRatio=\"none\">%s</g>", basestring);
        writer.write(viewbox + "\n");
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
        currentY = Y + Y_LINE_INCREMENT;
    }
}
