package org.monarchinitiative.vmvt.core.svg.fontprofile;

/**
 * Cousine is a Google monospace font
 * https://fonts.google.com/specimen/Cousine?category=Monospace
 * This font is used bu Ubuntu systems as a default Monospace font, and is used to implement Courier New on Ubuntu Systems.
 * @author Peter N Robinson
 */
public class CousineProfile extends FontProfile {
    /** This is a magic number that places the letters in the correct vertical position. Works with {@link #LOGO_COLUMN_HEIGHT}.*/
    private final double VERTICAL_SCALING_FACTOR = 1.14;
    /** Maximum height of the letters in the sequence logo. Needs to be adjusted together with {@link #VERTICAL_SCALING_FACTOR}.*/
    private final double LOGO_COLUMN_HEIGHT = 20.0;

    private final int VARIANT_BOX_SCALING_FACTOR = 12;

    @Override
    public double verticalScalingFactor() {
        return VERTICAL_SCALING_FACTOR;
    }
    @Override
    public double logoColumnHeight() {
        return LOGO_COLUMN_HEIGHT;
    }

    @Override
    public String fonts() {
        return "Cousine, \"Courier new\", courier, monospace";
    }

    @Override
    public int variantBoxScalingFactor() {
        return VARIANT_BOX_SCALING_FACTOR;
    }


}
