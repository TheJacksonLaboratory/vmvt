package org.jax.svgwalker.svg;


import org.jax.svgwalker.except.SvgwalkerRuntimeException;
import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * TODO -- FOR NOW DUPLICATE CODE.
 * LATER -- PROBABLY MAKE COMMON SUPER CLASS FOR THIS AND {@link SvgSequenceWalker} AFTER
 * WE UNDERSTAND NEEDS/ARCHITECTURE
 */
public abstract class SvgSequenceLogo {
    /** The reference (wildtype) sequence of the donor or acceptor splice site. */
    protected final String ref;
    /** The alternate (mutant) sequence of the donor or acceptor splice site. */
    protected final String alt;
    /** The length in nucleotides of the splice site (9 for donor TODO for acceptor). */
    protected final int seqlen;

    private final int WIDTH = 400;
    private final int HEIGHT = 200;
    /** Maximum height of the letters in the sequence logo. Needs to be adjusted together with {@link #FUDGE_FACTOR}.*/
    private final double LOGO_COLUMN_HEIGHT = 20.0;
    /** This is a magic number that places the letters in the correct vertical position. Works with {@link #LOGO_COLUMN_HEIGHT}.*/
    private final double FUDGE_FACTOR = 1.14;

    /** A blue color for Adenine */
    protected final static String A_COLOR = "#4dbbd5";
    /** A red color for Cytosine */
    protected final static String C_COLOR = "#e64b35";
    /** A green color for Guanine */
    protected final static String G_COLOR = "#00A087";
    /** A yellow color for Thymine */
    protected final static String T_COLOR = "#ffdf00";

    protected final static int LETTER_WIDTH = 8;
    /** Height of a letter before scaling */
    protected final static int LETTER_BASE_HEIGHT = 12;

    protected final static int MAX_LETTER_HEIGHT = 5 * LETTER_BASE_HEIGHT;

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

    private final int width;
    private final int height;

    /** Representation of the Splice donor/acceptor IC matrix. */
    private final DoubleMatrix splicesite;


    public SvgSequenceLogo(String ref, String alt, DoubleMatrix site) {
        splicesite = site;
        this.ref = ref;
        this.alt = alt;
        this.width = WIDTH;
        this.height = HEIGHT;
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

    /**
     * @return An SVG representation of the sequence logo with variant base or bases.
     */
    public abstract String getLogo();



    protected void writeHeader(Writer writer) throws IOException {
        writer.write("<svg width=\""+width+"\" height=\""+height+"\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n");
        writer.write("<!-- Created by svgwalker -->\n");
        writer.write("<style>\n" +
                "  text { font: 24px monospace; }\n" +
                "  </style>\n");
        writer.write("<g>\n");
    }

    protected void initXYpositions() {
        this.currentX = XSTART;
        this.currentY = YSTART;
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

    private String getBaseColorFromChar(String b) {
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
                throw new SvgwalkerRuntimeException("Unrecognized base: " + b);
        }
    }

    /**
     * Get the lower case character for this base
     * @param b index (0,1,2,3)
     * @return corresponding lower case character for the base (a,c,g,t)
     */
    private String getBaseCharUC(int b) {
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
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     */
    protected void writeLogoBaseColumn(Writer writer, int x, int y, int pos) throws IOException {
        Map<String, Double> sortedIcMap = this.splicesite.getIcValuesColumn(pos);
        double ypos = (double)y - LOGO_COLUMN_HEIGHT;
        for (Map.Entry<String, Double> entry : sortedIcMap.entrySet()) {
            String nt = entry.getKey();
            double ic = entry.getValue();
            String color = getBaseColorFromChar(nt);
            writer.write(String.format("<g transform='translate(%d,%f) scale(1,%f)'>\n",x,ypos,ic)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
            // The total ic should be 2.0
            // increment the Y value back up
            ypos -= (ic/FUDGE_FACTOR) * LOGO_COLUMN_HEIGHT;
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
        double startX = XSTART + b*LOWER_CASE_BASE_INCREMENT;
        double endX = startX + LOWER_CASE_BASE_INCREMENT;
        int startY = YSTART + LETTER_BASE_HEIGHT;
        int endY = YSTART + 2*LETTER_BASE_HEIGHT;
        writer.write(String.format("<rect x=\"%f\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" fill-opacity=\"0.1\"/>",
                startX,
                startY,
                LOWER_CASE_BASE_INCREMENT,
                LETTER_BASE_HEIGHT*3));

    }

    /**
     * Add some extra vertical space (one {@link #Y_LINE_INCREMENT}).
     */
    protected void incrementYposition() {
        this.currentY += Y_LINE_INCREMENT;
    }
    /**
     * Add some extra vertical space (one {@link #Y_LINE_INCREMENT}).
     */
    protected void incrementYposition(double factor) {
        this.currentY += (int)(factor*Y_LINE_INCREMENT);
    }

    protected void writeFooter(Writer writer) throws IOException {
        writer.write("</g>\n</svg>\n");
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


    protected void writeLogo(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;

        int Y1 = (int)(Y-LOGO_COLUMN_HEIGHT);
        int Y2 = (int)(Y-2.7*LOGO_COLUMN_HEIGHT);
//        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"red\"/>\n",X,Y1,X+300,Y1));
//        writer.write(String.format("<line x1=\"%s\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"green\"/>\n",X,Y2,X+300,Y2));
        for (int i=0; i<seqlen; i++) {
            writeLogoBaseColumn(writer, X, Y, i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Write position numbers underneath the logo.
        if (seqlen == 9) {
            int Xr = (int)(currentX + 0.7 * LOWER_CASE_BASE_INCREMENT);
            int Yr = (int)(Y-0.5*LOGO_COLUMN_HEIGHT);
            for (int i=0; i<seqlen; i++) {
                int j = i-2; // substract 3 for the 3 intronic positions
                j = j<=0 ? j-1 : j; // we do not have a zeroth position in this display!
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.25,0.25) rotate(270)'>\n",Xr,Yr)); //scale(1,%f)
                writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n",j));
                writer.write("</g>");
                Xr += LOWER_CASE_BASE_INCREMENT;
            }
        } else if (seqlen == 27) {

        } else {
            // should never happen
            throw new SvgwalkerRuntimeException("Unrecognized seqlen: " + seqlen);
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }



}
