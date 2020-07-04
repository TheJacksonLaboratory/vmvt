.. _rstcli:

===========================
vmvt command line interface
===========================

vmvt has a core module intended to be used as a programming library as well as a command line interface (in the
vmvt-cli module).  vmvt currently has four commands, which can be seen using the -h flag. ::

    $ java -jar vmvt-cli/target/vmvt-cli.jar  -h
    Usage: vmvt [-hV] [COMMAND]
        Variant-motif visualization tool.
        -h, --help      Show this help message and exit.
        -V, --version   Print version information and exit.
    Commands:
        logo, L    Create sequence logo
        ruler, R   Create sequence ruler
        walker, W  Create sequence ruler
        ese, E     Create ESE svg

Each of the commands has its own help menu, e.g., ::

    $ java -jar vmvt-cli/target/vmvt-cli.jar  logo -h
    Usage: vmvt logo [-adhV] [-o=<outname>]
    Create sequence logo
        -a, --acceptor
        -d, --donor
        -h, --help            Show this help message and exit.
        -o, --out=<outname>
        -V, --version         Print version information and exit.

