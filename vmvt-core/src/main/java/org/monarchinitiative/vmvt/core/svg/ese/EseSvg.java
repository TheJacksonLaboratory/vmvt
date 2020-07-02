package org.monarchinitiative.vmvt.core.svg.ese;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.hexamer.KmerFeatureCalculator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import static org.monarchinitiative.vmvt.svg.SvgColors.*;

public abstract class EseSvg extends AbstractSvgGenerator  {


    /** Canvas width of the SVG. */
    private final static int SVG_WIDTH = 900;
    /** Canvas height of the SVG. */
    private final static int SVG_HEIGHT = 400;
    /** Width of one of the two kmer plots (ref/alt) this SVG will show. Needs to be less than half of {@link #SVG_WIDTH}*/
    private final static int KMER_PLOT_WIDTH = 400;


    /** Reference to a hexa (6) or hepta (7) feature calculator. */
    private final KmerFeatureCalculator calculator;

    private final String reference;
    private final String alternate;

    private final int kmerLen;
    private final int padding;
    /** This is the length we expect to get to calculate the kmer view.
     * For hexamers, this is 6+5, for heptamers it is 7+6.
     */
    private final int expectedSequenceLength;

    private int XSTART = 70;
    private int XEND = KMER_PLOT_WIDTH - 20;
    private final int YTOP = 30;
    private final int YBOTTOM = HEIGHT - YTOP;
    /* This will convert the scores in {@link #ESEscoresRef} to the corresponding height. */
    private final double YSCALE = 0.5 * (YBOTTOM - YTOP);
    /** This is the Y position of where x=0. */
    private final int X_AXIS_BASELINE = (YTOP+YBOTTOM)/2;

    private final double [] ESEscoresRef;
    private final double [] ESEscoresAlt;
    private final double meanESEref;
    private final double meanESEalt;

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
        meanESEref = Arrays.stream(ESEscoresRef).average().orElseThrow();
        meanESEalt = Arrays.stream(ESEscoresAlt).average().orElseThrow();
    }

    private void writeYaxis(Writer writer) throws IOException {
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\"/>\n",
                XSTART,YTOP,XSTART,YBOTTOM));
        // write axis ticks and numbers
        // We will show 11 ticks
        double Yincrement = (double)(YBOTTOM - YTOP)/10.0;
        int Xtickstart = XSTART-5;
        int Xtextstart = XSTART-40;
        double y = YTOP;
        double currentYval = 1.0;
        double Ynudge=5;
        int xnudge=8;
        for (int i=0; i<11; i++) {
            int x=Xtextstart;
            if (currentYval < 0){
                x -= xnudge;
            }
                writer.write(String.format("<line x1=\"%d\" y1=\"%f\" x2=\"%d\" y2=\"%f\" stroke=\"black\"/>\n",
                        Xtickstart,y,XSTART,y));
                // writer.write(String.format("<g transform='translate(%d,%d) scale(0.4,0.4)'><text>-10</text></g>\n", X3,Y3));
                writer.write(String.format("<g transform='translate(%d,%f) scale(0.6,0.6)'><text>%.1f</text></g>\n",
                        x, y+Ynudge, currentYval));
                currentYval -= 0.2;
                y += Yincrement;
        }
    }



    private void writeReferenceBoxes(Writer writer, double[] esescores) throws IOException {
        int y = X_AXIS_BASELINE; // y at y=0, i.e., the X-axis baseline
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\"/>\n",
                XSTART,y,XEND,y));
        int numbins = reference.length() - this.padding;
        double Xincrement = (double)(XEND-XSTART)/(double)numbins;
        double barWidth = 0.6*Xincrement;
        double x = XSTART;
        double xposForNumbers = XSTART - 0.5*Xincrement;
        double xposForBoxes = XSTART - 0.8*Xincrement;
        double y3 = y+20.0;
        int y2 = y+5; // height of tick on X axis
        for (int i=0;i<kmerLen;i++) {
            x += Xincrement;
            xposForNumbers += Xincrement;
            xposForBoxes += Xincrement;
            writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"black\"/>\n",
                    x,y,x,y2));

            double barHeight = esescores[i] * YSCALE;
            double Y = y - barHeight;
            if (barHeight > 1.0) {
                String rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:%s; fill: %s\" />\n",
                        xposForBoxes, Y, barWidth, barHeight, DARKGREEN, GREEN);
                writer.write(rect);
            } else if (barHeight < -1.0){
                String rect = String.format("<rect x=\"%f\" y=\"%d\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:%s; fill: %s\" fill-opacity=\"0.4\"/>\n",
                        xposForBoxes, y, barWidth, Math.abs(barHeight), DARKGREEN, GREEN);
                writer.write(rect);
            }
            writer.write(String.format("<g transform='translate(%f,%f) scale(0.6,0.6)'><text>%d</text></g>\n",
                    xposForNumbers, y3, (1+i)));
        }

        double meanESE = Arrays.stream(esescores).average().orElse(0);
        if (meanESE == 0) {
            return; // no need to draw line
        }
        if (meanESE < 0) {
            y = X_AXIS_BASELINE - (int) (meanESE * YSCALE);
        } else {
            y = X_AXIS_BASELINE -  (int) (meanESE * YSCALE);
        }
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>\n",
                XSTART,y,XEND,y, RED));
    }


    private void plotInterplotLine(Writer writer) throws IOException {
        double deltaESE = meanESEalt - meanESEref;
        int x1 = KMER_PLOT_WIDTH - 20;
        int x2 = 570; // TODO -- fragile, make this more robust
        int y1 =  X_AXIS_BASELINE - (int) (meanESEref * YSCALE);
        int y2 =  X_AXIS_BASELINE - (int) (meanESEalt * YSCALE);
        int maxY = Math.max(y1, y2);
        int midX = (int)(0.9*x1+0.1*x2);
        String line = String.format("<line x1=\"%d\" x2=\"%d\" y1=\"%d\" y2=\"%d\" " +
                        "stroke=\"%s\" stroke-width=\"1\"  stroke-dasharray=\"1, 3\"/>\n",
                x1,x2,y1,y2, RED);
        writer.write(line);
        String eseText = String.format("<text font-size=\"smaller\">ΔESE: %.2f</text>",deltaESE);
        int Y = Math.max(20, maxY-100);
        String smallerText = String.format("<g transform='translate(%d,%d) scale(0.75,0.75)'>%s</g>\n",
                midX, Y,eseText );
        writer.write(smallerText);
    }



    private void plotReference(Writer writer) throws IOException {
        XSTART = 70;
        XEND = KMER_PLOT_WIDTH - 20;
        writeYaxis(writer);
        writeReferenceBoxes(writer, this.ESEscoresRef);
    }

    private void plotAlternate(Writer writer) throws IOException {
        XSTART += 500;
        XEND += 500;
        writeYaxis(writer);
        writeReferenceBoxes(writer, this.ESEscoresAlt);
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
}
