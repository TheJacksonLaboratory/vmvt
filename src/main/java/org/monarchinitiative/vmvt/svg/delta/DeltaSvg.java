package org.monarchinitiative.vmvt.svg.delta;



import org.monarchinitiative.vmvt.dist.DistributionCalculator;
import org.monarchinitiative.vmvt.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.AbstractSvgGenerator;


import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class DeltaSvg extends AbstractSvgGenerator {

    private final List<Double> deltavals;
    private final double min;
    private final double max;

    private static final int DONOR_LENGTH = 9;

    private static final int ACCEPTOR_LENGTH = 27;

    private static final int BIN_COUNT = 51;
    private final int [] bins;

    private final static int SVG_WIDTH = 400;
    private final static int SVG_HEIGHT = 400;

    private final DoubleMatrix splicesite;

    private final double ref_R_i;

    private final double alt_R_i;

    private final double delta;


    public DeltaSvg(String ref, String alt) {
        super(SVG_WIDTH, SVG_HEIGHT);
        if (ref.length() != alt.length()) {
            throw new VmvtRuntimeException("Ref and alt must have the same length");
        }
        if (ref.length() == DONOR_LENGTH) {
            splicesite = DoubleMatrix.donor();
        } else if (ref.length() == ACCEPTOR_LENGTH) {
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


    public void dump() {
        for (int i=0;i<bins.length;i++) {
            System.out.printf("%d: %d\n", i, bins[i]);
        }
    }





    /**
     *   Write an axis from -10 to +10
     */
    private void writeTicks(Writer writer, double startX, double endX, int Y) throws IOException {
        writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"red\"/>\n",
                startX,Y,endX,Y));
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
        int Y2 = Y + tickHeight;
        for (int i=0;i<span;i++) {
            writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"red\"/>\n",
                    X,Y,X,Y2));
            // Write numbers under the tick marks. We use fudge factors to adjust for
            // different widths of the numbers (-10, -5, 0, 5, 10).
            if (i==0) {
                int Y3 = Y2 + 15;
                int X3 = (int)X - 10;
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>-10</text></g>\n", X3,Y3));
            } else if (i==5) {
                int Y3 = Y2 + 15;
                int X3 = (int)X - 5;
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>-5</text></g>\n", X3,Y3));
            } else if (i==10) {
                int Y3 = Y2 + 15;
                int X3 = (int)X - 2;
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>0</text></g>\n", X3,Y3));
            } else if (i==15) {
                int Y3 = Y2 + 15;
                int X3 = (int)X - 2;
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>5</text></g>\n", X3,Y3));
            } else if (i==20) {
                int Y3 = Y2 + 15;
                int X3 = (int)X - 5;
                writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>10</text></g>\n", X3,Y3));
            }
            X += increment;
        }

        if (delta>max) {
            throw new VmvtRuntimeException("Bad data -- measured delta greater than max");
        } else if (delta < min) {
            throw new VmvtRuntimeException("Bad data -- measured delta smaller than min");
        }
        double Xzero = (10.0*increment) + startX; // X position at ZERO of the graph
        if (delta > 0) {
            int x1 = (int)((delta*increment) + Xzero);
            int x2 = (int)((max*increment) + Xzero) + 10; // add 10 to include and overlap with max part of X axis
            int y1 = Y-5;
            int y2 = SVG_HEIGHT - 55;
            writeDeltaBox(writer,x1,y1,x2,y2);
        } else {
            int x1 = (int)((min*increment) + Xzero);
            int x2 = (int)((delta*increment) + Xzero);
            int y1 = 55;
            int y2 = SVG_HEIGHT - 90;
            writeDeltaBox(writer,x1,y1,x2,y2);
        }
    }




    private void writeDeltaBox(Writer writer, int x1, int y1, int x2, int y2) throws IOException {
        int barWidth = x2-x1;
        String rect = String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" rx=\"2\" " +
                        "style='stroke: #000099;\n" +
                        "         fill: #3333ff;\n" +
                        "         fill-opacity: 0.1;'/>\n",
                x1, 55, barWidth, SVG_HEIGHT - 90);
        writer.write(rect);
    }


    @Override
    public String getSvg() {
        int startX = 50;
        int endX = SVG_WIDTH - startX;
        double increment = ((double)(endX-startX))/(double)BIN_COUNT;

        int startY = SVG_HEIGHT - 60;
        int maxHeight = SVG_HEIGHT - 120;
        if (maxHeight<100 || endX <0) {
            // should never happen
            throw new VmvtRuntimeException("Bad params for DeltaSvg");
        }
        // maximum bin count should be maxHeight
        int maxCount = Arrays.stream(bins).max().orElseThrow();
        double heightFactor = (double)maxHeight/(double)maxCount;
        double barWidth = (double)(SVG_WIDTH-2*startX)/(double)BIN_COUNT;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            double X = (double)startX;
            for (int i=0;i<bins.length;i++) {
                double barHeight = heightFactor*bins[i];
                // the "y" of a rect is the upper left hand corner
                double Y = startY - barHeight;
                //style="stroke:#006600; fill: #00cc00"
                String rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:#006600; fill: #00cc00\" />\n",
                        X, Y,barWidth, barHeight);
                swriter.write(rect);
                X += barWidth;
            }
            double X1 = startX + barWidth;
            double X2 = X + 2*barWidth;
            writeTicks(swriter, X1, X2,  startY);

            writeFooter(swriter);

        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
        return swriter.toString();
    }
}
