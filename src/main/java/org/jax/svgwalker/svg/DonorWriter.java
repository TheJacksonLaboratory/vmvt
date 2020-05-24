package org.jax.svgwalker.svg;

import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;

public class DonorWriter extends SvgWriter {
    
    private final int WIDTH = 400;
    private final int HEIGHT = 200;
    /** AMount of horizontal space to be taken up by one base character. */
    private final int LOWER_CASE_BASE_INCREMENT = 15;
    /** Amount to shift down between ref and alt sequence lines */
    private final int Y_LINE_INCREMENT = 20;



    public DonorWriter(String reference, String alternate) {
        super(reference, alternate, 400, 200 ,DoubleMatrix.donor());
    }

    @Override
    public String getSvg() {
       try {
           writeHeader(swriter);
           writeDonor(swriter);
           writeFooter(swriter);
           return swriter.toString();
       } catch (IOException e) {
           return getSvgErrorMessage(e.getMessage());
       }
    }





    private void writeDonor(StringWriter swriter) throws IOException {
        this.currentX = XSTART;
        this.currentY = YSTART;
        writeRefPlain(swriter);
        writeAltPlain(swriter);
        writeBoxAroundMutation(swriter);
        this.currentY += Y_LINE_INCREMENT; // Addd extra space
        writeRefWalker(swriter);
        writeRefAltSeparation(swriter);
        writeAltWalker(swriter);
    }


}
