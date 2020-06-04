package org.monarchinitiative.vmvt.svg;

import org.monarchinitiative.vmvt.except.VmvtRuntimeException;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractSvgCoreWriter {

    /** The reference (wildtype) sequence of the donor or acceptor splice site. */
    protected final String ref;
    /** The alternate (mutant) sequence of the donor or acceptor splice site. */
    protected final String alt;
    /** A coding of the String representing the reference sequence {@link #ref} using A=0,C=1,G=2,T=3. */
    protected final int [] refidx;
    /** A coding of the String representing the alternate sequence {@link #ref} using A=0,C=1,G=2,T=3. */
    protected final int [] altidx;
    /** The length in nucleotides of the splice site (9 for donor TODO for acceptor). */
    protected final int seqlen;
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

    /** Amount of horizontal space to be taken up by one base character. */
    protected final int LOWER_CASE_BASE_INCREMENT = LETTER_WIDTH + 5;

    protected final double HALF_A_BASE = (double)LOWER_CASE_BASE_INCREMENT/2.0;
    /** Amount to shift down between ref and alt sequence lines */
    protected final int Y_LINE_INCREMENT = 20;

    protected final static int LETTER_WIDTH = 10;
    /** Height of a letter before scaling */
    protected final static int LETTER_BASE_HEIGHT = 12;
    /** Width to be used for the current SVG element we will write. */
    protected final int width;
    /** Height to be used for the current SVG element we will write. */
    protected final int height;
    /** Current X position on the SVG canvas. */
    protected int currentX;
    /** Current Y position on the SVG canvas. */
    protected int currentY;

    public AbstractSvgCoreWriter(String ref, String alt, int w, int h) {
        this.ref = ref;
        this.alt = alt;
        if (ref.length() != alt.length()) {
            throw new VmvtRuntimeException(String.format("Reference sequence (%s-len=%d) and alternate sequence (%s-len=%d) have different lengths:",
                    ref, ref.length(), alt, alt.length()));
        }
        this.seqlen = ref.length();
        this.width = w;
        this.height = h;
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
                    throw new VmvtRuntimeException(String.format("Bad nucleotide in ref (%s): Only ACGT/acgt allowed!",ref));
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
                    throw new VmvtRuntimeException(String.format("Bad nucleotide in alt (%s): Only ACGT/acgt allowed!", alt));
            }
        }
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
                throw new VmvtRuntimeException("Unrecognized base: " + b);
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
                throw new VmvtRuntimeException("Unrecognized base: " + b);
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
                throw new VmvtRuntimeException("Unrecognized color: " + b);
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
                throw new VmvtRuntimeException("Unrecognized color: " + b);
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

    public abstract void write(Writer writer) throws IOException;

    public abstract int getYincrement();


}
