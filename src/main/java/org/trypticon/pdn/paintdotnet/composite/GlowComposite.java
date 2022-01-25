package org.trypticon.pdn.paintdotnet.composite;

/**
 * Custom composite operation for Glow.
 *
 * ???
 */
public class GlowComposite extends AbstractComposite {
    public static final GlowComposite INSTANCE = new GlowComposite();

    private GlowComposite() {
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
        int dstOutR = clampedDivide(srcR * srcR, invert(dstInR));
        int dstOutG = clampedDivide(srcG * srcG, invert(dstInG));
        int dstOutB = clampedDivide(srcB * srcB, invert(dstInB));
        int dstOutA = clampedDivide(srcA * srcA, invert(dstInA));

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }
}
