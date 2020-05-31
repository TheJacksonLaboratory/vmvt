package org.jax.vmvt.svg.combo;

import org.jax.vmvt.VmtVisualizer;
import org.jax.vmvt.pssm.DoubleMatrix;
import org.jax.vmvt.svg.AbstractSvgCoreWriter;
import org.jax.vmvt.svg.AbstractSvgWriter;
import org.jax.vmvt.svg.logo.SvgSequenceLogo;
import org.jax.vmvt.svg.ruler.PositionRuler;
import org.jax.vmvt.svg.ruler.SequenceRuler;
import org.jax.vmvt.svg.walker.SvgSequenceWalker;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Display a splice donor variant with both a sequence walker and a logo.
 */
public class DonorVmvtWriter extends AbstractSvgWriter implements VmtVisualizer {
    private final String reference;
    private final String alternate;
    private final DoubleMatrix splicesite;
    private final DoubleMatrix spliceHeightMatrix;

    public DonorVmvtWriter(String ref, String alt) {
        super(400,400);
        this.reference = ref;
        this.alternate = alt;
        this.splicesite = DoubleMatrix.donor();
        this.spliceHeightMatrix = DoubleMatrix.donorHeightMatrix();
    }

    @Override
    public String getSvg() {
        int startX = 20;
        int startY = 60;

        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            // WIDTH AND HEIGHT ARE FROM THE SUPERCLASS -- SET ABOVE IN THE CTOR
            AbstractSvgCoreWriter posRuler = new PositionRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            posRuler.write(swriter);
            startY += posRuler.getYincrement();
            AbstractSvgCoreWriter sequenceRuler = new SequenceRuler(reference, alternate,WIDTH, HEIGHT, startX, startY);
            sequenceRuler.write(swriter);
            startY += sequenceRuler.getYincrement();
            AbstractSvgCoreWriter donorLogo =
                    new SvgSequenceLogo(reference, alternate, this.spliceHeightMatrix, WIDTH, HEIGHT, startX, startY);
            donorLogo.write(swriter);
            startY += donorLogo.getYincrement();
            AbstractSvgCoreWriter donorWalker =
                    new SvgSequenceWalker(reference, alternate, this.splicesite, WIDTH, HEIGHT, startX, startY);
            donorWalker.write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }
}
