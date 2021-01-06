package org.monarchinitiative.vmvt.core.svg;


import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Fonts.SVG_FONTS;

/**
 * Interface for classes that write SVG files with default implementations for writing the SVG header and footer.
 * @author <a herf="mailto:peter.robinson@jax.org">Peter N Robinson</a>
 */
public interface SvgHeaderFooter {
    /**
     *  Write the header of the SVG.
     *  Here we use the default font (Courier). To use another SVG font, implement this method and use
     *  a different {@link org.monarchinitiative.vmvt.core.svg.fontprofile.FontProfile} object -- it is
     *  important that some of the constants match the chosen font to get the letter distortions to look good.
     * @param writer file handle
     * @param width width of the SVG
     * @param height height of the SVG
     * @param framed if true, write a black frame around the SVG
     * @throws IOException if we cannot write
     */
    default void writeHeader(Writer writer, int width, int height, boolean framed) throws IOException {
        writer.write("<svg width=\"" + width +"\" height=\""+ height +"\" ");
        if (framed) {
            writer.write(    "style=\"border:1px solid black\" ");
        }
        writer.write(  "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n");
        writer.write("<!-- Created by vmvt -->\n");
        writer.write("<style>\n" +
                "  text { font: 24px " + SVG_FONTS + "; }\n" +
                "  text.t20 { font: 20px " + SVG_FONTS + "; }\n" +
                "  text.t14 { font: 14px " + SVG_FONTS + "; }\n" +
                "  text.t12 { font: 12px " + SVG_FONTS + "; }\n" +
                "  </style>\n");
        writer.write("<g>\n");
    }

    /**
     * Write the footer of the SVG
     * @param writer file handle
     * @throws IOException if we cannot write
     */
    default void writeFooter(Writer writer) throws IOException {
        writer.write("</g>\n</svg>\n");
    }
}
