package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.AcceptorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.ruler.AcceptorRuler;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.Writer;

/**
 * A specialization of {@link SvgTrekkerWithRi} for splice acceptors
 * @author Peter N Robinson
 */
public class AcceptorTrekkerWithRiGenerator extends SvgTrekkerWithRi{

    public AcceptorTrekkerWithRiGenerator(String ref, String alt, DoubleMatrix site) {
        super(ref,alt,SVG_ACCEPTOR_WIDTH, site,DoubleMatrix.acceptorHeightMatrix());
    }

    public static AbstractSvgMotifGenerator acceptor(String ref, String alt, DoubleMatrix acceptor) {
        return new AcceptorTrekkerWithRiGenerator(ref, alt, acceptor);
    }


    @Override
    public void write(Writer swriter) throws IOException {
        AcceptorRuler ruler = new AcceptorRuler(this.ref, this.alt);
        ruler.write(swriter);
        SvgSequenceLogo donorLogo = new AcceptorLogoGenerator(this.spliceHeightMatrix, SVG_DONOR_LOGO_START);
        donorLogo.write(swriter);
        System.out.println("SVG_TREKKER_WALKER_START_Y: " + SVG_WALKER_START_Y);
        AbstractSvgMotifGenerator walker =
                new SvgSequenceWalker(this.ref, this.alt, this.splicesite, WIDTH, HEIGHT, SVG_WALKER_START_Y);
        walker.write(swriter);
        // now show the Ri values
        double refR_i = splicesite.getIndividualSequenceInformation(this.ref);
        double altR_i = splicesite.getIndividualSequenceInformation(this.alt);
        writeRiChange(swriter, refR_i, altR_i, SVG_WALKER_START_Y+120);
    }
}
