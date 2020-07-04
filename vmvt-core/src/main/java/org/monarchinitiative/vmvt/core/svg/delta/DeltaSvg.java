package org.monarchinitiative.vmvt.core.svg.delta;



import org.monarchinitiative.vmvt.core.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgColors;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;


/**
 * Write an SVG that shows the distribution of differences expected by a random single-nucleotide variant
 * in a splice sequence (acceptor or donor). Note that the difference induced by a multinucleotide change
 * or indel can be greater, but we will still display it in this graphic for simplicity.
 * @author Peter Robinson
 */
public class DeltaSvg extends AbstractSvgGenerator {
    /** List of values for the change in R_i induced by random SNVs. */
    private final List<Double> deltavals;
    /** Minumum R_i value for any sequence */
    private final double min;
    /** Maximum R_i value for any sequence */
    private final double max;

    /** Number of bins to show in the histogram. */
    private static final int BIN_COUNT = 51;
    /** Counts of values in each of the bins. */
    private final int [] bins;
    /** Canvas width of the SVG. */
    private final static int SVG_DELTA_WIDTH = 400;
    /** Canvas height of the SVG. */
    private final static int SVG_DELTA_HEIGHT = 400;
    /** Y position to start writing -- this will be the location of the X axis. */
    private final static int START_Y = SVG_DELTA_HEIGHT - 60;

    private final static int X_LOC_DELTA_RI = 230;
    private final static int Y_LOC_DELTA_RI = 50;
    /** Representation of the information content matrix of a donor or acceptor splice site. */
    private final DoubleMatrix splicesite;
    /** Individual sequence information content of the reference sequence. */
    private final double ref_R_i;
    /** Individual sequence information content of the alternate sequence. */
    private final double alt_R_i;
    /** Change in R_i between {@link #ref_R_i} and {@link #alt_R_i}. */
    private final double delta;

    /**
     * Construct Delta-SVG for either a donor or an acceptor site.
     * @param ref reference sequence
     * @param alt alternate sequence
     */
    public DeltaSvg(String ref, String alt) {
        super(SVG_DELTA_WIDTH, SVG_DELTA_HEIGHT);
        if (ref.length() != alt.length()) {
            throw new VmvtRuntimeException("Ref and alt must have the same length");
        }
        if (ref.length() == DONOR_NT_LENGTH) {
            splicesite = DoubleMatrix.donor();
        } else if (ref.length() == ACCEPTOR_NT_LENGTH) {
            splicesite = DoubleMatrix.acceptor();
        } else {
            throw new VmvtRuntimeException("Sequence length must be 9 or 27");
        }
        DistributionCalculator dcal = new DistributionCalculator(splicesite);
        this.deltavals = dcal.getDeltas();
        this.min = deltavals.stream().mapToDouble(Double::doubleValue).min().orElseThrow();
        this.max = deltavals.stream().mapToDouble(Double::doubleValue).max().orElseThrow();
        double span = max - min;
        if (span==0.0) {
            throw new VmvtRuntimeException("min == max in DeltaSvg");
        }
        bins = new int[BIN_COUNT];
        for (Double d : deltavals) {
            double normalized = (d-min)/span;
            int i = (int)Math.ceil((BIN_COUNT-1) * normalized);
            bins[i]++;
        }
        this.ref_R_i = this.splicesite.getIndividualSequenceInformation(ref);
        this.alt_R_i = this.splicesite.getIndividualSequenceInformation(alt);
        this.delta = ref_R_i - alt_R_i;
    }

    /**
     * Construct Delta-SVG for either a donor or an acceptor site.
     * @param ref reference sequence
     * @param alt alternate sequence
     */
    public DeltaSvg(String ref, String alt, DistributionCalculator dcal) {
        super(SVG_DELTA_WIDTH, SVG_DELTA_HEIGHT);
        this.deltavals = dcal.getDeltas();
        this.min = deltavals.stream().mapToDouble(Double::doubleValue).min().orElseThrow();
        this.max = deltavals.stream().mapToDouble(Double::doubleValue).max().orElseThrow();
        double span = max - min;
        if (span==0.0) {
            throw new VmvtRuntimeException("min == max in DeltaSvg");
        }
        bins = new int[BIN_COUNT];
        for (Double d : deltavals) {
            double normalized = (d-min)/span;
            int i = (int)Math.ceil((BIN_COUNT-1) * normalized);
            bins[i]++;
        }
        this.splicesite = dcal.getSplicesite();
        this.ref_R_i = this.splicesite.getIndividualSequenceInformation(ref);
        this.alt_R_i = this.splicesite.getIndividualSequenceInformation(alt);
        this.delta = ref_R_i - alt_R_i;

    }





    /**
     * For testing. Dump the number of counts in each bin to the shell.
     */
    public void dump() {
        for (int i=0;i<bins.length;i++) {
            System.out.printf("%d: %d\n", i, bins[i]);
        }
    }





