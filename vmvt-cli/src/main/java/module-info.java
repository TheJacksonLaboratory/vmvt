module org.monarchinitiative.vmvt.cli {
    requires org.monarchinitiative.vmvt.core;
    requires info.picocli;

    // the following is required for
    // picocli and junit
    opens org.monarchinitiative.vmvt.cli;
}