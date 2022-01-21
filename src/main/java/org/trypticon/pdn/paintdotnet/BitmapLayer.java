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
        int width = surface.getWidth();
        int height = surface.getHeight();
        int stride = surface.getStride();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        ByteBuffer data = surface.getScan0().getData();

        int[] intArray = new int[width];
        for (int y = 0; y < height; y++) {
            // TODO: Assuming BPP here, probably not ideal.
            IntBuffer intData = data.position(y * stride).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
            intData.get(intArray);
            image.setRGB(0, y, width, 1, intArray, 0, width);
        }

        return image;
    }

    public static class BitmapLayerProperties {
        private final UserBlendOps.BlendOp blendOp;

        public BitmapLayerProperties(NrbfClassRecord record) {
            Map<String, Object> map = record.asMap();
            NrbfClassRecord blendOpRecord = (NrbfClassRecord) map.get("blendOp");

            // XXX: A factory pattern should be put in place here.
            switch (blendOpRecord.getMetadata().getName()) {
                case "PaintDotNet.UserBlendOps+NormalBlendOp":
                    blendOp = new UserBlendOps.NormalBlendOp(blendOpRecord);
                    break;
                case "PaintDotNet.UserBlendOps+XorBlendOp":
                    blendOp = new UserBlendOps.XorBlendOp(blendOpRecord);
                    break;
                default:
                    blendOp = new UserBlendOps.NormalBlendOp(blendOpRecord);
                    break;
            }
        }

        public UserBlendOps.BlendOp getBlendOp() {
            return blendOp;
        }
    }
}
