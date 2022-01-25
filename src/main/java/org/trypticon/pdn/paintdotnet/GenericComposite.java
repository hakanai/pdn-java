package org.trypticon.pdn.paintdotnet;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * Generic implementation of image compositing logic.
 *
 * Originally adapted from the archived Paint.NET source code from when it was still
 * open source, but changed quite a bit to be more Java-friendly. (The original code
 * was almost entirely preprocessor macros.)
 */
public class GenericComposite implements Composite, CompositeContext {

    /**
     * "Over" composite. Essentially the same as {@link AlphaComposite#SrcOver}.
     */
    public static final GenericComposite OVER = new GenericComposite("Over", (a, b) -> b);

    /**
     * "Multiply" composite.
     */
    public static final GenericComposite MULTIPLY = new GenericComposite("Multiply", GenericComposite::intScale);

    /**
     * "Additive" composite.
     */
    public static final GenericComposite ADDITIVE = new GenericComposite("Additive", (a, b) -> Math.min(255, a + b));

    /**
     * "Color Burn" composite.
     */
    public static final GenericComposite COLOR_BURN = new GenericComposite("ColorBurn", (a, b) -> {
        if (b == 0) {
            return 0;
        } else {
            int r = (255 - a) * 255 / b;
            r = 255 - r;
            return Math.max(0, r);
        }
    });

    /**
     * "Color Dodge" composite.
     */
    public static final GenericComposite COLOR_DODGE = new GenericComposite("ColorDodge", (a, b) -> {
        if (b == 255) {
            return 255;
        } else {
            int r = a * 255 / (255 - b);
            return Math.min(255, r);
        }
    });

    private static final IntBinaryOperator REFLECT_FUNCTION = (a, b) -> {
        if (b == 255) {
            return 255;
        } else {
            int r = a * a / (255 - b);
            return Math.min(255, r);
        }
    };

    /**
     * "Reflect" composite.
     */
    public static final GenericComposite REFLECT = new GenericComposite("Reflect", REFLECT_FUNCTION);

    /**
     * "Glow" composite. The same as "Reflect" but with the layers switched.
     */
    public static final GenericComposite GLOW = new GenericComposite("Glow", (a, b) -> REFLECT_FUNCTION.applyAsInt(b, a));

    /**
     * "Overlay" composite.
     */
    public static final GenericComposite OVERLAY = new GenericComposite("Overlay", (a, b) -> {
        if (a < 128) {
            return intScale(2 * a, b);
        } else {
            int r = intScale(2 * (255 - a), 255 - b);
            return 255 - r;
        }
    });

    /**
     * "Difference" composite.
     */
    public static final GenericComposite DIFFERENCE = new GenericComposite("Difference", (a, b) -> Math.abs(b - a));

    /**
     * "Negation" composite.
     */
    public static final GenericComposite NEGATION = new GenericComposite("Negation", (a, b) -> (255 - Math.abs(255 - a - b)));

    /**
     * "Lighten" composite. Just takes the maximum of the two values.
     */
    public static final GenericComposite LIGHTEN = new GenericComposite("Lighten", Math::max);

    /**
     * "Darken" composite. Just takes the minimum of the two values.
     */
    public static final GenericComposite DARKEN = new GenericComposite("Darken", Math::min);

    /**
     * "Screen" composite.
     */
    public static final GenericComposite SCREEN = new GenericComposite("Screen", (a, b) -> {
        int r = intScale(b, a);
        return b + a - r;
    });

    /**
     * "XOR" composite.
     */
    public static final GenericComposite XOR = new GenericComposite("Xor", (a, b) -> a ^ b);

    /**
     * The name of the operation.
     */
    private final String name;

    /**
     * The actual function applied to the two values.
     */
    private final IntBinaryOperator f;

    /**
     * Function for loading the value for the destination-in layer.
     * (The layer on the bottom.)
     */
    private final IntUnaryOperator h;

    /**
     * Function for loading the value for the source layer.
     * (The layer on top.)
     */
    protected final IntUnaryOperator j;

    private GenericComposite(String name, IntBinaryOperator f) {
        this(name, f, IntUnaryOperator.identity(), IntUnaryOperator.identity());
    }

