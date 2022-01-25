package org.trypticon.pdn.paintdotnet.composite;

/**
 * Custom composite operation for Color Burn.
 *
 * Divides the inverted bottom layer by the top layer, and then inverts the result.
 */
public class ColorBurnComposite extends AbstractComposite {
    public static final ColorBurnComposite INSTANCE = new ColorBurnComposite();

    private ColorBurnComposite() {
    }

    @Override
    protected void mixPixels(int[] srcPixels, int[] dstInPixels, int[] dstOutPixels, int off) {
        int srcR = srcPixels[off];
        int srcG = srcPixels[off + 1];
        int srcB = srcPixels[off + 2];
        int srcA = srcPixels[off + 3];
        int dstInR = dstInPixels[off];
        int dstInG = dstInPixels[off + 1];
        int dstInB = dstInPixels[off + 2];
        int dstInA = dstInPixels[off + 3];

        // Blend logic here
        int dstOutR = invert(clampedDivide(invert(dstInR), srcR));
        int dstOutG = invert(clampedDivide(invert(dstInG), srcG));
        int dstOutB = invert(clampedDivide(invert(dstInB), srcB));
        int dstOutA = invert(clampedDivide(invert(dstInA), srcA));

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }
}
