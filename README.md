# vmvt
Variant Motif Visualization Tool: Create SVG plots to represent splice variants


## Requirements
SVG Walker is a Java 11 library. To use it, <TODO describe how to inlcude this in module-info etc.


## Usage

```$xslt
import org.jax.vmvt.VmvtWriter;

// Sequence of a reference and alternate donor site
final String ref = "AAGGTCAGA";
final String alt = "AAGATCAGA";
VmvtWriter donor = new VmvtWriter();
String svg = donor.getDonorLogoSvg(ref,alt);
```



## The output
vmvt produces SVG visualizations of the predicted effects of splice variants.
Here is an example variant at the +1 position of a donor sequence.

![donor vmvt image](https://github.com/TheJacksonLaboratory/vmvt/blob/master/docs/vmvt-donor.png "VMVT Donor Variant")





## Kick the tires

In the class ``org.jax.vmvt.svg.DonorWriterTest``, two SVGs can be created
```
testWriteDonorSvgLogo() 
testWriteDonorSvgWalker() 
```
Running the test will create two local files in this directory, ``walkertest.svg`` and ``logotest.svg``
that can be viewed in a browser for convenience. There is an analogous test for acceptors.