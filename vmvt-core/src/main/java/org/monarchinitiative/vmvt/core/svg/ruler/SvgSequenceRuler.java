package org.monarchinitiative.vmvt.core.svg.ruler;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;

public abstract class SvgSequenceRuler extends AbstractSvgGenerator {

    protected final String reference;
    protected final String alternate;
    protected final int seqlen;

    public SvgSequenceRuler(int w, int h, String ref, String alt) {
        super(w, h);
        this.reference = ref;
        this.alternate = alt;
        int reflen = reference.length();
        int altlen = this.alternate.length();
        if (reflen != altlen) {
            throw new VmvtRuntimeException(String.format("Reference length (%d) and alternate length (%d) do not match",
                    reflen, altlen));
        }
        seqlen = reflen;
    }

    @Override
    public abstract String getSvg();
}
