package org.jax.svgwalker;

import org.jax.svgwalker.svg.*;

public class WalkerWriter {
    


    public static void main( String[] args ) {
        System.out.println( "SVG Walker" );
    }

    public WalkerWriter() {

    }

    public String getDonorWalkerSvg(String reference, String alternate) {
        SvgSequenceWalker svgwriter = new DonorWalkerWriter(reference, alternate);
        return svgwriter.getWalker();
    }


    public String getAcceptorWalkerSvg(String reference, String alternate) {
        SvgSequenceWalker svgwriter = new AcceptorWalkerWriter(reference, alternate);
        return svgwriter.getWalker();
    }


    public String getDonorLogoSvg(String reference, String alternate) {
        SvgSequenceLogo svgwriter = new DonorLogoWriter(reference, alternate);
        return svgwriter.getLogo();
    }


}
