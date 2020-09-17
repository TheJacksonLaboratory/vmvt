package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

public abstract class AbstractSvgMotifGenerator extends AbstractSvgGenerator {
    /** Representation of the Splice donor/acceptor IC matrix. */
    protected final DoubleMatrix splicesite;
    /** The reference (wildtype) sequence of the donor or acceptor splice site. */
    protected final String ref;
    /** The alternate (mutant) sequence of the donor or acceptor splice site. */
    protected final String alt;
    /** A coding of the String representing the reference sequence {@link #ref} using A=0,C=1,G=2,T=3. */
    protected final int [] refidx;
    /** A coding of the String representing the alternate sequence {@link #ref} using A=0,C=1,G=2,T=3. */
    protected final int [] altidx;
    /** The length in nucleotides of the splice site (9 for donor TODO for acceptor). */
    protected final int seqlen;





    /** Current X position on the SVG canvas. */
    protected int currentX;
    /** Current Y position on the SVG canvas. */
    protected int currentY;

    /**
     * We use this constructor for the SequenceRuler SVGs, in which we do not
     * need a splicesite representation. These subclasses do not need the
     * {@link #splicesite} object, so we set it to null. This is not extremely
     * elegant and we should refactor later.
     * @param ref The reference sequence
     * @param alt The alternate sequence
     * @param w width of the SVG canvas
     * @param h height of the SVG canvas
     */
    public AbstractSvgMotifGenerator(String ref, String alt,  int w, int h) {
        this(ref,alt,null, w, h);
    }

    public AbstractSvgMotifGenerator(String ref, String alt, DoubleMatrix splicesite, int w, int h) {
        super(w,h);
        this.ref = ref;
        this.alt = alt;
        this.splicesite = splicesite;
        if (ref.length() != alt.length()) {
            throw new VmvtRuntimeException(String.format("Reference sequence (%s-len=%d) and alternate sequence (%s-len=%d) have different lengths:",
                    ref, ref.length(), alt, alt.length()));
        }
        this.seqlen = ref.length();
        this.refidx = new int[seqlen];
        this.altidx = new int[seqlen];
        for (int i=0; i<seqlen; i++) {
            switch (ref.charAt(i)) {
                case 'a':
                case 'A':
                    refidx[i] = A_BASE;
                    break;
                case 'c':
                case 'C':
                    refidx[i] = C_BASE;
                    break;
                case 'g':
                case 'G':
                    refidx[i] = G_BASE;
                    break;
                case 't':
                case 'T':
                    refidx[i] = T_BASE;
                    break;
                default:
                    throw new VmvtRuntimeException(String.format("Bad nucleotide in ref (%s): Only ACGT/acgt allowed!",ref));
            }
        }
        for (int i=0; i<seqlen; i++) {
            switch (alt.charAt(i)) {
                case 'a':
                case 'A':
                    altidx[i] = A_BASE;
                    break;
                case 'c':
                case 'C':
                    altidx[i] = C_BASE;
                    break;
                case 'g':
                case 'G':
                    altidx[i] = G_BASE;
                    break;
                case 't':
                case 'T':
                    altidx[i] = T_BASE;
                    break;
                default:
                    throw new VmvtRuntimeException(String.format("Bad nucleotide in alt (%s): Only ACGT/acgt allowed!", alt));
            }
        }
    }

}
