package org.jax.svgwalker.svg;

import org.jax.svgwalker.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;

public class AcceptorWriter extends SvgWriter {

    public AcceptorWriter(String ref, String alt) {
        super(ref, alt, DoubleMatrix.acceptor());
    }

    @Override
    public String getWalker() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            initXYpositions();
            writeRefPlain(swriter);
            writeAltPlain(swriter);
            writeBoxAroundMutation(swriter);
            incrementYposition();
            writeRefWalker(swriter);
            writeRefAltSeparation(swriter);
            writeAltWalker(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