    private GenericComposite(String name, IntBinaryOperator f, IntUnaryOperator h, IntUnaryOperator j) {
        this.name = name;
        this.f = f;
        this.h = h;
        this.j = j;
    }

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
                blend(srcPixels, dstInPixels, dstOutPixels, x);
            }
            dstOut.setPixels(0, y, width, 1, dstOutPixels);
        }
    }

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

    /**
     * Performs blending for a single pixel.
     *
     * Takes the values from {@code srcPixels} and {@code dstInPixels}, performs
     * the blend operation, placing the result in {@code dstOutPixels}.
     * In all cases, the values used are at the given offset, in order R, G, B, A.
     *
     * @param srcPixels the array of source pixel values.
     * @param dstInPixels the array of destination-in pixel values.
     * @param dstOutPixels the array of destination-out pixel values.
     * @param off the offset into the arrays of the first (R) value.
     */
    private void blend(int[] srcPixels, int[] dstInPixels, int[] dstOutPixels, int off) {
        int srcR = srcPixels[off];
        int srcG = srcPixels[off + 1];
        int srcB = srcPixels[off + 2];
        int srcA = srcPixels[off + 3];
        int dstInR = dstInPixels[off];
        int dstInG = dstInPixels[off + 1];
        int dstInB = dstInPixels[off + 2];
        int dstInA = dstInPixels[off + 3];

        // Start of blending logic

        int lhsA = h.applyAsInt(dstInA);
        int rhsA = j.applyAsInt(srcA);
        int y = intScale(lhsA, 255 - rhsA);
        int totalA = y + rhsA;
        if (totalA == 0) {
            Arrays.fill(dstOutPixels, off, off + 4, 0);
            return;
        }

        int fB = f.applyAsInt(dstInB, srcB);
        int fG = f.applyAsInt(dstInG, srcG);
        int fR = f.applyAsInt(dstInR, srcR);

        int x = intScale(lhsA, rhsA);
        int z = rhsA - x;

        int dstOutR = (dstInR * y + srcR * z + fR * x) / totalA;
        int dstOutG = (dstInG * y + srcG * z + fG * x) / totalA;
        int dstOutB = (dstInB * y + srcB * z + fB * x) / totalA;
        int dstOutA = computeAlpha(lhsA, rhsA);

        // End of blending logic

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }

    /**
     * Computes summed alpha value.
     *
     * @param a the first alpha value.
     * @param b the second alpha value.
     * @return the result.
     */
    private static int computeAlpha(int a, int b) {
        int r = intScale(a, 255 - b);
        r += b;
        return r;
    }

    /**
     * Multiplies a and b, which are [0,255] as if they were scaled to [0,1].
     *
     * @param a the first value.
     * @param b the second value.
     * @return the result.
     */
    private static int intScale(int a, int b) {
        int r = a * b + 0x80;
        return ((r >> 8) + r) >> 8;
    }

    @Override
    public final CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return this;
    }

    @Override
    public final void dispose() {
    }

    /**
     * Creates a composite which uses the same composite operation,
     * but with the given opacity applied to the source layer.
     *
     * @param opacity the opacity.
     * @return the new composite.
     */
    public GenericComposite withOpacity(int opacity) {
        return new WithOpacity(name, f, h, j, opacity);
    }

    /**
     * Specialisation of a blend operation with opacity taken into account.
     */
    public static class WithOpacity extends GenericComposite {
        private final int opacity;

        public WithOpacity(String name,
                           IntBinaryOperator F,
                           IntUnaryOperator h,
                           IntUnaryOperator j,
                           int opacity) {
            super(name, F, h, a -> applyOpacity(a, j, opacity));
            this.opacity = opacity;
        }

        public int getOpacity() {
            return opacity;
        }

        private static int alphaWithOpacity(int a, int opacity) {
            return intScale(a, opacity);
        }

        private static int applyOpacity(int a, IntUnaryOperator j, int opacity) {
            int r = j.applyAsInt(a);
            r = alphaWithOpacity(r, opacity);
            return r;
        }
    }
}
