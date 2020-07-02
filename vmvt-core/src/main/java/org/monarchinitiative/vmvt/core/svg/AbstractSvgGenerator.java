package org.monarchinitiative.vmvt.core.svg;

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
    protected final static int SVG_LOGO_STARTY = 40;
    /** Height on the SVG canvas for Sequence logos. */
    protected final static int SVG_LOGO_HEIGHT = 50;

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

}
