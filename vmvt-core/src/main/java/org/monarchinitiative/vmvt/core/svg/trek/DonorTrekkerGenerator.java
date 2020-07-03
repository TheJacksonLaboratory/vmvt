package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This class creates an SVG graphic that contains a sequence ruler (intron/exon positions), the
 * reference sequence, deviating alternate bases, a box around the variant, the sequence logo,
 * and a sequence walker. This class is for the splice donor.
 * @author Peter N Robinson
 */
public class DonorTrekkerGenerator extends AbstractSvgGenerator {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;

    public DonorTrekkerGenerator(String ref, String alt) {
        super(SVG_TREKKER_DONOR_WIDTH,SVG_TREKKER_HEIGHT);
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
            SvgSequenceLogo donorLogo =
                    new DonorLogoGenerator(this.spliceHeightMatrix);
            donorLogo.write(swriter);
            startY += TREKKER_Y_INCREMENT;
            AbstractSvgMotifGenerator donorWalker =
                    new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, startY);
            donorWalker.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    @Override
    public void write(Writer writer) throws IOException {
        throw new UnsupportedOperationException("todo");
    }
}
