package org.monarchinitiative.vmvt.svg;

import org.monarchinitiative.vmvt.pssm.DoubleMatrix;

/**
 * This is the base class for writing an SVG string for elements that have a splice site motif (information
 * content or height).
 * @author Peter Robinson
 */
public abstract class AbstractSvgMotifGenerator extends AbstractSvgCoreGenerator {

    /** Representation of the Splice donor/acceptor IC matrix. */
    protected final DoubleMatrix splicesite;



    public AbstractSvgMotifGenerator(String ref, String alt, DoubleMatrix site, int w, int h) {
       super(ref,alt, w, h);
        this.splicesite = site;
    }







}
