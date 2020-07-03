======================================
Variant Motif Visualization Tool: vmvt
======================================



vmvt is a modern Java (version 11 and above) library for creating SVG plots to represent splice variants.
It can be used as a library or as a standalone app.

-------------
Quick Example
-------------

The following snippet will load the ``hp.obo`` file (which can be downloaded from the
`HPO website <https://hpo.jax.org/app/>`_) into an ``Ontology`` object. The HPO
has multiple subontologies, and the following code extracts all of the terms
of the `Phenotypic Abnormality <https://hpo.jax.org/app/browse/term/HP:0000118>`_ subontology.

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

.. toctree::
   :caption: Installation & Tutorial
   :name: tutorial
   :maxdepth: 1

    install
    input
    tutorial_io
    tutorial_hpo
    tutorial_go
    tutorial_similarity


.. toctree::
    :caption: Project Info
    :name: project-info
    :maxdepth: 1

    contributing
    authors
    history
    license
    release_howto
