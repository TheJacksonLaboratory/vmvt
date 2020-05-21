package org.jax.svgwalker.svg;

import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class DonorWriter extends SvgWriter {
    
    private final int WIDTH = 400;
    private final int HEIGHT = 200;
    /** Position where we will start to write things from the left side of the SVG. */
    private final int XSTART = 10;
    /** Position where we will start to write things from the top of the SVG */
    private final int YSTART = 10;
    /** AMount of horizontal space to be taken up by one base character. */
    private final int LOWER_CASE_BASE_INCREMENT = 15;
    /** Amount to shift down between ref and alt sequence lines */
    private final int Y_LINE_INCREMENT = 20;



    public DonorWriter(String reference, String alternate) {
        super(reference, alternate, DoubleMatrix.donor());
    }

    @Override
    public String getSvg() {
       try {
           writeHeader(swriter, WIDTH, HEIGHT);
           writeDonor(swriter);
           writeFooter(swriter);
           return swriter.toString();
       } catch (IOException e) {
           System.err.println("[ERROR] IO Exception while writing SVG");
       }
        return "IO Exception while writing SVG";
    }





    private void writeDonor(StringWriter swriter) {
        this.currentX = XSTART;
        this.currentY = YSTART;
        try {
            writeRefPlain(swriter);
            writeAltPlain(swriter);
            this.currentY += Y_LINE_INCREMENT; // Addd extra space
            writeRefWalker(swriter);
        } catch (IOException e) {
            // TODO  -- figure out desired behavior on failure
            System.err.println("[ERROR] Could not write donor");
        }
    }
}
