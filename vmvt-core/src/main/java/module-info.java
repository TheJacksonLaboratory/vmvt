module org.monarchinitiative.vmvtcore {
    exports org.monarchinitiative.vmvtcore;

    requires slf4j.api;
    requires com.google.common;

    opens org.monarchinitiative.vmvt.core;
    opens org.monarchinitiative.vmvt.core.pssm;
    opens org.monarchinitiative.vmvt.core.svg;
    opens org.monarchinitiative.vmvt.core.dist;
    opens org.monarchinitiative.vmvt.core.hexamer;
}

