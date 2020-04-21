package org.trypticon.pdn;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class PdnTest {

    @Test
    public void testExample1() throws Exception {
        URL resource = Resources.getResource("org/trypticon/pdn/example1.pdn");
        Pdn pdn = Pdn.readFrom(Resources.asByteSource(resource));

        BufferedImage image = pdn.getDocument().toBufferedImage();
        Icon icon = new ImageIcon(image);
        System.out.println("How are we doing?");
    }
}
