package org.monarchinitiative.vmvt.core.svg.ese;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.hexamer.KmerFeatureCalculator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgColors;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

public abstract class EseSvg extends AbstractSvgGenerator {
    /** Canvas width of the SVG */
    private final static int SVG_WIDTH = 900;
    /** Canvas height of the SVG.*/
    private final static int SVG_HEIGHT = 300;
    /** Width of one of the two kmer plots (ref/alt) this SVG will show. Needs to be less than half of {@link #SVG_WIDTH}*/
    private final static int KMER_PLOT_WIDTH = 350;
    /** Start to draw the first ESE plot here. */
    private int XSTART_REF_PLOT = 80;
    private int XEND_REF_PLOT = XSTART_REF_PLOT + KMER_PLOT_WIDTH;
    private int INTERPLOT_GAP = 100;
    /** x start position of the second plot. */
    private int XSTART_ALT_PLOT = XEND_REF_PLOT + INTERPLOT_GAP;
    private int XEND_ALT_PLOT = XSTART_ALT_PLOT + KMER_PLOT_WIDTH;

    /**
     * Reference to a hexa (6) or hepta (7) feature calculator.
     */
    private final KmerFeatureCalculator calculator;

    private final String reference;
    private final String alternate;

    private final int kmerLen;
    private final int padding;
    /**
     * This is the length we expect to get to calculate the kmer view.
     * For hexamers, this is 6+5, for heptamers it is 7+6.
     */
    private final int expectedSequenceLength;




    private final int YTOP = 100;
    private final int YBOTTOM = HEIGHT - 20;
    /* This will convert the scores in {@link #ESEscoresRef} to the corresponding height. */
    private final double YSCALE = 0.5 * (YBOTTOM - YTOP);
    /**
     * This is the Y position of where x=0.
     */
    private final int X_AXIS_BASELINE = (YTOP + YBOTTOM) / 2;

    private final double[] ESEscoresRef;
    private final double[] ESEscoresAlt;
    /** Sum of the 6/7-mer scores for each position (reference). */
    private final double sumESEref;
    /** Sum of the 6/7-mer scores for each position (reference). */
    private final double sumESEalt;
    /** Mean of the 6/7-mer scores for each position (reference). */
    private final double meanESEref;
    /** Mean of the 6/7-mer scores for each position (reference). */
    private final double meanESEalt;
    /** Difference between the sum of ESE scores for reference and alt. */
    private final double deltaESE;


    public EseSvg(KmerFeatureCalculator calc, String ref, String alt) {
        super(SVG_WIDTH, SVG_HEIGHT);
        this.calculator = calc;
        this.reference = ref.toUpperCase();
        this.alternate = alt.toUpperCase();
        this.kmerLen = calc.getKmerLength();
        this.padding = calc.getPadding();
        this.expectedSequenceLength = this.kmerLen + this.padding;
        if (reference.length() != expectedSequenceLength) {
            throw new VmvtRuntimeException("Unexpected reference sequence length for kmer analysis: " + reference);
        }
        if (alternate.length() != expectedSequenceLength) {
            throw new VmvtRuntimeException("Unexpected alternate sequence length for kmer analysis: " + alternate);
        }
        ESEscoresRef = calc.kmerScoreArray(this.reference);
        ESEscoresAlt = calc.kmerScoreArray(this.alternate);
        sumESEref = Arrays.stream(ESEscoresRef).sum();
        sumESEalt = Arrays.stream(ESEscoresAlt).sum();
        deltaESE = sumESEalt - sumESEref;
        meanESEref = Arrays.stream(ESEscoresRef).average().orElse(0);
        meanESEalt = Arrays.stream(ESEscoresAlt).average().orElse(0);
    }

