package org.monarchinitiative.vmvt.core.svg.logo;

import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;

public class JasparLogoGenerator extends SvgSequenceLogo {
    /** X-dimension for each base of the motif. */
    private final static int WIDTH_PER_BASE = 20;

    public JasparLogoGenerator(DoubleMatrix site, int w, int h, boolean framed) {
        super(site, w, h, framed);
    }








    public static JasparLogoGenerator fromDoubleMatrix(DoubleMatrix site, boolean framed) {
        int len = site.getMotifLength();
        //  /** The width of the SVG canvas for all Donor figures. */
        //    protected final static int SVG_DONOR_WIDTH = 150;
        //    /** The width of the SVG canvas for all Acceptor figures. */
        //    protected final static int SVG_ACCEPTOR_WIDTH = 420;
        // donor 9, acceptor 27
        int width = len * WIDTH_PER_BASE;
        return new JasparLogoGenerator(site, width, SVG_LOGO_HEIGHT, framed);
    }


}
