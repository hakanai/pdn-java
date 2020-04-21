package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.BitmapLayer}.
 */
public class BitmapLayer extends Layer {
    private final BitmapLayerProperties properties;
    private final Surface surface;

    public BitmapLayer(NrbfClassRecord record) {
        super(record);
        Map<String, Object> map = record.asMap();
        properties = new BitmapLayerProperties((NrbfClassRecord) map.get("properties"));
        surface = new Surface((NrbfClassRecord) map.get("surface"));
    }

    public BitmapLayerProperties getBitmapProperties() {
        return properties;
    }

    public Surface getSurface() {
        return surface;
    }

    @Override
    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);

        int width = surface.getWidth();
        int stride = surface.getStride();
        ByteBuffer data = surface.getScan0().getData();

        int[] intArray = new int[width];
        for (int y = 0; y < 512; y++) {
            // TODO: Assuming BPP here, probably not ideal.
            IntBuffer intData = data.position(y * stride).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
            intData.get(intArray);
            image.setRGB(0, y, width, 1, intArray, 0, width);
        }

        return image;
    }

    public static class BitmapLayerProperties {
        private final UserBlendOps.NormalBlendOp blendOp;

        public BitmapLayerProperties(NrbfClassRecord record) {
            Map<String, Object> map = record.asMap();
            blendOp = new UserBlendOps.NormalBlendOp((NrbfClassRecord) map.get("blendOp"));
        }

        public UserBlendOps.NormalBlendOp getBlendOp() {
            return blendOp;
        }
    }
}
