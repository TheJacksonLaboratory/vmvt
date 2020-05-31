package org.jax.vmvt.svg;

import java.io.IOException;
import java.io.Writer;

/**
 * This is the superclass for writing an SVG string of all types.
 * @author Peter Robinson
 */
public abstract class AbstractSvgWriter {
    /** This name will appear in the comment of the SVGs we produce. */
    private final String PROGRAM_NAME = "vmvt";

    /** Total width of the SVG canvas. */
    protected final int WIDTH;
    /** Total height of the SVG canvas. */
    protected final int HEIGHT;

    public AbstractSvgWriter(int w, int h) {
        this.WIDTH = w;
        this.HEIGHT = h;
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

    public abstract String getSvg();

}
