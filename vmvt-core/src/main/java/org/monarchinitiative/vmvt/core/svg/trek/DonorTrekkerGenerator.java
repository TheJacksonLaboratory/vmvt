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

    private final boolean framed;

    public DonorTrekkerGenerator(String ref, String alt, boolean framed) {
        super(SVG_TREKKER_DONOR_WIDTH,SVG_TREKKER_HEIGHT, framed);
        this.framed = framed;
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.donor();
        this.spliceHeightMatrix = DoubleMatrix.donorHeightMatrix();
    }

    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            write(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    @Override
    public void write(Writer swriter) throws IOException {
        SvgSequenceLogo donorLogo =
                new DonorLogoGenerator(this.spliceHeightMatrix, this.framed);
        donorLogo.write(swriter);
        AbstractSvgMotifGenerator donorWalker =
                new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, TREKKER_WALKER_START_Y, this.framed);
        donorWalker.write(swriter);
        writeFooter(swriter);
    }
}
