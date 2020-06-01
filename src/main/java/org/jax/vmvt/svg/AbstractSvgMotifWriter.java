package org.jax.vmvt.svg;

import org.jax.vmvt.pssm.DoubleMatrix;

/**
 * This is the base class for writing an SVG string for elements that have a splice site motif (information
 * content or height).
 * @author Peter Robinson
 */
public abstract class AbstractSvgMotifWriter extends AbstractSvgCoreWriter {

    /** Representation of the Splice donor/acceptor IC matrix. */
    protected final DoubleMatrix splicesite;



    public AbstractSvgMotifWriter(String ref, String alt, DoubleMatrix site, int w, int h) {
       super(ref,alt, w, h);
        this.splicesite = site;
    }







}
