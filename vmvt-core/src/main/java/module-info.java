module org.monarchinitiative.vmvt.core {
    exports org.monarchinitiative.vmvt.core;

    //requires slf4j.api;

    opens org.monarchinitiative.vmvt.core;
    opens org.monarchinitiative.vmvt.core.pssm;
    opens org.monarchinitiative.vmvt.core.svg;
    opens org.monarchinitiative.vmvt.core.dist;
    opens org.monarchinitiative.vmvt.core.hexamer;
}

