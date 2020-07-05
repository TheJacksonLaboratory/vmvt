package org.monarchinitiative.vmvt.core.svg.fontprofile;

/**
 * SVGs use fonts provided by the Browser/Operating system, and it is relatively difficult to ensure that the
 * same font will be used across browsers and systems. However, it is important for displaying the logos etc.,
 * because different fonts have different letter heights, and vmvt uses empirical scalaing factors to scale
 * letters in the Logos and the Walkers. This class hierarchy represents all of the scaling factors that are
 * used across Vmvt, and thus helps to ensure that the SVGs will look good even if different fonts are used.
 * @author Peter N Robinson
 */
public abstract class FontProfile {
    /** This is a magic number that places the letters in the correct vertical position.
     * Needs to be adjusted together with the logo column height.
     * */
    public abstract double verticalScalingFactor();
    /** Maximum height of the letters in the sequence logo. Needs to be adjusted together with  the vertical scaling factor.*/
    public abstract double logoColumnHeight();
    /** The fonts that will be indicated in the SVG style sheet. */
    public abstract String fonts();
    /** We create a grey box surrounding the position of the mutation, and need a factor to calculate the proper height. */
    public abstract int variantBoxScalingFactor();
}
