package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgCoreGenerator;

import java.io.IOException;
import java.io.StringWriter;

public class DonorRuler  extends SvgSequenceRuler {

    public DonorRuler(String ref, String alt) {
        super(SVG_DONOR_WIDTH, SVG_RULER_HEIGHT, ref, alt);
        if (this.seqlen != DONOR_NT_LENGTH) {
            throw new VmvtRuntimeException(String.format("Sequence length must be %d for donor but was %d",
                    DONOR_NT_LENGTH, this.seqlen));
        }
    }

    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        int startY = SVG_RULER_STARTY;
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator posRuler = new PositionRuler(reference,
                    alternate,
                    WIDTH,
                    HEIGHT,
                    SVG_STARTX,
                    startY);
            posRuler.write(swriter);
            startY += posRuler.getYincrement();
            AbstractSvgCoreGenerator sequenceRuler = new SequenceRuler(reference,
                    alternate,
                    WIDTH,
                    HEIGHT,
                    SVG_STARTX,
                    startY);
            sequenceRuler.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
