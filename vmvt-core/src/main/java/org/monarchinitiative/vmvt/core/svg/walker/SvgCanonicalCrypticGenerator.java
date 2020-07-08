package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Generate an SVG graphic that shows the canonical splice site as well as the cryptic splice site together with
 * their individual information content R_i
 * @author Peter N Robinson
 */
public class SvgCanonicalCrypticGenerator extends AbstractSvgGenerator {

    private static final int  SVG_CANONICAL_CRYPTIC_HEIGHT = 340;
    /** Add some extra width to write the R_i of the sequences. */
    private static final int EXTRA_X_FOR_RI = 140;
    private final int walkerWidth;

    private final String canonical;
    private final String cryptic;
    private final DoubleMatrix splicesite;
    private final boolean isDonor;

    private SvgCanonicalCrypticGenerator(int w, int h, String can, String crypt, DoubleMatrix site) {
        super(w+EXTRA_X_FOR_RI, h);
        this.walkerWidth = w;
        this.canonical = can;
        this.cryptic = crypt;
        this.splicesite = site;
        if (site.getMotifLength() == DONOR_NT_LENGTH) {
            isDonor = true;
        } else if (site.getMotifLength() == ACCEPTOR_NT_LENGTH) {
            isDonor = false;
        } else {
            throw new VmvtRuntimeException("Invalid splice site length (not 9 or 27):" + site.getMotifLength());
        }
    }


    private void writeDonor(Writer writer) throws IOException {
        // Note the API of the WalkerGenerators requires ref and alt but for
        // now we just want to show one sequence that we treat as ref
        // todo -- refactor
        int ystart = AbstractSvgGenerator.SVG_WALKER_STARTY;
        SvgSequenceWalker walker = SvgSequenceWalker.singleDonorWalker(this.canonical, this.splicesite, ystart);
        walker.writeRefWalker(writer);
        ystart += SVG_WALKER_HEIGHT;
        walker = SvgSequenceWalker.singleDonorWalker(this.cryptic, this.splicesite, ystart);
        walker.writeRefWalker(writer);
    }

    private void writeRi(Writer writer, double R_i, String category, int y) throws IOException {
        int startx = walkerWidth + 10;
        int y_increment = 20;
        String RiString = String.format("<text x=\"%d\" y=\"%d\" font-size=\"16\">\n" +
                        "R" +
                        "<tspan dy=\"1\" font-size=\"12\">i</tspan></text>\n" +
                        "<text x=\"%d\" y=\"%d\">: %.2f</text>\n",
                startx,y,startx+20,y,R_i);
        writer.write(RiString);
        String categoryString = String.format("<text x=\"%d\" y=\"%d\" font-size=\"12\">\n" +category +"</text>\n",
                startx,y+y_increment);
        writer.write(categoryString);

    }


    private void writeAcceptor(Writer writer) throws IOException {
        // Note the API of the WalkerGenerators requires ref and alt but for
        // now we just want to show one sequence that we treat as ref
        // todo -- refactor
        int ystart = AbstractSvgGenerator.SVG_WALKER_STARTY;
        SvgSequenceWalker walker = SvgSequenceWalker.singleAcceptorWalker(this.canonical, this.splicesite, ystart);
        walker.writeRefWalker(writer);
        double R_i = splicesite.getIndividualSequenceInformation(this.canonical);
        writeRi(writer, R_i, "canonical", ystart);
        ystart += SVG_WALKER_HEIGHT/2;
        walker.writeRefAltSeparation(writer, ystart);
        ystart += SVG_WALKER_HEIGHT/2;
        walker = SvgSequenceWalker.singleAcceptorWalker(this.cryptic, this.splicesite, ystart);
        walker.writeRefWalker(writer);
        R_i = splicesite.getIndividualSequenceInformation(this.cryptic);
        writeRi(writer, R_i, "cryptic", ystart);
    }


    @Override
    public String getSvg() {
        StringWriter swriter = new StringWriter();
        try {
            writeHeader(swriter);
            write(swriter);
            writeFooter(swriter);
            return swriter.toString();
        } catch (IOException e) {
            return getSvgErrorMessage(e.getMessage());
        }
    }

    @Override
    public void write(Writer writer) throws IOException {
        if (isDonor) {
            writeDonor(writer);
        } else {
            writeAcceptor(writer);
        }
    }


    public static SvgCanonicalCrypticGenerator donor(String can, String crypt, DoubleMatrix donor) {
        SvgCanonicalCrypticGenerator generator =
                new SvgCanonicalCrypticGenerator(SVG_DONOR_WIDTH, SVG_CANONICAL_CRYPTIC_HEIGHT, can, crypt,donor);
        return generator;
    }

    public static SvgCanonicalCrypticGenerator acceptor(String can, String crypt, DoubleMatrix acceptor) {
        return new SvgCanonicalCrypticGenerator(SVG_ACCEPTOR_WIDTH, SVG_CANONICAL_CRYPTIC_HEIGHT, can, crypt, acceptor);
    }




}
