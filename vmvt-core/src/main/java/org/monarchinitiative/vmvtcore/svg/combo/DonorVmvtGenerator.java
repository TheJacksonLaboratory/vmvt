package org.monarchinitiative.vmvtcore.svg.combo;

import org.monarchinitiative.vmvtcore.pssm.DoubleMatrix;
import org.monarchinitiative.vmvtcore.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvtcore.svg.walker.SvgSequenceWalker;
import org.monarchinitiative.vmvtcore.svg.AbstractSvgCoreGenerator;
import org.monarchinitiative.vmvtcore.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;

/**
 * This class creates an SVG graphic that contains a sequence ruler (intron/exon positions), the
 * reference sequence, deviating alternate bases, a box around the variant, the sequence logo,
 * and a sequence walker. This class is for the splice donor.
 * @author Peter N Robinson
 */
public class DonorVmvtGenerator extends AbstractSvgGenerator {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;

    private final static int SVG_WIDTH = 170;
    private final static int SVG_HEIGHT = 250;

    public DonorVmvtGenerator(String ref, String alt) {
        super(SVG_WIDTH,SVG_HEIGHT);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.donor();
        this.spliceHeightMatrix = DoubleMatrix.donorHeightMatrix();
    }

    @Override
    public String getSvg() {
        int startX = 20;
        int startY = 60;

        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator donorLogo =
                    new SvgSequenceLogo(reference, alternate, this.spliceHeightMatrix, WIDTH, HEIGHT, startX, startY);
            donorLogo.write(swriter);
            startY += donorLogo.getYincrement();

            AbstractSvgCoreGenerator donorWalker =
                    new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, startX, startY);
            donorWalker.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
