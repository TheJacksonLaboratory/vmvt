package org.monarchinitiative.vmvt;

import org.monarchinitiative.vmvt.svg.combo.AcceptorVmvtWriter;
import org.monarchinitiative.vmvt.svg.combo.DonorVmvtWriter;
import org.monarchinitiative.vmvt.svg.logo.AcceptorLogoWriter;
import org.monarchinitiative.vmvt.svg.logo.DonorLogoWriter;
import org.monarchinitiative.vmvt.svg.walker.AcceptorWalkerWriter;
import org.monarchinitiative.vmvt.svg.walker.DonorWalkerWriter;
import org.monarchinitiative.vmvt.svg.AbstractSvgWriter;

/**
 * Public interface to VMVT: Variant-Motif Visualization Tool. For all functions listed here, there
 * are two parameters: reference and alternate. In the case of the donor functions, these should be
 * 9 nucleotide sequences (3:intron + 6: exon). In the case of the acceptor functions, these should
 * be 27 nucleotide sequences (25:intron + 2 exon).
 * @author Peter N Robinson
 */
public class VmvtWriter {

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
