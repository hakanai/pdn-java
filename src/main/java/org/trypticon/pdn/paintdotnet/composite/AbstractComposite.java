package org.trypticon.pdn.paintdotnet.composite;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Convenience base class for implementing composites.
 */
abstract class AbstractComposite implements Composite, CompositeContext {
    @Override
    public final void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        checkRaster(src);
        checkRaster(dstIn);
        checkRaster(dstOut);
        checkRasterCompatibility(src, dstIn);
        checkRasterCompatibility(src, dstOut);

        int width = src.getWidth();
        int height = src.getHeight();
        int[] srcPixels = null;
        int[] dstInPixels = null;
        int[] dstOutPixels = null;
        for (int y = 0; y < height; y++) {
            srcPixels = src.getPixels(0, y, width, 1, srcPixels);
            dstInPixels = dstIn.getPixels(0, y, width, 1, dstInPixels);
            if (dstOutPixels == null) {
                dstOutPixels = new int[dstInPixels.length];
            }
            for (int x = 0; x < srcPixels.length; x += 4) {
                mixPixels(srcPixels, dstInPixels, dstOutPixels, x);
            }
            dstOut.setPixels(0, y, width, 1, dstOutPixels);
        }
    }

    /**
     * Implemented by subclasses to perform the mixing operation.
     *
     * Subclasses should read the pixels from {@code srcPixels} and {@code dstInPixels}
     * and place the result in {@code dstOutPixels}.
     *
     * @param srcPixels the array of source pixels.
     * @param dstInPixels the array of destination-in pixels.
     * @param dstOutPixels the array of destination-out pixels.
     * @param off the offset into the array of the red pixel. Green, blue, alpha
     *            pixels follow in the next offsets.
     */
    protected abstract void mixPixels(int[] srcPixels, int[] dstInPixels, int[] dstOutPixels, int off);

    private void checkRaster(Raster r) {
        if (r.getSampleModel().getDataType() != DataBuffer.TYPE_INT) {
            throw new IllegalArgumentException("Expected integer sample type");
        }
    }

    private void checkRasterCompatibility(Raster r1, Raster r2) {
        if (r1.getWidth() != r2.getWidth()) {
            throw new IllegalArgumentException("Expected raster widths " + r1.getWidth() +
                    " and " + r2.getWidth() + " to match");
        }
        if (r1.getHeight() != r2.getHeight()) {
            throw new IllegalArgumentException("Expected raster heights " + r1.getHeight() +
                    " and " + r2.getHeight() + " to match");
        }
    }


    @Override
    public final CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return this;
    }

    @Override
    public final void dispose() {
    }
}
