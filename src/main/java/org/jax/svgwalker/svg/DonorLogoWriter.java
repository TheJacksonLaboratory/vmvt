package org.jax.svgwalker.svg;


import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;

/**
 * TODO -- Probably refactor/simplifiy after the LOGO SVG is working
 */
public class DonorLogoWriter extends SvgSequenceLogo {

    public DonorLogoWriter(String ref, String alt) {
        super(ref, alt, DoubleMatrix.donorHeightMatrix());
    }

    @Override
    public String getLogo() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            initXYpositions();
            incrementYposition();
            incrementYposition();
            writeLogo(swriter);
            incrementYposition();


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
