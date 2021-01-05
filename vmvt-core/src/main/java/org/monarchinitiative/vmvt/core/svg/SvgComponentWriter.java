package org.monarchinitiative.vmvt.core.svg;


import org.monarchinitiative.vmvt.core.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Dimensions.*;

/**
 * This class intends to centralize where SVGs are written. Each component of the SVG is treated as a vertical stripe
 * that knows how to write itself and knows how high it is. This class adds the header and the footer and can write to
 * a file.
 * @author Peter N Robinson
 */
public class SvgComponentWriter implements SvgHeaderFooter, SvgWriter {

    private final static int totalMarginHeight = 30;
    private final DoubleMatrix donor;
    private final DoubleMatrix acceptor;
    private final DoubleMatrix donorHeight;
    private final DoubleMatrix acceptorHeight;
    private final DistributionCalculator donorDistribution;
    private final DistributionCalculator acceptorDistribution;
    private final static int NUM_SAMPLES = 250_000;

    private final boolean framed;

    private final int svgWidth;

    private SvgComponentWriter(int width, boolean framed) {
        this.svgWidth = width;
        this.framed = framed;
        donor = DoubleMatrix.donor();
        acceptor = DoubleMatrix.acceptor();
        donorHeight = DoubleMatrix.donorHeightMatrix();
        acceptorHeight = DoubleMatrix.acceptorHeightMatrix();
        donorDistribution = new DistributionCalculator(donor);
        acceptorDistribution = new DistributionCalculator(acceptor,NUM_SAMPLES);
    }



    @Deprecated
    public String getSvg(){
        throw new UnsupportedOperationException();
    }

    public String getSvg(SvgComponent... components) {
        List<SvgComponent> comps = new ArrayList<>();
        comps.addAll(Arrays.asList(components));
        return getSvg(comps);
    }

    public String getSvg(List<SvgComponent> components) {
        int totalComponentHeight = components.stream().mapToInt(SvgComponent::height).sum();
        int totalIntercomponentHeight = (components.size() - 1)* INTERCOMPONENT_VERTICAL_OFFSET;
        int totalHeight = totalComponentHeight + totalIntercomponentHeight + SVG_Y_TOP_MARGIN + SVG_Y_BOTTOM_MARGIN;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter, this.svgWidth, totalHeight, this.framed);
            for (var c : components) {
                c.write(swriter, SVG_Y_TOP_MARGIN);
            }
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    @Override
    public void write(Writer writer) throws IOException {
        throw new UnsupportedOperationException();
    }

    public static SvgComponentWriter donorWriter(boolean framed) {
        return new SvgComponentWriter(SVG_DONOR_WIDTH, framed);
    }

    public static SvgComponentWriter donorWriter() {
        return new SvgComponentWriter(SVG_DONOR_WIDTH, false);
    }

    public static SvgComponentWriter acceptorWriter(boolean framed) {
        return new SvgComponentWriter(SVG_ACCEPTOR_WIDTH, framed);
    }

    public static SvgComponentWriter acceptorWriter() {
        return new SvgComponentWriter(SVG_ACCEPTOR_WIDTH, false);
    }


}
