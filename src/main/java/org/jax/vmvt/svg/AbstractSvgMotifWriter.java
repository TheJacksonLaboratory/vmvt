package org.jax.vmvt.svg;

import org.jax.vmvt.pssm.DoubleMatrix;


public abstract class AbstractSvgMotifWriter extends AbstractSvgCoreWriter {

    /** Representation of the Splice donor/acceptor IC matrix. */
    protected final DoubleMatrix splicesite;



    public AbstractSvgMotifWriter(String ref, String alt, DoubleMatrix site, int w, int h) {
       super(ref,alt, w, h);
        this.splicesite = site;
    }







}
