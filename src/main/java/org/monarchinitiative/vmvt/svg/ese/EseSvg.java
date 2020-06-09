package org.monarchinitiative.vmvt.svg.ese;

import org.monarchinitiative.vmvt.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.hexamer.KmerFeatureCalculator;
import org.monarchinitiative.vmvt.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class EseSvg extends AbstractSvgGenerator  {
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

    private final int XSTART = 70;
    private final int XEND = WIDTH - 20;
    private final int YTOP = 30;
    private final int YBOTTOM = HEIGHT - YTOP;
    /* This will convert the scores in {@link #ESEscoresRef} to the corresponding height. */
    private final double YSCALE = 0.5 * (YBOTTOM - YTOP);

    private final double [] ESEscoresRef;

    public EseSvg(KmerFeatureCalculator calc, String ref, String alt, int w, int h) {
        super(w,h);
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


    private void writeXaxis(Writer writer) throws IOException {
        int y = (YTOP+YBOTTOM)/2;
        writer.write(String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\"/>\n",
                XSTART,y,XEND,y));
        double Xincrement = (double)(XEND-XSTART)/(double)kmerLen;
        double barWidth = 0.6*Xincrement;
        double x = XSTART;
        double x2 = XSTART - 0.5*Xincrement;
        double xStartForBoxes = XSTART - 0.8*Xincrement;
        double y3 = y+20.0;
        int y2 = y+5; // height of tick on X axis
        for (int i=0;i<kmerLen;i++) {
            x += Xincrement;
            x2 += Xincrement;
            xStartForBoxes += Xincrement;
            writer.write(String.format("<line x1=\"%f\" y1=\"%d\" x2=\"%f\" y2=\"%d\" stroke=\"black\"/>\n",
                    x,y,x,y2));

            double barHeight = this.ESEscoresRef[i] * YSCALE;
            double Y = y - barHeight;
            if (barHeight > 1.0) {
                String rect = String.format("<rect x=\"%f\" y=\"%f\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:#006600; fill: #00cc00\" />\n",
                        xStartForBoxes, Y, barWidth, barHeight);
                writer.write(rect);
            } else if (barHeight < -1.0){
                String rect = String.format("<rect x=\"%f\" y=\"%d\" width=\"%f\" height=\"%f\" rx=\"2\" " +
                                "style=\"stroke:#006600; fill: #00cc00\" fill-opacity=\"0.4\"/>\n",
                        xStartForBoxes, y, barWidth, Math.abs(barHeight));
                writer.write(rect);
            }
            writer.write(String.format("<g transform='translate(%f,%f) scale(0.6,0.6)'><text>%d</text></g>\n",
                    x2, y3, (1+i)));
        }
    }





    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            writeYaxis(swriter);
            writeXaxis(swriter);
            writeFooter(swriter);
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
        return swriter.toString();
    }
}
