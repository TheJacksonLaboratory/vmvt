package org.monarchinitiative.vmvt.core.svg;

import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.core.VmvtGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SvgIcBarchartTest {
    private final String refDonor = "AAGGTCAGA";
    private final String altDonor = "AAGATCAGA";

    private final String refAcceptor = "cctggctggcggcaccgggtgccagGT";
    /** chr10-90768644-A-G, -2 position */
    private final String altAcceptor = "cctggctggcggcaccgggtgccggGT";


    @Test
    public void testDeltaDonor() {
        VmvtGenerator vmvt = new VmvtGenerator(true);
        String svg = vmvt.getDonorIcBars(refDonor, altDonor);

        try {
            String path = "target/donorIcBar.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeltaAcceptor() {
        VmvtGenerator vmvt = new VmvtGenerator(true);
        String svg = vmvt.getAcceptorIcBars(refAcceptor, altAcceptor);

        try {
            String path = "target/acceptorIcBar.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeltaRiDonor() {
        VmvtGenerator vmvt = new VmvtGenerator(true);
        String svg = vmvt.getDonorIcBarsWithRi(refDonor, altDonor);

        try {
            String path = "target/donorIcBarRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogoRiDonor() {
        VmvtGenerator vmvt = new VmvtGenerator(true);
        String svg = vmvt.getDonorIcBarsWithLogoAndRi(refDonor, altDonor);

        try {
            String path = "target/donorIcBarLogoRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeltaRiAcceptor() {
        VmvtGenerator vmvt = new VmvtGenerator(true);
        String svg = vmvt.getAcceptorIcBarsWithRi(refAcceptor, altAcceptor);

        try {
            String path = "target/acceptorIcBarRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogoRiAcceptor() {
        VmvtGenerator vmvt = new VmvtGenerator(true);
        String svg = vmvt.getAcceptorIcBarsWithRi(refAcceptor, altAcceptor);

        try {
            String path = "target/acceptorIcBarLogoRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
