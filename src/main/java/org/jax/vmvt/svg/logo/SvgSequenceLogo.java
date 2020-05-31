package org.jax.vmvt.svg.logo;


import org.jax.vmvt.except.SvgwalkerRuntimeException;
import org.jax.vmvt.pssm.DoubleMatrix;
import org.jax.vmvt.svg.AbstractSvgWriter;
import org.jax.vmvt.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * TODO -- FOR NOW DUPLICATE CODE.
 * LATER -- PROBABLY MAKE COMMON SUPER CLASS FOR THIS AND {@link SvgSequenceWalker} AFTER
 * WE UNDERSTAND NEEDS/ARCHITECTURE
 */
public abstract class SvgSequenceLogo extends AbstractSvgWriter {
    /** Maximum height of the letters in the sequence logo. Needs to be adjusted together with {@link #FUDGE_FACTOR}.*/
    private final double LOGO_COLUMN_HEIGHT = 20.0;
    /** This is a magic number that places the letters in the correct vertical position. Works with {@link #LOGO_COLUMN_HEIGHT}.*/
    private final double FUDGE_FACTOR = 1.14;
    /** Width of the Upper-case letters used for the Logo */
    protected final static int LETTER_WIDTH = 8;
    /** Height of a letter before scaling */
    protected final static int LETTER_BASE_HEIGHT = 12;
    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART = 10;
    /** Position where we will start to write things from the top of the SVG */
    protected final int YSTART = 60;
    /** Amount of horizontal space to be taken up by one base character. */
    private final int LOWER_CASE_BASE_INCREMENT = LETTER_WIDTH + 5;
    /** Amount to shift down between ref and alt sequence lines */
    private final int Y_LINE_INCREMENT = 20;

    /**
     * Create an Svg Logo for the donor or acceptor with representation of reference sequence and alt bases
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (height matrix)
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     */
    public SvgSequenceLogo(String ref, String alt, DoubleMatrix site, int w, int h) {
        super(ref,alt,site, w, h);
   }

    /**
     * @return An SVG representation of the sequence logo with variant base or bases.
     */
    public abstract String getLogo();


    protected void initXYpositions() {
        this.currentX = XSTART;
        this.currentY = YSTART;
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
        } else if (seqlen == 27){
            int Xr = (int)(currentX + 0.7 * LOWER_CASE_BASE_INCREMENT);
            int Yr = (int)(Y-0.5*LOGO_COLUMN_HEIGHT);
            for (int i=0; i<seqlen; i++) {
                int j = i - 24; // substract 3 for the 3 intronic positions
                j = j <= 0 ? j - 1 : j; // we do not have a zeroth position in this display!
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.25,0.25) rotate(270)'>\n", Xr, Yr)); //scale(1,%f)
                writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"black\">%d</text>\n", j));
                writer.write("</g>");
                Xr += LOWER_CASE_BASE_INCREMENT;
            }
        } else {
            // should never happen
            throw new SvgwalkerRuntimeException("Sequence length must but 9 (donor) or 27 (acceptor) but we got " + seqlen);
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }



}
