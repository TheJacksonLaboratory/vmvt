package org.monarchinitiative.vmvt.core.svg.trek;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgMotifGenerator;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.AcceptorLogoGenerator;
import org.monarchinitiative.vmvt.core.svg.logo.SvgSequenceLogo;
import org.monarchinitiative.vmvt.core.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * This class creates an SVG graphic that contains a sequence ruler (intron/exon positions), the
 * reference sequence, deviating alternate bases, a box around the variant, the sequence logo,
 * and a sequence walker. This class is for the acceptor.
 * @author Peter N Robinson
 */
public class AcceptorTrekkerGenerator extends AbstractSvgGenerator {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;
    private final boolean framed;

    public AcceptorTrekkerGenerator(String ref, String alt, boolean framed) {
        super(SVG_TREKKER_ACCEPTOR_WIDTH ,SVG_TREKKER_HEIGHT, framed);
        this.framed = framed;
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.acceptor();
        this.spliceHeightMatrix = DoubleMatrix.acceptorHeightMatrix();
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
        SvgSequenceLogo acceptorLogo =
                new AcceptorLogoGenerator(this.spliceHeightMatrix, this.framed);
        acceptorLogo.write(swriter);
        AbstractSvgMotifGenerator acceptorWalker =
                new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT,TREKKER_WALKER_START_Y, this.framed);
        acceptorWalker.write(swriter);
    }
}
