package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class DonorTrekkerWithRiGenerator extends AbstractSvgMotifGenerator{
    /** Add some extra width to write the R_i of the sequences. */
    private static final int EXTRA_X_FOR_RI = 140;
    private static final int  SVG_CANONICAL_CRYPTIC_HEIGHT = 340;


    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;


    public DonorTrekkerWithRiGenerator(int w, int h, String ref, String alt, DoubleMatrix site) {
        super(ref,alt,w+EXTRA_X_FOR_RI, h);
        this.splicesite = site;
        this.spliceHeightMatrix = DoubleMatrix.donorHeightMatrix();
    }




    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    @Override
    public void write(Writer swriter) throws IOException {
        SvgSequenceLogo donorLogo =
                new DonorLogoGenerator(this.spliceHeightMatrix);
        donorLogo.write(swriter);
        AbstractSvgMotifGenerator donorWalker =
                new SvgSequenceWalker(this.ref, this.alt, this.splicesite, WIDTH, HEIGHT, TREKKER_WALKER_START_Y);
        donorWalker.write(swriter);

    }

    public static AbstractSvgMotifGenerator donor(String ref, String alt, DoubleMatrix donor) {
        return new DonorTrekkerWithRiGenerator(SVG_DONOR_WIDTH, SVG_CANONICAL_CRYPTIC_HEIGHT, ref, alt,donor);
    }
}
