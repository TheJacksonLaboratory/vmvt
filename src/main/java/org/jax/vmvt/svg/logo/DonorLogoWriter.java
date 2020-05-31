package org.jax.vmvt.svg.logo;


import org.jax.vmvt.VmtVisualizer;
import org.jax.vmvt.pssm.DoubleMatrix;
import org.jax.vmvt.svg.AbstractSvgWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Write an SVG seqeunce logo for a splice donor site with reference and alternate sequences
 */
public class DonorLogoWriter extends AbstractSvgWriter implements VmtVisualizer {

    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;

        /**
     * Write a sequence logo for a splice donor site (showing ref/alt sequences)
     * Note that the size of the SVG is set in the superclass constructor (w,h)
     * @param ref Reference sequence
     * @param alt Alternate (mutant) sequence
     */
    public DonorLogoWriter(String ref, String alt) {
        super(400, 400);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.donorHeightMatrix();
    }

    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
//            initXYpositions();
//            incrementYposition();
//            writeLogo(swriter);
//            incrementYposition(0.4);
//            writeRefPlain(swriter);
//            writeAltPlain(swriter);
//            writeBoxAroundMutation(swriter);
           writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
