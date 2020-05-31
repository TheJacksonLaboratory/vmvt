package org.jax.vmvt;

import org.jax.vmvt.svg.AbstractSvgWriter;
import org.jax.vmvt.svg.combo.AcceptorVmvtWriter;
import org.jax.vmvt.svg.combo.DonorVmvtWriter;
import org.jax.vmvt.svg.logo.AcceptorLogoWriter;
import org.jax.vmvt.svg.logo.DonorLogoWriter;
import org.jax.vmvt.svg.walker.AcceptorWalkerWriter;
import org.jax.vmvt.svg.walker.DonorWalkerWriter;

public class VmvtWriter {
    


    public static void main( String[] args ) {
        System.out.println( "vmvt" );
    }

    public VmvtWriter() {

    }

    public String getDonorWalkerSvg(String reference, String alternate) {
        AbstractSvgWriter svgwriter = new DonorWalkerWriter(reference, alternate);
        return svgwriter.getSvg();
    }


    public String getAcceptorWalkerSvg(String reference, String alternate) {
        AbstractSvgWriter svgwriter = new AcceptorWalkerWriter(reference, alternate);
        return svgwriter.getSvg();
    }


    public String getDonorLogoSvg(String reference, String alternate) {
        AbstractSvgWriter svgwriter = new DonorLogoWriter(reference, alternate);
        return svgwriter.getSvg();
    }

    public String getAcceptorLogoSvg(String reference, String alternate) {
        AbstractSvgWriter svgwriter = new AcceptorLogoWriter(reference, alternate);
        return svgwriter.getSvg();
    }

    public String getDonorVmvtSvg(String reference, String alternate) {
        AbstractSvgWriter svgwriter = new DonorVmvtWriter(reference, alternate);
        return svgwriter.getSvg();
    }

    public String getAcceptorVmvtSvg(String reference, String alternate) {
        AbstractSvgWriter svgwriter = new AcceptorVmvtWriter(reference, alternate);
        return svgwriter.getSvg();
    }


}
