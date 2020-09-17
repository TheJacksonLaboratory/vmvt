package org.monarchinitiative.vmvt.core.svg.walker;

import org.monarchinitiative.vmvt.core.except.VmvtRuntimeException;
import org.monarchinitiative.vmvt.core.pssm.DoubleMatrix;
import org.monarchinitiative.vmvt.core.svg.AbstractSvgGenerator;
import org.monarchinitiative.vmvt.core.svg.SvgColors;

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

    private static final int BLUE_BOX_WIDTH = 120;
    private static final int BLUE_BOX_HEIGHT = 50;
    private static final int Y_INCREMENT = 20; // add beneath text box

    private final int walkerWidth;
    private final int middle;
    /** Position to start the blue box so that it is in the middle. */
    private final int blueBoxStart;

    private final String canonical;
    private final String cryptic;
    private final DoubleMatrix splicesite;
    private final boolean isDonor;

    private SvgCanonicalCrypticGenerator(int w, int h, String can, String crypt, DoubleMatrix site) {
        super(w+EXTRA_X_FOR_RI, h);
        this.walkerWidth = w;
        middle = w/2;
        blueBoxStart = middle - BLUE_BOX_WIDTH/2;
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


    private void writeRi(Writer writer, double R_i, String category, int y) throws IOException {
        int startx = blueBoxStart + 10;
        int y_increment = 20;
        int texty = y;
        int blueBoxFudge = 20; // move back up by this amount to be in the right place
        int x_increment = 15;

        String blueRect = String.format("<rect x=\"%d\" y=\"%d\" rx=\"3\" ry=\"3\" width=\"%d\" height=\"%d\" style=\"stroke: none; fill: %s;fill-opacity: 0.1\"></rect>",
                  blueBoxStart,
                  y-blueBoxFudge,
                  BLUE_BOX_WIDTH,
                BLUE_BOX_HEIGHT,
                SvgColors.BLUE);
       writer.write(blueRect);

        String RiString = String.format("<text x=\"%d\" y=\"%d\" class=\"t20\">R" +
                        "<tspan dy=\"1\" font-size=\"12\">i</tspan></text>\n" +
                        "<text x=\"%d\" y=\"%d\" class=\"t20\">:%.2f</text>\n",
                startx+x_increment,texty,startx+18+x_increment,texty,R_i);
        //  String eseText = String.format("<text x='50%%' y='57' text-anchor='middle'>ΔESE: %.2f</text>", deltaESE);
        writer.write(RiString);
        String categoryString = String.format("<text x=\"%d\" y=\"%d\" class=\"t20\">\n" +category +"</text>\n",
                startx,y+y_increment);
        writer.write(categoryString);
    }

    private void writeDonor(Writer writer) throws IOException {
        // Note the API of the WalkerGenerators requires ref and alt but for
        // now we just want to show one sequence that we treat as ref
        int ystart = AbstractSvgGenerator.SVG_CANCRYPT_STARTY;
        double R_i = splicesite.getIndividualSequenceInformation(this.canonical);
        writeRi(writer, R_i, "canonical", ystart);
        ystart += BLUE_BOX_HEIGHT + Y_INCREMENT;
        SvgSequenceWalker walker = SvgSequenceWalker.singleDonorWalker(this.canonical, this.splicesite, ystart);
        walker.writeRefWalker(writer);
        R_i = splicesite.getIndividualSequenceInformation(this.cryptic);
        ystart += 80;
        writeRi(writer, R_i, "cryptic", ystart);
        ystart += BLUE_BOX_HEIGHT + Y_INCREMENT;
        walker = SvgSequenceWalker.singleDonorWalker(this.cryptic, this.splicesite, ystart);
        walker.writeRefWalker(writer);
    }


    private void writeAcceptor(Writer writer) throws IOException {
        // Note the API of the WalkerGenerators requires ref and alt but for
        // now we just want to show one sequence that we treat as ref
        int ystart = AbstractSvgGenerator.SVG_CANCRYPT_STARTY;
        double R_i = splicesite.getIndividualSequenceInformation(this.canonical);
        writeRi(writer, R_i, "canonical", ystart);
        ystart += BLUE_BOX_HEIGHT + Y_INCREMENT;
        SvgSequenceWalker walker = SvgSequenceWalker.singleAcceptorWalker(this.canonical, this.splicesite, ystart);
        walker.writeRefWalker(writer);
        ystart += 80;
        R_i = splicesite.getIndividualSequenceInformation(this.cryptic);
        writeRi(writer, R_i, "cryptic", ystart);
        ystart += BLUE_BOX_HEIGHT + Y_INCREMENT;
        walker = SvgSequenceWalker.singleAcceptorWalker(this.cryptic, this.splicesite, ystart);
        walker.writeRefWalker(writer);

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
        return new SvgCanonicalCrypticGenerator(SVG_DONOR_WIDTH, SVG_CANONICAL_CRYPTIC_HEIGHT, can, crypt,donor);
    }

    public static SvgCanonicalCrypticGenerator acceptor(String can, String crypt, DoubleMatrix acceptor) {
        return new SvgCanonicalCrypticGenerator(SVG_ACCEPTOR_WIDTH, SVG_CANONICAL_CRYPTIC_HEIGHT, can, crypt, acceptor);
    }




}
