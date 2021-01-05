package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgConstants;
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

    private final boolean framed;

    public AcceptorTrekkerWithRiGenerator(String ref, String alt, DoubleMatrix site, boolean framed) {
        super(ref,alt,SVG_ACCEPTOR_WIDTH, site,DoubleMatrix.acceptorHeightMatrix(), framed);
        this.framed = framed;
    }

    public static AbstractSvgMotifGenerator acceptor(String ref, String alt, DoubleMatrix acceptor,boolean framed) {
        return new AcceptorTrekkerWithRiGenerator(ref, alt, acceptor, framed);
    }


    @Override
    public void write(Writer swriter) throws IOException {
        AcceptorRuler ruler = new AcceptorRuler(this.ref, this.alt);
        ruler.write(swriter,SvgConstants.Dimensions.SVG_Y_TOP_MARGIN);
        SvgSequenceLogo acceptorLogo = SvgSequenceLogo.acceptor(this.spliceHeightMatrix);
        //   new AcceptorLogoGenerator(this.spliceHeightMatrix, this.framed);
        acceptorLogo.write(swriter, SvgConstants.Dimensions.SVG_Y_TOP_MARGIN);
        System.out.println("SVG_TREKKER_WALKER_START_Y: " + SVG_WALKER_START_Y);
        AbstractSvgMotifGenerator walker =
                new SvgSequenceWalker(this.ref, this.alt, this.splicesite, WIDTH, HEIGHT, SVG_WALKER_START_Y, this.framed);
        walker.write(swriter);
        // now show the Ri values
        double refR_i = splicesite.getIndividualSequenceInformation(this.ref);
        double altR_i = splicesite.getIndividualSequenceInformation(this.alt);
        writeRiChange(swriter, refR_i, altR_i, SVG_WALKER_START_Y+120);
    }
}
