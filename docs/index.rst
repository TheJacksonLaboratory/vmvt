.. _rstindex:

======================================
Variant Motif Visualization Tool: vmvt
======================================



vmvt is a modern Java (version 11 and above) library for creating SVG plots to represent splice variants.
It can be used as a library or as a standalone app.

-------------
Quick Example
-------------

The following snippet will create a splice donor Sequence Walker (for details, see :ref:`rstwalker`).

  .. code-block:: java

    import org.monarchinitiative.vmvt.core;

    VmvtGenerator vmvt = new VmvtGenerator();

    // create a Splice Donor sequence logo
    String logoSvg = vmvt.getDonorLogoSvg();

    // create a sequence walker showing the effects of a variant
    final String ref = "AAGGTCAGA";
    final String alt = "AAGATCAGA";
    String walkerSvg = vmvt.getDonorWalkerSvg(ref,alt);







--------
Feedback
--------

The best place to leave feedback, ask questions, and report bugs is the `vmvt Issue Tracker <https://github.com/TheJacksonLaboratory/vmvt/issues>`_.

..  toctree::
    :caption: Tutorial
    :name: tutorial
    :maxdepth: 1

    logo
    ruler
    walker
    trekker
    Exonic Splicing Enhancer Analysis<ese>
    delta
    Command Line App<vmvtcli>



.. toctree::
    :caption: Project Info
    :name: project-info
    :maxdepth: 1

    contributing
    authors
    history
    license
    build
