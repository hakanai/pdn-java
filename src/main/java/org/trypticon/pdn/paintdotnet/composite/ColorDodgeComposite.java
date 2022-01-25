package org.trypticon.pdn.paintdotnet.composite;

/**
 * Custom composite operation for Color Dodge.
 *
 * Divides the bottom layer by the inverted top layer.
 */
public class ColorDodgeComposite extends AbstractComposite {
    public static final ColorDodgeComposite INSTANCE = new ColorDodgeComposite();

    private ColorDodgeComposite() {
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
        int dstOutR = clampedDivide255(dstInR, invert(srcR));
        int dstOutG = clampedDivide255(dstInG, invert(srcG));
        int dstOutB = clampedDivide255(dstInB, invert(srcB));
        int dstOutA = clampedDivide255(dstInA, invert(srcA));

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }
}
