package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;

import java.io.IOException;
import java.io.Writer;

/**
 * This is the base class for writing an SVG string of all types.
 * @author Peter Robinson
 */
public abstract class AbstractSvgGenerator {
    /** This name will appear in the comment of the SVGs we produce. */
    private final static String PROGRAM_NAME = "vmvt";

    /** Total width of the SVG canvas. */
    protected final int WIDTH;
    /** Total height of the SVG canvas. */
    protected final int HEIGHT;
    /** X Position on the SVG canvas to start drawing. */
    protected final static int SVG_STARTX = 10;
    /** The width of the SVG canvas for all Donor figures. */
    protected final static int SVG_DONOR_WIDTH = 150;
    /** The width of the SVG canvas for all Acceptor figures. */
    protected final static int SVG_ACCEPTOR_WIDTH = 420;
    /** Y Position on the SVG canvas to start drawing Sequence logos. */
    protected final static int SVG_LOGO_STARTY = 60;
    /** Height on the SVG canvas for Sequence logos. */
    protected final static int SVG_LOGO_HEIGHT = 50;

    protected final static int DONOR_NT_LENGTH = 9;
    protected final static int ACCEPTOR_NT_LENGTH = 27;
    /** Height on the SVG canvas for Sequence rulers. */
    protected final static int SVG_RULER_HEIGHT = 110;

    protected final static int SVG_RULER_STARTY = 30;

    protected final static int A_BASE = 0;
    protected final static int C_BASE = 1;
    protected final static int G_BASE = 2;
    protected final static int T_BASE = 3;
    /** A green color for Adenine */
    protected final static String A_COLOR = SvgColors.GREEN;
    /** A blue color for Cytosine */
    protected final static String C_COLOR = SvgColors.BLUE;
    /** An orange color for Guanine */
    protected final static String G_COLOR = SvgColors.ORANGE;
    /** A red color for Thymine */
    protected final static String T_COLOR = SvgColors.RED;

    protected final static int LETTER_WIDTH = 10;
    /** Amount of horizontal space to be taken up by one base character. */
    protected final int LOWER_CASE_BASE_INCREMENT = LETTER_WIDTH + 5;


    public AbstractSvgGenerator(int w, int h) {
        this.WIDTH = w;
        this.HEIGHT = h;
    }

    /** Write the header of the SVG */
    protected void writeHeader(Writer writer) throws IOException {
        writer.write("<svg width=\"" + this.WIDTH +"\" height=\""+ this.HEIGHT +"\" style=\"border:1px solid black\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n");
        writer.write("<!-- Created by " + PROGRAM_NAME + " -->\n");
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
     * If there is some IO Exception, return an SVG with a text that indicates the error
     * @param msg The error
     * @return An SVG element that contains the error
     */
    protected String getSvgErrorMessage(String msg) {
        return String.format("<svg width=\"200\" height=\"100\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                "<!-- Created by " + PROGRAM_NAME +" -->\n" +
                "<g><text x=\"10\" y=\"10\">%s</text>\n</g>\n" +
                "</svg>\n", msg);
    }

    public abstract String getSvg();

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


}
