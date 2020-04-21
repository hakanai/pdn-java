package org.trypticon.pdn;

import com.google.common.io.Resources;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("UnstableApiUsage")
public class BlendOpTest {
    @Test
    public void testNormal() throws Exception {
        commonTest("normal");
    }

    @Test
    public void testXor() throws Exception {
        commonTest("xor");
    }

    private void commonTest(String mode) throws Exception {
        URL pdnResource = Resources.getResource("org/trypticon/pdn/blendop-" + mode + ".pdn");
        Pdn pdn = Pdn.readFrom(Resources.asByteSource(pdnResource));
        BufferedImage pdnImage = pdn.getDocument().toBufferedImage();

        URL pngResource = Resources.getResource("org/trypticon/pdn/blendop-" + mode + ".png");
        BufferedImage pngImage = ImageIO.read(pngResource);

        assertThat(pdnImage.getWidth(), is(pngImage.getWidth()));
        assertThat(pdnImage.getHeight(), is(pngImage.getHeight()));

        for (int y = 0; y < pdnImage.getHeight(); y++) {
            for (int x = 0; x < pdnImage.getWidth(); x++) {
                assertThat(pdnImage.getRGB(x, y), is(pngImage.getRGB(x, y)));
            }
        }
    }
}
