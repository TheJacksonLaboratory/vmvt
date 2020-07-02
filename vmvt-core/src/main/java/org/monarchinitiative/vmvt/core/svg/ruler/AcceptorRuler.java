package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgCoreGenerator;

import java.io.IOException;
import java.io.StringWriter;

public class AcceptorRuler extends SvgSequenceRuler {

      public AcceptorRuler(String ref, String alt) {
        super(SVG_ACCEPTOR_WIDTH, SVG_RULER_HEIGHT, ref, alt);
        if (this.seqlen != ACCEPTOR_NT_LENGTH) {
            throw new VmvtRuntimeException(String.format("Sequence length must be %d for donor but was %d",
                    ACCEPTOR_NT_LENGTH, this.seqlen));
        }
    }

    @Override
    public String getSvg() {
        int startX = SVG_STARTX;
        int startY = SVG_RULER_STARTY;
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreGenerator posRuler = new PositionRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            posRuler.write(swriter);
            startY += posRuler.getYincrement();
            AbstractSvgCoreGenerator sequenceRuler = new SequenceRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            sequenceRuler.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
