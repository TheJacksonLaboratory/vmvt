package org.jax.vmvt.svg.logo;

import org.jax.vmvt.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;

public class AcceptorLogoWriter extends SvgSequenceLogo {

    /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public AcceptorLogoWriter(String ref, String alt) {
        super(ref, alt, DoubleMatrix.acceptorHeightMatrix(), 500, 400);
    }

    @Override
    public String getLogo() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            initXYpositions();
            incrementYposition();
            writeLogo(swriter);
            incrementYposition(0.4);
            writeRefPlain(swriter);
            writeAltPlain(swriter);
            writeBoxAroundMutation(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
