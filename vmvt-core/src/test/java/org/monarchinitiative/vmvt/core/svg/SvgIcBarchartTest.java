package org.monarchinitiative.vmvt.core.svg;

import org.junit.jupiter.api.Test;
import org.monarchinitiative.vmvt.core.VmvtGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SvgIcBarchartTest {
    private final String ref = "AAGGTCAGA";
    private final String alt = "AAGATCAGA";


    @Test
    public void testDeltaDonor() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDonorIcBars(ref, alt);

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
    public void testDeltaRiDonor() {
        VmvtGenerator vmvt = new VmvtGenerator();
        String svg = vmvt.getDonorIcBarsWithRi(ref, alt);

        try {
            String path = "target/donorIcBarRi.svg";
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(svg);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
