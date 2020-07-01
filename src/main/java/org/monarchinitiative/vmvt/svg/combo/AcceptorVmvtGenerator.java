package org.monarchinitiative.vmvt.svg.combo;

import org.monarchinitiative.vmvt.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.svg.ruler.PositionRuler;
import org.monarchinitiative.vmvt.svg.ruler.SequenceRuler;
import org.monarchinitiative.vmvt.svg.walker.SvgSequenceWalker;
import org.monarchinitiative.vmvt.svg.AbstractSvgCoreGenerator;
import org.monarchinitiative.vmvt.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;

/**
 * This class creates an SVG graphic that contains a sequence ruler (intron/exon positions), the
 * reference sequence, deviating alternate bases, a box around the variant, the sequence logo,
 * and a sequence walker. This class is for the acceptor.
 * @author Peter N Robinson
 */
public class AcceptorVmvtGenerator extends AbstractSvgGenerator {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;
    private final static int SVG_WIDTH = 450;
    private final static int SVG_HEIGHT = 250;

    public AcceptorVmvtGenerator(String ref, String alt) {
        super(SVG_WIDTH,SVG_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.acceptor();
        this.spliceHeightMatrix = DoubleMatrix.acceptorHeightMatrix();
    }

    @Override
    public String getSvg() {
        int startX = 20;
        int startY = 60;

        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator acceptorLogo =
                    new SvgSequenceLogo(reference, alternate, this.spliceHeightMatrix, WIDTH, HEIGHT, startX, startY);
            acceptorLogo.write(swriter);
            startY += acceptorLogo.getYincrement();
            AbstractSvgCoreGenerator acceptorWalker =
                    new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, startX, startY);
            acceptorWalker.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
