package org.monarchinitiative.vmvt.core.svg;

import org.monarchinitiative.vmvt.core.svg.fontprofile.CourierProfile;
import org.monarchinitiative.vmvt.core.svg.fontprofile.FontProfile;

/**
 * Constants used in multiple classses for all things SVG.
 */
public final class SvgConstants {

    /**
     * This controls what fonts are used in the application. Courier is safe on essentially all machines,
     * but other fonts might look better. To use another font, one would implement SvgHeaderFooter and
     * override the default writeHeader and use a different {@link FontProfile} implementation.
     */
    public final static class Fonts {
        private static final FontProfile fprofile = new CourierProfile();
        public static final double VERTICAL_SCALING_FACTOR = fprofile.verticalScalingFactor();
        public static final int VARIANT_BOX_SCALING_FACTOR = fprofile.variantBoxScalingFactor();
        public static final double LOGO_COLUMN_HEIGHT = fprofile.logoColumnHeight();
        public static final String SVG_FONTS = fprofile.fonts();
    }


    public final static class Vmvt {
        public final static String PROGRAM_NAME = "vmvt";
        public final static String PROGRAM_VERSION = "0.9.3";
    }

    /**
     * Named colors that we use in various classes for organizing SVG colors.
     */
    public final static class Colors {
        public final static String PURPLE = "#790079";
        public final static String GREEN = "#00A087";
        public final static String DARKGREEN = "#006600";
        public final static String RED = "#e64b35";
        public final static String BLACK = "#000000";
        public final static String NEARLYBLACK = "#040C04";

        public final static String BLUE = "#4dbbd5";

        public final static String BROWN = "#7e6148";
        public final static String DARKBLUE = "#3c5488";
        public final static String VIOLET = "#8491b4";
        public final static String ORANGE = "#ff9900";
        public final static String BRIGHT_GREEN = "#00a087";
        /** A green color for Adenine */
        public final static String A_COLOR = Colors.GREEN;
        /** A blue color for Cytosine */
        public final static String C_COLOR = Colors.BLUE;
        /** An orange color for Guanine */
        public final static String G_COLOR = Colors.ORANGE;
        /** A red color for Thymine */
        public final static String T_COLOR = Colors.RED;
    }

    /**
     * Default dimensions for various kinds of SVG objects.
     */
    public final static class Dimensions {
        /** The width of the SVG canvas for all Donor figures. */
        public final static int SVG_DONOR_WIDTH = 150;
        /** The width of the SVG canvas for all Acceptor figures. */
        public final static int SVG_ACCEPTOR_WIDTH = 420;
        public final static int SVG_BARCHART_HEIGHT = 250;

        /** X Position on the SVG canvas to start drawing. */
        public final static int SVG_STARTX = 10;

        public final static int LETTER_WIDTH = 10;
        /** Amount of horizontal space to be taken up by one base character. */
        public final static int LOWER_CASE_BASE_INCREMENT = LETTER_WIDTH + 5;

        /** Y offset from top of {@link SvgComponent} to start drawing the sequence logos. */
        public final static int SVG_LOGO_STARTY = 50;
        /** Height on the SVG canvas for Sequence logos. */
        public final static int SVG_LOGO_HEIGHT = 40;

        public final static int INTERCOMPONENT_VERTICAL_OFFSET = 30;

        public final static int SVG_Y_TOP_MARGIN = 5;
        public final static int SVG_Y_BOTTOM_MARGIN = 5;
        /** Height of a letter before scaling */
        public final static int LETTER_BASE_HEIGHT = 12;

        public final static int SVG_RULER_HEIGHT = 80;
        public final static int SVG_WALKER_HEIGHT = 130;

        /** Y position to start off the Walker in the Trekker layout. */
        public final static int TREKKER_WALKER_START_Y = 80;
        public final static int SVG_TREKKER_HEIGHT = 220;
        public final static int SVG_TREKKER_WITH_RI_HEIGHT = SVG_TREKKER_HEIGHT + 80;
        public final static int SVG_TREKKER_ACCEPTOR_WIDTH = SVG_ACCEPTOR_WIDTH + 10;
        public final static int SVG_TREKKER_DONOR_WIDTH = SVG_DONOR_WIDTH + 10;
        /** Width of the 'blue box', which is used as a background for the Ri text. */
        public static final int BLUE_BOX_WIDTH = 150;
        public static final int BLUE_BOX_HEIGHT = 40;

        public static final int ESE_SVG_WIDTH = 900;

        public static final int SVG_RI_BOX_HEIGHT = 50;

    }

    public final static class Sequence {
        public final static int DONOR_NT_LENGTH = 9;
        public final static int ACCEPTOR_NT_LENGTH = 27;
        public final static int A_BASE = 0;
        public final static int C_BASE = 1;
        public final static int G_BASE = 2;
        public final static int T_BASE = 3;
    }

    /**
     * This is used to keep track of whether we are drawing an acceptor/donor splice site
     * or a general JASPAR motif.
     */
    public enum MotifType {
        DONOR, ACCEPTOR, JASPAR;
    }
}
