package org.trypticon.pdn.paintdotnet;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

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
        private static final ImmutableMap<String, BlendOpFactory> factoryMap = ImmutableMap
                .<String, BlendOpFactory>builder()
                .put("PaintDotNet.UserBlendOps+NormalBlendOp", UserBlendOps.NormalBlendOp::new)
                .put("PaintDotNet.UserBlendOps+MultiplyBlendOp", UserBlendOps.MultiplyBlendOp::new)
                .put("PaintDotNet.UserBlendOps+AdditiveBlendOp", UserBlendOps.AdditiveBlendOp::new)
                .put("PaintDotNet.UserBlendOps+ColorBurnBlendOp", UserBlendOps.ColorBurnBlendOp::new)
                .put("PaintDotNet.UserBlendOps+ColorDodgeBlendOp", UserBlendOps.ColorDodgeBlendOp::new)
                .put("PaintDotNet.UserBlendOps+ReflectBlendOp", UserBlendOps.ReflectBlendOp::new)
                .put("PaintDotNet.UserBlendOps+GlowBlendOp", UserBlendOps.GlowBlendOp::new)
                .put("PaintDotNet.UserBlendOps+XorBlendOp", UserBlendOps.XorBlendOp::new)
                .build();

        private final UserBlendOps.BlendOp blendOp;

        public BitmapLayerProperties(NrbfClassRecord record) {
            Map<String, Object> map = record.asMap();
            NrbfClassRecord blendOpRecord = (NrbfClassRecord) map.get("blendOp");

            String blendOpName = blendOpRecord.getMetadata().getName();
            BlendOpFactory blendOpFactory = factoryMap.get(blendOpName);
            if (blendOpFactory == null) {
                throw new UnsupportedOperationException("Unsupported blend op: " + blendOpName);
            }
            blendOp = blendOpFactory.create(blendOpRecord);
        }

        public UserBlendOps.BlendOp getBlendOp() {
            return blendOp;
        }
    }

    private interface BlendOpFactory {
        UserBlendOps.BlendOp create(NrbfClassRecord blendOpRecord);
    }
}
