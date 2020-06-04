package org.monarchinitiative.vmvt.svg.walker;

import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.AbstractSvgMotifWriter;

import java.io.IOException;
import java.io.Writer;


/**
 * Base class for writing SVG sequence walkers for Splice Acceptor or Donor sequences
 * @author Peter N Robinson
 */
public class SvgSequenceWalker extends AbstractSvgMotifWriter {

    /** Position where we will start to write things from the left side of the SVG. */
    protected final int XSTART;
    /** Position where we will start to write things from the top of the SVG */
    protected final int YSTART;

    protected int currentX;
    protected int currentY;


    /**
     * Create an Svg Walker for the donor or acceptor with representation of reference sequence and alt bases
     * @param ref reference sequence
     * @param alt alternate (mutant) sequence
     * @param site Representation of the splice site (weight matrix)
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     */
    public SvgSequenceWalker(String ref, String alt, DoubleMatrix site, int w, int h, int x, int y) {
        super(ref,alt,site,w,h);
        this.XSTART = x;
        this.YSTART = y;
        this.currentX = this.XSTART;
        this.currentY = this.YSTART;
    }




    /**
     * Add some extra vertical space (one {@link #Y_LINE_INCREMENT}).
     */
    protected void incrementYposition() {
        this.currentY += Y_LINE_INCREMENT;
    }




    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     */
    protected void writeWalkerBase(Writer writer, int x, int y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        double IC = this.splicesite.get(base, pos);

        if (IC>0) {
            writer.write(String.format("<g transform='translate(%d,%d) scale(1,%f)'>\n",x,y,IC)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
        } else {
            double xpos = (double)x + (double)LOWER_CASE_BASE_INCREMENT;
            int ypos = y+1;
            writer.write(String.format("<g transform='translate(%f,%d)  scale(1,%f)  rotate(180)'>\n",xpos,ypos, Math.abs(IC))); //
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>\n");
        }
    }


    /**
     * Write one lower case nucleotide (a, c, g, t) for the walker.
     * @param writer A string writer
     * @param x x position
     * @param y y position
     * @param base index of the base
     */
    protected void writeWalkerAltBase(Writer writer, int x, int y, int base, int pos) throws IOException {
        String color = getBaseColor(base);
        String nt = getBaseCharLC(base);
        double IC = this.splicesite.get(base, pos);

        if (IC>0) {
            writer.write(String.format("<g transform='translate(%d,%d) scale(1,%f)'>\n",x,y,IC)); //scale(1,%f)
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\">%s</text>\n",color,nt));
            writer.write("</g>");
        } else {
            double xpos = (double)x + (double)LOWER_CASE_BASE_INCREMENT;
            int ypos = y+1;
            writer.write(String.format("<g transform='translate(%f,%d)  scale(1,%f)  rotate(180)'>\n",xpos,ypos, Math.abs(IC))); //
            writer.write(String.format("<text x=\"0\" y=\"0\" fill=\"%s\" opacity=\"0.4\" style=\"text-shadow: 2px 2px #FF0000;\">%s</text>\n",color,nt));
            writer.write("</g>\n");
        }
    }

    protected void writeRefWalker(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        int startX = X;
        for (int i=0; i<seqlen; i++) {
            writeWalkerBase(writer, X, Y, refidx[i], i);
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }

    protected void writeRefAltSeparation(Writer writer) throws IOException {
        //currentY += (double)Y_LINE_INCREMENT/5.0;
        int endX = (1+this.seqlen) * LOWER_CASE_BASE_INCREMENT;
        writer.write("<g fill=\"none\" stroke=\"black\" stroke-width=\"1\">\n");
        writer.write(String.format("<path stroke-dasharray=\"2,2\" d=\"M%d %d L%d %d\"/>\n", XSTART, currentY, endX, currentY));
        writer.write("</g>\n");
    }

    protected void writeAltWalker(Writer writer) throws IOException {
        int X = currentX;
        int Y = currentY;
        for (int i=0; i<seqlen; i++) {
            if (refidx[i] != altidx[i]) {
                writeWalkerAltBase(writer, X, Y, altidx[i], i);
            }
            X += LOWER_CASE_BASE_INCREMENT;
        }
        // Reset (x,y) for next line
        currentX = XSTART;
    }

    @Override
    public void write(Writer writer) throws IOException {
        writeAltWalker(writer);
        writeRefWalker(writer);
        writeRefAltSeparation(writer);
    }

    @Override
    public int getYincrement() {
       // return this.currentY - this.startY;
        throw new UnsupportedOperationException("TODO");
    }

}
