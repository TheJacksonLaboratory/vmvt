package org.monarchinitiative.vmvt;

import org.monarchinitiative.vmvt.svg.combo.AcceptorVmvtGenerator;
import org.monarchinitiative.vmvt.svg.combo.DonorVmvtGenerator;
import org.monarchinitiative.vmvt.svg.logo.AcceptorLogoGenerator;
import org.monarchinitiative.vmvt.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.svg.walker.AcceptorWalkerGenerator;
import org.monarchinitiative.vmvt.svg.walker.DonorWalkerGenerator;
import org.monarchinitiative.vmvt.svg.AbstractSvgGenerator;

/**
 * Public interface to VMVT: Variant-Motif Visualization Tool. For all functions listed here, there
 * are two parameters: reference and alternate. In the case of the donor functions, these should be
 * 9 nucleotide sequences (3:intron + 6: exon). In the case of the acceptor functions, these should
 * be 27 nucleotide sequences (25:intron + 2 exon).
 * @author Peter N Robinson
 */
public class VmvtGenerator {

    public String getDonorWalkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new DonorWalkerGenerator(reference, alternate);
        return svgGenerator.getSvg();
    }


    public String getAcceptorWalkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new AcceptorWalkerGenerator(reference, alternate);
        return svgGenerator.getSvg();
    }


    public String getDonorLogoSvg(String reference, String alternate) {
        AbstractSvgGenerator svgwriter = new DonorLogoGenerator(reference, alternate);
        return svgwriter.getSvg();
    }

    public String getAcceptorLogoSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new AcceptorLogoGenerator(reference, alternate);
        return svgGenerator.getSvg();
    }

    public String getDonorVmvtSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new DonorVmvtGenerator(reference, alternate);
        return svgGenerator.getSvg();
    }

    public String getAcceptorVmvtSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new AcceptorVmvtGenerator(reference, alternate);
        return svgGenerator.getSvg();
    }


}