    /**
     *   Write an axis from -10 to +10
     */
    private void writeTicks(Writer writer, double startX, double endX) throws IOException {
        writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"%s\"/>\n",
                startX,START_Y,endX,START_Y, SvgColors.RED));
        // for both donor and acceptor the biggest change is nearly 10/-10 startX is -10 and startY is 10
        if (Math.round(this.min) != -10) {
            // should never happen
            throw new VmvtRuntimeException("Bad start point -- should be -10");
        }
        if (Math.round(this.max) != 10) {
            throw new VmvtRuntimeException("Bad end point -- should be 10");
        }
        int span = 21;
        double increment = (endX - startX)/(double)span;
        double X = startX;
        int tickHeight = 5;
        int Y2 = START_Y + tickHeight;
        int Y_INCREMENT = 25; // amount of space to "lower" the numbers on the X axixs
        for (int i=0;i<span;i++) {
            writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"%s\"/>\n",
                    X,START_Y,X,Y2, SvgColors.RED));
            // Write numbers under the tick marks. We use fudge factors to adjust for
            // different widths of the numbers (-10, -5, 0, 5, 10).
            if (i==0) {
                int Y3 = Y2 + Y_INCREMENT;
                int X3 = (int)X - 30;
                writer.write(String.format("<g transform='translate(%d,%d)'><text>-10</text></g>\n", X3,Y3));
            } else if (i==5) {
                int Y3 = Y2 + Y_INCREMENT;
                int X3 = (int)X - 20;
                writer.write(String.format("<g transform='translate(%d,%d)'><text>-5</text></g>\n", X3,Y3));
            } else if (i==10) {
                int Y3 = Y2 + Y_INCREMENT;
                int X3 = (int)X - 7;
                writer.write(String.format("<g transform='translate(%d,%d)'><text>0</text></g>\n", X3,Y3));
            } else if (i==15) {
                int Y3 = Y2 + Y_INCREMENT;
                int X3 = (int)X - 7;
                writer.write(String.format("<g transform='translate(%d,%d)'><text>5</text></g>\n", X3,Y3));
            } else if (i==20) {
                int Y3 = Y2 + Y_INCREMENT;
                int X3 = (int)X - 12;
                writer.write(String.format("<g transform='translate(%d,%d)'><text>10</text></g>\n", X3,Y3));
            }
            X += increment;
        }
        // the following should never happen
        if (delta>max) {
            throw new VmvtRuntimeException("Bad data -- measured delta greater than max");
        } else if (delta < min) {
            throw new VmvtRuntimeException("Bad data -- measured delta smaller than min");
        }
        double Xzero = (10.0*increment) + startX; // X position at ZERO of the graph
        if (delta > 0) {
            int x1 = (int)((delta*increment) + Xzero);
            writeDeltaInformation(writer,x1);
        } else {
            int x1 = (int)((min*increment) + Xzero);
            writeDeltaInformation(writer,x1);
        }
    }


    /**
     * Write text showing the Delta R_i, as well as a circle and a dashed
     * line representing the magnitude of Delta R_i (unless Delta R_i is
     * greater than 10).
     * @param writer handle to writer
     * @param x X location corresponding to position of the delta-Ri value
     * @throws IOException if there are problems writing the SVG elements
     */

    private void writeDeltaInformation(Writer writer, int x) throws IOException {
        String deltaRi = String.format("<text x=\"%d\" y=\"%d\" font-size=\"16\">\n" +
                "ΔR" +
                "<tspan dy=\"3\" font-size=\"12\">i</tspan></text>\n" +
                "<text x=\"%d\" y=\"%d\">: %.2f</text>\n",
                X_LOC_DELTA_RI,Y_LOC_DELTA_RI,X_LOC_DELTA_RI+38,Y_LOC_DELTA_RI,this.delta);
        int lineheight = START_Y-100;
        String line2 = String.format("<g fill=\"none\" stroke=\"%s\" stroke-width=\"2\">\n" +
                "<path stroke-dasharray=\"2,2\" d=\"M%d %d l0 %d\"/>" +
                "</g>\n", SvgColors.PURPLE, x,100,lineheight);

        String circle = String.format("<circle cx=\"%d\" cy=\"%d\" r=\"8\" stroke=\"black\" \n" +
                " stroke-width=\"3\" fill=\"%s\"/>\n",x,100, SvgColors.PURPLE);

        writer.write(deltaRi);
        writer.write(line2);
        writer.write(circle);

    }


    @Override
    public String getSvg() {
        int startX = 50;
        int maxHeight = SVG_DELTA_HEIGHT - 120;
        // maximum bin count should be maxHeight
        int maxCount = Arrays.stream(bins).max().orElseThrow();
        double heightFactor = (double)maxHeight/(double)maxCount;
        double barWidth = (double)(SVG_DELTA_WIDTH-2*startX)/(double)BIN_COUNT;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            double X = startX;
            for (int bin : bins) {
                double barHeight = heightFactor * bin;
                // the "y" of a rect is the upper left hand corner
                double Y = START_Y - barHeight;
                String rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:#006600; fill:%s\" />\n",
                        X, Y, barWidth, barHeight, SvgColors.GREEN);
                swriter.write(rect);
                X += barWidth;
            }
            double X1 = startX + barWidth;
            double X2 = X + 2*barWidth;
            writeTicks(swriter, X1, X2);
            writeFooter(swriter);
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
        return swriter.toString();
    }

    @Override
    public void write(Writer writer) throws IOException {
        throw new UnsupportedOperationException("todo");
    }
}
