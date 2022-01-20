package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.awt.*;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Map;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.LayerBlendMode}.
 */
public class LayerBlendMode {
    private final int value;

    private static final int DEFAULT_BLEND_MODE = 0;

    public LayerBlendMode() {
        value = DEFAULT_BLEND_MODE;
    }

    public LayerBlendMode(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        value = (int) map.get("value__");
    }

    public int getValue() {
        return value;
    }

    /**
     * Creates a composite suitable for performing this blend operation.
     *
     * @return the composite.
     */
    public Composite createComposite() {
        switch (getValue()) {
            case 0: // Normal
                return AlphaComposite.SrcOver;

            case 13: // Xor
                return (srcColorModel, dstColorModel, hints) -> new CompositeContext() {
                    @Override
                    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
                        assert src.getWidth() == dstIn.getWidth();
                        assert src.getHeight() == dstIn.getHeight();
                        assert src.getWidth() == dstOut.getWidth();
                        assert src.getHeight() == dstOut.getHeight();

                        int width = src.getWidth();
                        int height = src.getHeight();
                        int[] srcPixels = null;
                        int[] dstInPixels = null;
                        for (int y = 0; y < height; y++) {
                            srcPixels = src.getPixels(0, y, width, 1, srcPixels);
                            dstInPixels = dstIn.getPixels(0, y, width, 1, dstInPixels);
                            for (int x = 0; x < srcPixels.length; x += 4) {
                                int srcR = srcPixels[x];
                                int srcG = srcPixels[x + 1];
                                int srcB = srcPixels[x + 2];
                                int srcA = srcPixels[x + 3];
                                int dstInR = dstInPixels[x];
                                int dstInG = dstInPixels[x + 1];
                                int dstInB = dstInPixels[x + 2];
                                int dstInA = dstInPixels[x + 3];

                                // Blend logic here
                                int dstOutR = dstInR ^ srcR;
                                int dstOutG = dstInG ^ srcG;
                                int dstOutB = dstInB ^ srcB;
                                int dstOutA = Math.min(255, srcA + dstInA - (srcA * dstInA) / 255);

                                dstInPixels[x] = dstOutR;
                                dstInPixels[x + 1] = dstOutG;
                                dstInPixels[x + 2] = dstOutB;
                                dstInPixels[x + 3] = dstOutA;
                            }
                            dstOut.setPixels(0, y, width, 1, dstInPixels);
                        }
                    }

                    @Override
                    public void dispose() {
                        // Nothing to do
                    }
                };

            default:
                throw new UnsupportedOperationException("Unsupported blend mode: " + getValue());
        }
    }
}
