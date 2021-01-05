package org.monarchinitiative.vmvt.core;

import org.monarchinitiative.vmvt.core.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.*;
import org.monarchinitiative.vmvt.core.svg.icbar.DeltaRiBox;
import org.monarchinitiative.vmvt.core.svg.icbar.SvgIcBarchart;
import org.monarchinitiative.vmvt.core.svg.delta.DeltaSvg;
import org.monarchinitiative.vmvt.core.svg.ese.EseSvg;
import org.monarchinitiative.vmvt.core.svg.ese.HeptamerEseSvg;
import org.monarchinitiative.vmvt.core.svg.ese.HexamerEseSvg;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.ruler.SvgSequenceRuler;
import org.monarchinitiative.vmvt.core.svg.walker.SvgCanonicalCrypticGenerator;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.SVG_Y_TOP_MARGIN;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Fonts.SVG_FONTS;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Vmvt.PROGRAM_NAME;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Vmvt.PROGRAM_VERSION;

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
        SvgComponent donorRuler = SvgSequenceRuler.donor(reference, alternate);
        return getDonorSvg(donorRuler);
    }

    public String getAcceptorSequenceRuler(String reference, String alternate) {
        SvgComponent acceptorRuler = SvgSequenceRuler.acceptor(reference, alternate);
        return getAcceptorSvg(acceptorRuler);
    }


    public String getDonorWalkerSvg(String reference, String alternate) {
        SvgComponent donorWalker = SvgSequenceWalker.donorWalker(reference, alternate, donor);
        return getDonorSvg(donorWalker);
    }


    public String getAcceptorWalkerSvg(String reference, String alternate) {
        SvgComponent acceptorWalker = SvgSequenceWalker.acceptorWalker(reference, alternate, acceptor);
        return getAcceptorSvg(acceptorWalker);
    }


    public String getDonorLogoSvg() {
        SvgComponent donorLogo = SvgSequenceLogo.donor(donorHeight);
        return getDonorSvg(donorLogo);
    }

    public String getAcceptorLogoSvg() {
        SvgComponent acceptorLogo = SvgSequenceLogo.acceptor(acceptorHeight);
        return getAcceptorSvg(acceptorLogo);
    }

    public String getDonorTrekkerSvg(String reference, String alternate) {
        SvgComponent donorLogo = SvgSequenceLogo.donor(donorHeight);
        SvgComponent donorWalker = SvgSequenceWalker.donorWalker(reference, alternate, donor);
        return getDonorSvg(donorLogo, donorWalker);
    }

    public String getAcceptorTrekkerSvg(String reference, String alternate) {
        SvgComponent acceptorLogo = SvgSequenceLogo.acceptor(acceptorHeight);
        SvgComponent acceptorWalker = SvgSequenceWalker.acceptorWalker(reference, alternate, acceptor);
        return getAcceptorSvg(acceptorLogo, acceptorWalker);
    }

    public String getDonorDistributionSvg(String reference, String alternate) {
        SvgComponent dsvg = new DeltaSvg(reference, alternate, donorDistribution);
        return getSvg(ESE_SVG_WIDTH, dsvg);
    }

    public String getAcceptorDistributionSvg(String reference, String alternate) {
        SvgComponent dsvg = new DeltaSvg(reference, alternate, acceptorDistribution);
        return getSvg(ESE_SVG_WIDTH, dsvg);
    }

    public String getHexamerSvg(String reference, String alternate) {
        SvgComponent ese = new HexamerEseSvg(reference, alternate, framed);
        return getSvg(SvgConstants.Dimensions.ESE_SVG_WIDTH, ese);
    }

    public String getHeptamerSvg(String reference, String alternate) {
        SvgComponent ese = new HeptamerEseSvg(reference, alternate, framed);
        return getSvg(SvgConstants.Dimensions.ESE_SVG_WIDTH, ese);
    }

    public String getDelta(String reference, String alternate) {
        SvgComponent delta = new DeltaSvg(reference, alternate);
        return getSvg(SvgConstants.Dimensions.ESE_SVG_WIDTH, delta);
   }

    public String getDonorCanonicalCryptic(String canonical, String cryptic) {
        SvgCanonicalCrypticGenerator gen = SvgCanonicalCrypticGenerator.donor(canonical, cryptic, donor, framed);
        return gen.getSvg();
    }

    public String getAcceptorCanonicalCryptic(String canonical, String cryptic) {
        SvgCanonicalCrypticGenerator gen = SvgCanonicalCrypticGenerator.acceptor(canonical, cryptic, acceptor, framed);
        return gen.getSvg();
    }

    public String getAcceptorTrekkerWithRi(String reference, String alternate) {
        SvgComponent acceptorRuler = SvgSequenceRuler.acceptor(reference, alternate);
        SvgComponent acceptorLogo = SvgSequenceLogo.acceptor(acceptorHeight);
        SvgComponent acceptorWalkerWithRi = SvgSequenceWalker.acceptorWalker(reference, alternate, acceptor);
        SvgComponent deltaRi = DeltaRiBox.acceptor(reference, alternate, acceptor);
        return getAcceptorSvg(acceptorRuler, acceptorLogo, acceptorWalkerWithRi, deltaRi);
    }
    public String getDonorTrekkerWithRi(String reference, String alternate) {
        SvgComponent donorRuler = SvgSequenceRuler.donor(reference, alternate);
        SvgComponent donorLogo = SvgSequenceLogo.donor(donorHeight);
        SvgComponent donorWalkerWithRi = SvgSequenceWalker.donorWalker(reference, alternate, donor);
        SvgComponent deltaRi = DeltaRiBox.donor(reference, alternate, donor);
        return getDonorSvg(donorRuler, donorLogo, donorWalkerWithRi, deltaRi);
    }

    public String getDonorIcBars(String reference, String alternate) {
        SvgComponent donorBarChart = SvgIcBarchart.donorBarChart(reference, alternate, donor);
        return getDonorSvg(donorBarChart);
    }

    public String getDonorIcBarsWithRi(String reference, String alternate) {
        SvgComponent donorBarChart = SvgIcBarchart.donorBarChart(reference, alternate, donor);
        SvgComponent deltaRi = DeltaRiBox.donor(reference, alternate, donor);
        return getDonorSvg(donorBarChart, deltaRi);
    }

    public String getAcceptorIcBars(String reference, String alternate) {
        SvgComponent acceptorBarChart = SvgIcBarchart.acceptorBarChart(reference, alternate, donor);
        return getAcceptorSvg(acceptorBarChart);
    }

    public String getAcceptorIcBarsWithRi(String reference, String alternate) {
        SvgComponent acceptorBarChart = SvgIcBarchart.acceptorBarChart(reference, alternate, acceptor);
        SvgComponent deltaRi = DeltaRiBox.acceptor(reference, alternate, acceptor);
        return getAcceptorSvg(acceptorBarChart, deltaRi);
    }

    /**
     * Return an SVG for a Splice donor variant
     * @param components List of {@link SvgComponent} with the vertical parts of the SVG
     * @return the SVG graphic
     */
    public String getDonorSvg(SvgComponent... components) {
        List<SvgComponent> comps = new ArrayList<>();
        comps.addAll(Arrays.asList(components));
        return getSvg(SVG_DONOR_WIDTH, comps);
    }

    /**
     * Return an SVG for a Splice acceptor variant
     * @param components List of {@link SvgComponent} with the vertical parts of the SVG
     * @return the SVG graphic
     */
    public String getAcceptorSvg(SvgComponent... components) {
        List<SvgComponent> comps = new ArrayList<>();
        comps.addAll(Arrays.asList(components));
        return getSvg(SVG_ACCEPTOR_WIDTH, comps);
    }

    /**
     * Return an SVG for a general SVG graphic of defined width
     * @param components List of {@link SvgComponent} with the vertical parts of the SVG
     * @return the SVG graphic
     */
    public String getSvg(int width, SvgComponent... components) {
        List<SvgComponent> comps = new ArrayList<>();
        comps.addAll(Arrays.asList(components));
        return getSvg(width, comps);
    }

    public String getSvg(int width, List<SvgComponent> components) {
        int totalComponentHeight = components.stream().mapToInt(SvgComponent::height).sum();
        int totalIntercomponentHeight = (components.size() - 1)* INTERCOMPONENT_VERTICAL_OFFSET;
        int totalHeight = totalComponentHeight + totalIntercomponentHeight + SVG_Y_TOP_MARGIN + SVG_Y_BOTTOM_MARGIN;
        StringWriter swriter = new StringWriter();
        int ypos = SVG_Y_TOP_MARGIN;
        try {
            writeHeader(swriter, width, totalHeight, this.framed);
            for (var c : components) {
                c.write(swriter, ypos);
                ypos += c.height();
            }
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    /**
     * If there is some IO Exception, return an SVG with a text that indicates the error
     * @param msg The error
     * @return An SVG element that contains the error
     */
    private String getSvgErrorMessage(String msg) {
        return String.format("<svg width=\"200\" height=\"100\" " +
                "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n" +
                "<!-- Created by " + PROGRAM_NAME + "(" + PROGRAM_VERSION + ") -->\n" +
                "<g><text x=\"10\" y=\"10\">%s</text>\n</g>\n" +
                "</svg>\n", msg);
    }

    /**
     *  Write the header of the SVG.
     *  Here we use the default font (Courier). To use another SVG font, implement this method and use
     *  a different {@link org.monarchinitiative.vmvt.core.svg.fontprofile.FontProfile} object -- it is
     *  important that some of the constants match the chosen font to get the letter distortions to look good.
     * @param writer file handle
     * @param width width of the SVG
     * @param height height of the SVG
     * @param framed if true, write a black frame around the SVG
     * @throws IOException if we cannot write
     */
    private void writeHeader(Writer writer, int width, int height, boolean framed) throws IOException {
        writer.write("<svg width=\"" + width +"\" height=\""+ height +"\" ");
        if (framed) {
            writer.write(    "style=\"border:1px solid black\" ");
        }
        writer.write(  "xmlns=\"http://www.w3.org/2000/svg\" " +
                "xmlns:svg=\"http://www.w3.org/2000/svg\">\n");
        writer.write("<!-- Created by vmvt -->\n");
        writer.write("<style>\n" +
                "  text { font: 24px " + SVG_FONTS + "; }\n" +
                "  text.t20 { font: 20px " + SVG_FONTS + "; }\n" +
                "  text.t14 { font: 14px " + SVG_FONTS + "; }\n" +
                "  </style>\n");
        writer.write("<g>\n");
    }

    /**
     * Write the footer of the SVG
     * @param writer file handle
     * @throws IOException if we cannot write
     */
    private void writeFooter(Writer writer) throws IOException {
        writer.write("</g>\n</svg>\n");
    }

}
