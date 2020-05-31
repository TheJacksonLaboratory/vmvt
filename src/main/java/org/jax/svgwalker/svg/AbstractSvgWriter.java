package org.jax.svgwalker.svg;

import org.jax.svgwalker.except.SvgwalkerRuntimeException;
import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.Writer;

public class AbstractSvgWriter {
    /** This name will appear in the comment of the SVGs we produce. */
    private final String PROGRAM_NAME = "svgwalker";
    /** The reference (wildtype) sequence of the donor or acceptor splice site. */
    protected final String ref;
    /** The alternate (mutant) sequence of the donor or acceptor splice site. */
    protected final String alt;
    /** The length in nucleotides of the splice site (9 for donor TODO for acceptor). */
    protected final int seqlen;

    /** Representation of the Splice donor/acceptor IC matrix. */
    protected final DoubleMatrix splicesite;
    /** A coding of the String representing the reference sequence {@link #ref} using A=0,C=1,G=2,T=3. */
    protected final int [] refidx;
    /** A coding of the String representing the alternate sequence {@link #ref} using A=0,C=1,G=2,T=3. */
    protected final int [] altidx;

    protected final static int A_BASE = 0;
    protected final static int C_BASE = 1;
    protected final static int G_BASE = 2;
    protected final static int T_BASE = 3;
    /** A blue color for Adenine */
    protected final static String A_COLOR = "#4dbbd5";
    /** A red color for Cytosine */
    protected final static String C_COLOR = "#e64b35";
    /** A green color for Guanine */
    protected final static String G_COLOR = "#00A087";
    /** A yellow color for Thymine */
    protected final static String T_COLOR = "#ffdf00";
    // NOTE SURE WE NEED THESE COLORS ?
    protected final static String BLUE ="#4dbbd5";
    protected final static String RED ="#e64b35";
    protected final static String BROWN="#7e6148";
    protected final static String DARKBLUE = "#3c5488";
    protected final static String VIOLET = "#8491b4";
    protected final static String ORANGE = "#ff9900";
    protected final static String BLACK = "#000000";
    protected final static String GREEN = "#00A087";
    protected final static String BRIGHT_GREEN = "#00a087";

    /** Total width of the SVG canvas. */
    protected final int WIDTH;
    /** Total height of the SVG canvas. */
    protected final int HEIGHT;
    /** Current X position on the SVG canvas. */
    protected int currentX;
    /** Current Y position on the SVG canvas. */
    protected int currentY;


    public AbstractSvgWriter(String ref, String alt, DoubleMatrix site, int w, int h) {
        this.ref = ref;
        this.alt = alt;
        if (ref.length() != alt.length()) {
            throw new SvgwalkerRuntimeException(String.format("Reference sequence (%s-len=%d) and alternate sequence (%s-len=%d) have different lengths:",
                    ref, ref.length(), alt, alt.length()));
        }
        this.seqlen = ref.length();
        this.splicesite = site;
        this.WIDTH = w;
        this.HEIGHT = h;
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
                    throw new SvgwalkerRuntimeException(String.format("Bad nucleotide in ref (%s): Only ACGT/acgt allowed!",ref));
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
                    throw new SvgwalkerRuntimeException(String.format("Bad nucleotide in alt (%s): Only ACGT/acgt allowed!", alt));
            }
        }
    }



    /** Write the header of the SVG */
    protected void writeHeader(Writer writer) throws IOException {
        writer.write("<svg width=\"" + this.WIDTH +"\" height=\""+ this.HEIGHT +"\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n");
        writer.write("<!-- Created by " + this.PROGRAM_NAME + " -->\n");
        writer.write("<style>\n" +
                "  text { font: 24px monospace; }\n" +
                "  </style>\n");
        writer.write("<g>\n");
    }

    /** Write the footer of the SVG */
    protected void writeFooter(Writer writer) throws IOException {
        writer.write("</g>\n</svg>\n");
    }

    /**
     * Get the lower case character for this base
     * @param b index (0,1,2,3)
     * @return corresponding lower case character for the base (a,c,g,t)
     */
    protected String getBaseCharUC(int b) {
        switch (b) {
            case A_BASE:
                return "A";
            case C_BASE:
                return "C";
            case G_BASE:
                return "G";
            case T_BASE:
                return "T";
            default:
                // should never happen
                throw new SvgwalkerRuntimeException("Unrecognized base: " + b);
        }
    }


    /**
     * Get the lower case character for this base
     * @param b index (0,1,2,3)
     * @return corresponding lower case character for the base (a,c,g,t)
     */
    protected String getBaseCharLC(int b) {
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
                throw new SvgwalkerRuntimeException("Unrecognized base: " + b);
        }
    }



    protected String getBaseColor(int b) {
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

    protected String getBaseColorFromChar(String b) {
        switch (b) {
            case "A":
                return A_COLOR;
            case "C":
                return C_COLOR;
            case "G":
                return G_COLOR;
            case "T":
                return T_COLOR;
            default:
                // should never happen
                throw new SvgwalkerRuntimeException("Unrecognized color: " + b);
        }
    }


    /**
     * If there is some IO Exception, return an SVG with a text that indicates the error
     * @param msg The error
     * @return An SVG element that contains the error
     */
    protected String getSvgErrorMessage(String msg) {
        return String.format("<svg width=\"200\" height=\"100\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                "<!-- Created by svgwalker -->\n" +
                "<g><text x=\"10\" y=\"10\">%s</text>\n</g>\n" +
                "</svg>\n", msg);
    }



}
