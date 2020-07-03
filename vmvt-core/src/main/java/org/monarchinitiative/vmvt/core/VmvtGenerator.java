package org.monarchinitiative.vmvt.core;

import org.monarchinitiative.vmvt.core.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.combo.AcceptorVmvtGenerator;
import org.monarchinitiative.vmvt.core.svg.combo.DonorVmvtGenerator;
import org.monarchinitiative.vmvt.core.svg.delta.DeltaSvg;
import org.monarchinitiative.vmvt.core.svg.ese.EseSvg;
import org.monarchinitiative.vmvt.core.svg.ese.HexamerEseSvg;
import org.monarchinitiative.vmvt.core.svg.logo.AcceptorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.ruler.AcceptorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.DonorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.SvgSequenceRuler;
import org.monarchinitiative.vmvt.core.svg.walker.AcceptorWalkerGenerator;
import org.monarchinitiative.vmvt.core.svg.walker.DonorWalkerGenerator;

/**
 * Public interface to VMVT: Variant-Motif Visualization Tool. For all functions listed here, there
 * are two parameters: reference and alternate. In the case of the donor functions, these should be
 * 9 nucleotide sequences (3:intron + 6: exon). In the case of the acceptor functions, these should
 * be 27 nucleotide sequences (25:intron + 2 exon).
 * @author Peter N Robinson
 */
public class VmvtGenerator {

    private final DoubleMatrix donor;
    private final DoubleMatrix acceptor;
    private final DoubleMatrix donorHeight;
    private final DoubleMatrix acceptorHeight;
    private final DistributionCalculator donorDistribution;
    private final DistributionCalculator acceptorDistribution;
    private final static int NUM_SAMPLES = 250_000;

    public VmvtGenerator() {
        donor = DoubleMatrix.donor();
        acceptor = DoubleMatrix.acceptor();
        donorHeight = DoubleMatrix.donorHeightMatrix();
        acceptorHeight = DoubleMatrix.acceptorHeightMatrix();
        donorDistribution = new DistributionCalculator(donor);
        acceptorDistribution = new DistributionCalculator(acceptor,NUM_SAMPLES);
    }

    public String getDonorSequenceRuler(String reference, String alternate) {
        SvgSequenceRuler ruler = new DonorRuler(reference, alternate);
        return ruler.getSvg();
    }

    public String getAcceptorSequenceRuler(String reference, String alternate) {
        SvgSequenceRuler ruler = new AcceptorRuler(reference, alternate);
        return ruler.getSvg();
    }


    public String getDonorWalkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new DonorWalkerGenerator(reference, alternate, donor);
        return svgGenerator.getSvg();
    }


    public String getAcceptorWalkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new AcceptorWalkerGenerator(reference, alternate, acceptor);
        return svgGenerator.getSvg();
    }


    public String getDonorLogoSvg() {
        SvgSequenceLogo svgwriter = new DonorLogoGenerator(donorHeight);
        return svgwriter.getSvg();
    }

    public String getAcceptorLogoSvg() {
        AbstractSvgGenerator svgGenerator = new AcceptorLogoGenerator(acceptorHeight);
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

    public String getDonorDistributionSvg(String reference, String alternate) {
        DeltaSvg dsvg = new DeltaSvg(reference, alternate, donorDistribution);
        return dsvg.getSvg();
    }

    public String getAcceptorDistributionSvg(String reference, String alternate) {
        DeltaSvg dsvg = new DeltaSvg(reference, alternate, acceptorDistribution);
        return dsvg.getSvg();
    }

    public String getHexamerSvg(String reference, String alternate) {
        EseSvg ese = new HexamerEseSvg(reference, alternate);
        return ese.getSvg();
    }


}
