package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
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

    public DonorTrekkerWithRiGenerator(String ref, String alt, DoubleMatrix site) {
        super(ref,alt,SVG_DONOR_WIDTH, site,DoubleMatrix.donorHeightMatrix());
    }

    public static AbstractSvgMotifGenerator donor(String ref, String alt, DoubleMatrix donor) {
        return new DonorTrekkerWithRiGenerator(ref, alt,donor);
    }

    @Override
    public void write(Writer swriter) throws IOException {
        DonorRuler ruler = new DonorRuler(this.ref, this.alt);
        ruler.write(swriter);
        SvgSequenceLogo donorLogo = new DonorLogoGenerator(this.spliceHeightMatrix, SVG_DONOR_LOGO_START);
        donorLogo.write(swriter);
        System.out.println("SVG_TREKKER_WALKER_START_Y: " + SVG_WALKER_START_Y);
        AbstractSvgMotifGenerator donorWalker =
                new SvgSequenceWalker(this.ref, this.alt, this.splicesite, WIDTH, HEIGHT, SVG_WALKER_START_Y);
        donorWalker.write(swriter);
        // now show the Ri values
        double refR_i = splicesite.getIndividualSequenceInformation(this.ref);
        double altR_i = splicesite.getIndividualSequenceInformation(this.alt);
        writeRiChange(swriter, refR_i, altR_i, SVG_WALKER_START_Y+120);
    }
}
