package org.monarchinitiative.vmvt.core.svg.delta;



import org.monarchinitiative.vmvt.core.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.SvgComponent;


import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Colors.*;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.ACCEPTOR_NT_LENGTH;
import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.DONOR_NT_LENGTH;


/**
 * Write an SVG that shows the distribution of differences expected by a random single-nucleotide variant
 * in a splice sequence (acceptor or donor). Note that the difference induced by a multinucleotide change
 * or indel can be greater, but we will still display it in this graphic for simplicity.
 * @author Peter Robinson
 */
public class DeltaSvg implements SvgComponent {
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
    /** Where to write the text showing the delta-Ri (x). */
    private final static int X_LOC_DELTA_RI = 230;
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
    private void writeTicks(Writer writer, double startX, double endX, int ypos) throws IOException {
        writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"%s\"/>\n",
                startX,ypos,endX,ypos, BLACK));
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
        int Y2 = ypos + tickHeight;
        int YmajorTick = ypos + 10;
        int Y_INCREMENT = 25; // amount of space to "lower" the numbers on the X axixs
        for (int i=0;i<span;i++) {
            if (i%5==0) {
                writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"%s\"/>\n",
                        X, ypos, X, YmajorTick, BLACK));
            } else {
                writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"%s\"/>\n",
                        X, ypos, X, Y2, BLACK));
            }
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
//        if (delta>max) {
//            throw new VmvtRuntimeException("Bad data -- measured delta greater than max");
//        } else if (delta < min) {
//            throw new VmvtRuntimeException("Bad data -- measured delta smaller than min");
//        }
        double Xzero = (10.0*increment) + startX; // X position at ZERO of the graph
        if (delta > 0) {
            int x1 = (int)((delta*increment) + Xzero);
            writeDeltaInformation(writer,x1, ypos);
        } else {
            int x1 = (int)((min*increment) + Xzero);
            writeDeltaInformation(writer,x1, ypos);
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

    private void writeDeltaInformation(Writer writer, int x, int ypos) throws IOException {
        int Y_LOC_DELTA_RI = ypos-250;
        String deltaRi = String.format("<text x=\"%d\" y=\"%d\" font-size=\"16\">\n" +
                "ΔR" +
                "<tspan dy=\"3\" font-size=\"12\">i</tspan></text>\n" +
                "<text x=\"%d\" y=\"%d\">: %.2f</text>\n",
                X_LOC_DELTA_RI,Y_LOC_DELTA_RI,X_LOC_DELTA_RI+38,Y_LOC_DELTA_RI,this.delta);
        int y_offset = 100;
        int lineheight = ypos-y_offset;
        writer.write(deltaRi);
        if (delta > max) {
            // In this case, the delta-Ri is larger than the delta-Ri of any single nt mutation
            // draw a red box around the delta Ri
            int y_bottom = Y_LOC_DELTA_RI + 15;
            int y_top = Y_LOC_DELTA_RI - 30;
            int x_left = X_LOC_DELTA_RI - 10;
            int x_right = X_LOC_DELTA_RI + 150;

            writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\" stroke-width=\"3\"/>\n",
                    x_left,y_bottom,x_right,y_bottom, RED));
            writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\" stroke-width=\"3\"/>\n",
                    x_left,y_top,x_right, y_top, RED));
            writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\" stroke-width=\"3\"/>\n",
                    x_left+1,y_bottom,x_left+1,y_top, RED));
            writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\" stroke-width=\"3\"/>\n",
                    x_right-1,y_bottom,x_right-1,y_top, RED));
        } else {
            // write a line with pinhead to show location of delta-Ri of the variant
            String line2 = String.format("<g fill=\"none\" stroke=\"%s\" stroke-width=\"2\">\n" +
                    "<path stroke-dasharray=\"2,2\" d=\"M%d %d l0 %d\"/>" +
                    "</g>\n", PURPLE, x, y_offset, lineheight);
            String circle = String.format("<circle cx=\"%d\" cy=\"%d\" r=\"8\" stroke=\"black\" \n" +
                    " stroke-width=\"3\" fill=\"%s\"/>\n", x, y_offset, PURPLE);
            writer.write(line2);
            writer.write(circle);
        }
    }

    @Override
    public void write(Writer writer, int ypos) throws IOException {
        int startX = 50;
        int maxHeight = SVG_DELTA_HEIGHT - 120;
        // maximum bin count should be maxHeight
        int maxCount = Arrays.stream(bins).max().orElseThrow();
        double heightFactor = (double)maxHeight/(double)maxCount;
        double barWidth = (double)(SVG_DELTA_WIDTH-2*startX)/(double)BIN_COUNT;
        double X = startX;
        ypos += 300;
        for (int bin : bins) {
            double barHeight = heightFactor * bin;
            // the "y" of a rect is the upper left hand corner
            double Y = ypos - barHeight;
            String rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                            "style=\"stroke:#006600; fill:%s\" />\n",
                    X, Y, barWidth, barHeight, GREEN);
            writer.write(rect);
            X += barWidth;
        }
        double X1 = startX + barWidth;
        double X2 = X + 2*barWidth;
        writeTicks(writer, X1, X2, ypos);
    }

    @Override
    public int height() {
        return 350;
    }
}
