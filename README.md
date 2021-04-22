# vmvt
Variant Motif Visualization Tool: Create SVG plots to represent splice variants


## Requirements
*Vmvt* is a Java 11 library with no external dependencies.

First, add *vmvt* into your project, i.e. add the following into the `pom.xml` of your Maven project:
```
<dependency>
    <groupId>org.monarchinitiative.vmvt</groupId>
    <artifactId>vmvt-core</artifactId>
    <version>0.9.4</version>
</dependency>
```

*Vmvt* can be used in both modular and non-modular Java applications.


## Usage

```java
// Sequence of a reference and alternate donor site
final String ref = "AAGGTCAGA";
final String alt = "AAGATCAGA";
VmvtGenerator vmvt = new VmvtGenerator();
String dist = vmvt.getDonorDistributionSvg(ref, alt);
String donor = vmvt.getDonorLogoSvg(ref,alt);
```



## The output
*Vmvt* produces SVG visualizations of the predicted effects of splice variants.
Here is an example variant at the +1 position of a donor sequence.

![donor vmvt image](docs/img/donorTrekker.svg "VMVT Donor Variant")





## Kick the tires

In the class ``org.monarchinitiative.vmvt.svg.DonorWriterTest``, two SVGs can be created
```
testWriteDonorSvgLogo() 
testWriteDonorSvgWalker() 
```
Running the test will create two local files in this directory, ``walkertest.svg`` and ``logotest.svg``
that can be viewed in a browser for convenience. There is an analogous test for acceptors.