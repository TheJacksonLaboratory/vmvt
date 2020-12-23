package org.monarchinitiative.vmvt.core;

import org.monarchinitiative.vmvt.core.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.icbar.SvgIcBarchart;
import org.monarchinitiative.vmvt.core.svg.trek.AcceptorTrekkerGenerator;
import org.monarchinitiative.vmvt.core.svg.trek.AcceptorTrekkerWithRiGenerator;
import org.monarchinitiative.vmvt.core.svg.trek.DonorTrekkerGenerator;
import org.monarchinitiative.vmvt.core.svg.delta.DeltaSvg;
import org.monarchinitiative.vmvt.core.svg.ese.EseSvg;
import org.monarchinitiative.vmvt.core.svg.ese.HeptamerEseSvg;
import org.monarchinitiative.vmvt.core.svg.ese.HexamerEseSvg;
import org.monarchinitiative.vmvt.core.svg.logo.AcceptorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.ruler.AcceptorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.DonorRuler;
import org.monarchinitiative.vmvt.core.svg.ruler.SvgSequenceRuler;
import org.monarchinitiative.vmvt.core.svg.trek.DonorTrekkerWithRiGenerator;
import org.monarchinitiative.vmvt.core.svg.walker.AcceptorWalkerGenerator;
import org.monarchinitiative.vmvt.core.svg.walker.SvgCanonicalCrypticGenerator;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

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
    /** If true, draw a black frame around SVG graphics. */
    private final boolean framed;

    public VmvtGenerator() {
        this(false);
    }

    public VmvtGenerator(boolean framed) {
        this.framed = framed;
        donor = DoubleMatrix.donor();
        acceptor = DoubleMatrix.acceptor();
        donorHeight = DoubleMatrix.donorHeightMatrix();
        acceptorHeight = DoubleMatrix.acceptorHeightMatrix();
        donorDistribution = new DistributionCalculator(donor);
        acceptorDistribution = new DistributionCalculator(acceptor,NUM_SAMPLES);
    }

    public String getDonorSequenceRuler(String reference, String alternate) {
        SvgSequenceRuler ruler = new DonorRuler(reference, alternate, framed);
        return ruler.getSvg();
    }

    public String getAcceptorSequenceRuler(String reference, String alternate) {
        SvgSequenceRuler ruler = new AcceptorRuler(reference, alternate, framed);
        return ruler.getSvg();
    }


    public String getDonorWalkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = SvgSequenceWalker.donor(reference, alternate, donor, framed);
        return svgGenerator.getSvg();
    }


    public String getAcceptorWalkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new AcceptorWalkerGenerator(reference, alternate, acceptor, framed);
        return svgGenerator.getSvg();
    }


    public String getDonorLogoSvg() {
        SvgSequenceLogo svgwriter = new DonorLogoGenerator(donorHeight, framed);
        return svgwriter.getSvg();
    }

    public String getAcceptorLogoSvg() {
        AbstractSvgGenerator svgGenerator = new AcceptorLogoGenerator(acceptorHeight, framed);
        return svgGenerator.getSvg();
    }

    public String getDonorTrekkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new DonorTrekkerGenerator(reference, alternate, framed);
        return svgGenerator.getSvg();
    }

    public String getAcceptorTrekkerSvg(String reference, String alternate) {
        AbstractSvgGenerator svgGenerator = new AcceptorTrekkerGenerator(reference, alternate, framed);
        return svgGenerator.getSvg();
    }

    public String getDonorDistributionSvg(String reference, String alternate) {
        DeltaSvg dsvg = new DeltaSvg(reference, alternate, donorDistribution, framed);
        return dsvg.getSvg();
    }

    public String getAcceptorDistributionSvg(String reference, String alternate) {
        DeltaSvg dsvg = new DeltaSvg(reference, alternate, acceptorDistribution, framed);
        return dsvg.getSvg();
    }

    public String getHexamerSvg(String reference, String alternate) {
        EseSvg ese = new HexamerEseSvg(reference, alternate, framed);
        return ese.getSvg();
    }

    public String getHeptamerSvg(String reference, String alternate) {
        EseSvg ese = new HeptamerEseSvg(reference, alternate, framed);
        return ese.getSvg();
    }

    public String getDelta(String reference, String alternate) {
        DeltaSvg svg = new DeltaSvg(reference, alternate, framed);
        return svg.getSvg();
    }

    public String getDonorCanonicalCryptic(String canonical, String cryptic) {
        SvgCanonicalCrypticGenerator gen = SvgCanonicalCrypticGenerator.donor(canonical, cryptic, donor, framed);
        return gen.getSvg();
    }

    public String getAcceptorCanonicalCryptic(String canonical, String cryptic) {
        SvgCanonicalCrypticGenerator gen = SvgCanonicalCrypticGenerator.acceptor(canonical, cryptic, acceptor, framed);
        return gen.getSvg();
    }

    public String getAcceptorWithRi(String reference, String alternate) {
        AbstractSvgMotifGenerator gen = AcceptorTrekkerWithRiGenerator.acceptor(reference, alternate, acceptor, framed);
        return gen.getSvg();
    }
    public String getDonorWithRi(String reference, String alternate) {
        AbstractSvgMotifGenerator gen = DonorTrekkerWithRiGenerator.donor(reference, alternate, donor, framed);
        return gen.getSvg();
    }

    public String getDonorIcBars(String reference, String alternate) {
        SvgIcBarchart gen = SvgIcBarchart.donorBarChart(reference, alternate, donor, true);
        return gen.getSvg();
    }

    public String getAcceptorIcBars(String reference, String alternate) {
        SvgIcBarchart gen = SvgIcBarchart.donorBarChart(reference, alternate, acceptor, true);
        return gen.getSvg();
    }



}