    /**
     * Writes the Y axis for reference or alternate.
     * @param writer file handle
     * @throws IOException if we cannot write
     */
    private void writeYaxis(Writer writer, int xstart) throws IOException {
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\"/>\n",
                xstart, YTOP, xstart, YBOTTOM));
        // write axis ticks and numbers
        // We will show 11 ticks
        double Yincrement = (double) (YBOTTOM - YTOP) / 10.0;
        int Xtickstart = xstart - 5;
        int XbigTickstart = xstart - 10;
        int Xtextstart = xstart - 60;
        double y = YTOP;
        double currentYval = 1.0;
        double Ynudge = 5;
        int xnudge = 14;
        for (int i = 0; i <= 10; i++) {
            int x = Xtextstart;
            if (currentYval < 0) {
                x -= xnudge;
            }

            // writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>-10</text></g>\n", X3,Y3));
            if (i%5==0) { // just show ticks for -1, 0, and 1
                writer.write(String.format("<g transform='translate(%d,%f)'><text>%.1f</text></g>\n",
                        x, y + Ynudge, currentYval));
                writer.write(String.format("<line x1=\"%d\" y1=\"%f\" x2=\"%d\" y2=\"%f\" stroke=\"black\"/>\n",
                        XbigTickstart, y, xstart, y));
            } else {
                writer.write(String.format("<line x1=\"%d\" y1=\"%f\" x2=\"%d\" y2=\"%f\" stroke=\"black\"/>\n",
                        Xtickstart, y, xstart, y));
            }
            currentYval -= 0.2;
            y += Yincrement;
        }
    }


    /**
     * Writes an ESE bar plot for either reference or alternate
     * @param writer file handle
     * @param esescores ESE scores (for reference or alternate)
     * @param meanEseScore Corresponding average ESE score
     * @throws IOException if we cannot write.
     */
    private void writeEsePlot(Writer writer, double[] esescores, double meanEseScore, int xstart, int xend) throws IOException {
        int y = X_AXIS_BASELINE; // y at y=0, i.e., the X-axis baseline
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\"/>\n",
                xstart, y, xend, y));
        int numbins = reference.length() - this.padding;
        double Xincrement = (double) (xend - xstart) / (double) numbins;
        double barWidth = 0.6 * Xincrement;
        double x = xstart;
        double xposForNumbers = xstart - 0.5 * Xincrement;
        double xposForBoxes = xstart - 0.8 * Xincrement;
        double y3 = y + 20.0;
        int y2 = y + 5; // height of tick on X axis
        for (int i = 0; i < kmerLen; i++) {
            x += Xincrement;
            xposForNumbers += Xincrement;
            xposForBoxes += Xincrement;
            writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"black\"/>\n",
                    x, y, x, y2));

            double barHeight = esescores[i] * YSCALE;
            double Y = y - barHeight;
            if (barHeight > 1.0) {
                String rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:%s; fill: %s\" />\n",
                        xposForBoxes, Y, barWidth, barHeight, SvgColors.DARKGREEN, SvgColors.GREEN);
                writer.write(rect);
            } else if (barHeight < -1.0) {
                String rect = String.format("<rect x=\"%f\" y=\"%d\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:%s; fill: %s\" fill-opacity=\"0.4\"/>\n",
                        xposForBoxes, y, barWidth, Math.abs(barHeight), SvgColors.DARKGREEN, SvgColors.GREEN);
                writer.write(rect);
            }
            writer.write(String.format("<g transform='translate(%f,%d) scale(0.75,0.75)'><text>%d</text></g>\n",
                    xposForNumbers, YBOTTOM, (1 + i)));
        }
        if (meanEseScore == 0) {
            return; // no need to draw line
        } else if (meanEseScore < 0) {
            y = X_AXIS_BASELINE - (int) (meanEseScore * YSCALE);
        } else {
            y = X_AXIS_BASELINE - (int) (meanEseScore * YSCALE);
        }
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n",
                xstart, y, xend, y, SvgColors.RED));
    }

    /**
     * Plot a red dotted line between the average score of the reference and the average score of the
     * alternate sequence.
     * @param writer a file handle
     * @throws IOException if we cannot write the SVG
     */
    private void plotInterplotLine(Writer writer) throws IOException {
        int x1 = XEND_REF_PLOT - 20;
        int x2 = XSTART_ALT_PLOT; // TODO -- fragile, make this more robust
        int y1 = X_AXIS_BASELINE - (int) (meanESEref * YSCALE);
        int y2 = X_AXIS_BASELINE - (int) (meanESEalt * YSCALE);
        String line = String.format("<line x1=\"%d\" x2=\"%d\" y1=\"%d\" y2=\"%d\" " +
                        "stroke=\"%s\" stroke-width=\"1\"  stroke-dasharray=\"1, 3\"/>\n",
                x1, x2, y1, y2, SvgColors.RED);
        writer.write(line);

        String blueRect = String.format("<rect x=\"350\" y=\"30\" rx=\"3\" ry=\"3\" width=\"200\" height=\"40\" style=\"stroke: none; fill: %s;fill-opacity: 0.1\"></rect>",
                SvgColors.BLUE);
        String eseText = String.format("<text x='50%%' y='57' text-anchor='middle'>ΔESE: %.2f</text>", deltaESE);
        // Position to place the Delta-Ri value
        int X = KMER_PLOT_WIDTH - 40;
        int Y =  45;//Math.max(20, maxY - 100);

        writer.write(String.format("%s%s", blueRect, eseText));
    }


    private void plotReference(Writer writer) throws IOException {
        writeYaxis(writer, XSTART_REF_PLOT);
        writeEsePlot(writer, this.ESEscoresRef, this.meanESEref, XSTART_REF_PLOT, XEND_REF_PLOT);
    }

    private void plotAlternate(Writer writer) throws IOException {
        writeYaxis(writer, XSTART_ALT_PLOT);
        writeEsePlot(writer, this.ESEscoresAlt, this.meanESEalt, XSTART_ALT_PLOT, XEND_ALT_PLOT);
    }


    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            plotReference(swriter);
            plotAlternate(swriter);
            plotInterplotLine(swriter);
            writeFooter(swriter);
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
        return swriter.toString();
    }

    @Override
    public void write(Writer writer) {
        throw new UnsupportedOperationException("todo");
    }
}
