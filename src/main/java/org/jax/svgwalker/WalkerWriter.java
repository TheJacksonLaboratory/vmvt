package org.jax.svgwalker;

import org.jax.svgwalker.pssm.DoubleMatrix;
import org.jax.svgwalker.svg.DonorWriter;
import org.jax.svgwalker.svg.SvgWriter;

import java.io.IOException;
import java.io.StringWriter;

public class WalkerWriter {
    


    public static void main( String[] args ) {
        System.out.println( "SVG Walker" );
    }

    public WalkerWriter() {

    }

    public String getDonorSvg(String reference, String alternate) {
        SvgWriter svgwriter = new DonorWriter(reference, alternate);
        return svgwriter.getWalker();
    }


    public String getAcceptorSvg(String reference, String alternate) {
        return null;//this.walkerWriter.getDonorSvg(reference, alternate);
    }

}
