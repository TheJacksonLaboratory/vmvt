# svgwalker
SVG Walker: Create SVG plots to represent sequence walker plots.


## Requirements
SVG Walker is a Java 11 library. To use it, <TODO describe how to inlcude this in module-info etc.


## Usage

```$xslt
import org.jax.svgwalker.WalkerWriter;

// Sequence of a reference and alternate donor site
final String ref = "AAGGTCAGA";
final String alt = "AAGATCAGA";
WalkerWriter donor = new WalkerWriter();
String svg = donor.getDonorSvg(ref,alt);
```
