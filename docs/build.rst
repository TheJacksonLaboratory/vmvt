.. _rstbuild:
=============
Building vmvt
=============

The vmvt `GitHub repository <https://github.com/TheJacksonLaboratory/vmvt>`_ contains
two modules (core and cli). The core module is used for the actual logic, and the
cli module provides a simple command line interface. Both modules can be built using
maven, as usual. The following command builds the library and the app and displays
a help message on the shell. ::

    git clone https://github.com/TheJacksonLaboratory/vmvt
    cd vmvt
    java -jar vmvt-cli/target/vmvt.jar -h

Using jpackage
~~~~~~~~~~~~~~

vmvt is a Java 11 app. We are experimenting with jpackage to build standalone command-line interface
applications. For this, we need to download the jpackage app for the OS for which we desire to create
the standalone app. Gluon has made these packages available for Java 11.

* `linux <http://download2.gluonhq.com/jpackager/11/jdk.packager-linux.zip>`_
* `Mac <http://download2.gluonhq.com/jpackager/11/jdk.packager-osx.zip>`_
* `Windows <http://download2.gluonhq.com/jpackager/11/jdk.packager-windows.zip>`_

Creating an installer for Mac
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

We will demonstrate the process for the Mac. Note that we are assuming that you have Java 11
and Java 14 installed on
your machine.

First, set ``JAVA_HOME``  (temporarily) to the value for Java 14. ::

    export JAVA_HOME=`/usr/libexec/java_home -v 14`

You can check that the above command has worked by entering ``echo $JAVA_HOME``, which should reveal the
correct path (e.g., ``/Library/Java/JavaVirtualMachines/jdk-14.0.1.jdk/Contents/Home``).

We can now run jpackage as follows (adjust the path to the downloaded jpackage executable as needed). First
change directory into the ``vmvt-cli`` subdirectory and then execute this command. ::

    /Library/Java/JavaVirtualMachines/jdk-14.0.1.jdk/Contents/Home/bin/jpackage \
        --input target/ \
        --name VmvtCli \
        --main-jar target/vmvt-cli.jar \
        --main-class org.monarchinitiative.vmvt.cli.Main \
        --type dmg

<Note: to do not working yet!>
