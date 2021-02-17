package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;

import java.io.IOException;
import java.io.Writer;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Colors.*;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.*;

/**
 *  Each component of the SVG is treated as a vertical stripe
 *  that knows how to write itself and knows how high it is.
 */
public interface SvgComponent {

    void write(Writer writer, int starty) throws IOException;

    int height();

    default String getBaseColorFromChar(String b) {
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

    default String getBaseColor(int b) {
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

    /**
     * Write one lower case nucleotide (a, c, g, t) to show the sequence. This goes above the actual walker logo
     * and is just a plain letter but with one of the four base colors
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     * @throws IOException if we cannot write
     */
    default void writePlainBase(Writer writer, int x, int y, int base) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        String basestring = String.format("<text x=\"%d\" y=\"%d\" fill=\"%s\">%s</text>",x,y,color,nt);
        writer.write(basestring + "\n");
    }


    /**
     * Get the lower case character for this base
     * @param b index (0,1,2,3)
     * @return corresponding lower case character for the base (a,c,g,t)
     */
    default String getBaseCharLC(int b) {
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


}
