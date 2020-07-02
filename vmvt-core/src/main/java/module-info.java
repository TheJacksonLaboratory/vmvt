module org.monarchinitiative.vmvt {
    requires slf4j.api;
    requires com.google.common;
    exports org.monarchinitiative.vmvt;

    opens org.monarchinitiative.vmvt;
    opens org.monarchinitiative.vmvt.pssm;
    opens org.monarchinitiative.vmvt.svg;
}

