package org.monarchinitiative.vmvt.core;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;

import static org.monarchinitiative.vmvt.core.svg.SvgConstants.Sequence.*;

public interface SvgInitializer {


    default int sequenceLength(String ref, String alt) {
        if (ref.length() != alt.length()) {
            throw new VmvtRuntimeException(String.format("Ref (%s) and alt (%d) need to have the same length", ref, alt));
        }
        int seqlen = ref.length();
        if (seqlen != DONOR_NT_LENGTH && seqlen != ACCEPTOR_NT_LENGTH) {
            throw new VmvtRuntimeException(String.format("sequence length (%d) did not conform to donor or acceptor", seqlen));
        }
        return seqlen;
    }


    default int[] sequenceIndex(String seq) {
        int seqlen = seq.length();
        int [] idx = new int[seqlen];
        for (int i=0; i<seqlen; i++) {
            switch (seq.charAt(i)) {
                case 'a':
                case 'A':
                    idx[i] = A_BASE;
                    break;
                case 'c':
                case 'C':
                    idx[i] = C_BASE;
                    break;
                case 'g':
                case 'G':
                    idx[i] = G_BASE;
                    break;
                case 't':
                case 'T':
                    idx[i] = T_BASE;
                    break;
                default:
                    throw new VmvtRuntimeException(String.format("Bad nucleotide in ref (%s): Only ACGT/acgt allowed!",seq));
            }
        }
        return idx;
    }


}
