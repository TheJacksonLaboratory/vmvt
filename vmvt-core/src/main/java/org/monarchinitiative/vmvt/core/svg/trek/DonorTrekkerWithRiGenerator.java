package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;
import org.monarchinitiative.vmvt.core.svg.logo.DonorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.ruler.DonorRuler;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.Writer;

/**
 * A specialization of {@link SvgTrekkerWithRi} for splice donors
 * @author Peter N Robinson
 */
public class DonorTrekkerWithRiGenerator extends SvgTrekkerWithRi{

    private final boolean framed;

    public DonorTrekkerWithRiGenerator(String ref, String alt, DoubleMatrix site, boolean framed) {
        super(ref,alt,SVG_DONOR_WIDTH, site,DoubleMatrix.donorHeightMatrix(), framed);
        this.framed = framed;
    }

    public static AbstractSvgMotifGenerator donor(String ref, String alt, DoubleMatrix donor, boolean framed) {
        return new DonorTrekkerWithRiGenerator(ref, alt,donor, framed);
    }

    @Override
    public void write(Writer swriter) throws IOException {
        DonorRuler ruler = new DonorRuler(this.ref, this.alt);
        ruler.write(swriter, SvgConstants.Dimensions.SVG_Y_TOP_MARGIN);
//        SvgSequenceLogo donorLogo = new DonorLogoGenerator(this.spliceHeightMatrix, SVG_DONOR_LOGO_START, this.framed);
//        donorLogo.write(swriter);
        SvgSequenceLogo donorLogo = SvgSequenceLogo.donor(this.spliceHeightMatrix);
        //new DonorLogoGenerator(this.spliceHeightMatrix, this.framed);
        donorLogo.write(swriter, 10);
        System.out.println("SVG_TREKKER_WALKER_START_Y: " + SVG_WALKER_START_Y);
        AbstractSvgMotifGenerator donorWalker =
                new SvgSequenceWalker(this.ref, this.alt, this.splicesite, WIDTH, HEIGHT, SVG_WALKER_START_Y, this.framed);
        donorWalker.write(swriter);
        // now show the Ri values
        double refR_i = splicesite.getIndividualSequenceInformation(this.ref);
        double altR_i = splicesite.getIndividualSequenceInformation(this.alt);
        writeRiChange(swriter, refR_i, altR_i, SVG_WALKER_START_Y+120);
    }
}
