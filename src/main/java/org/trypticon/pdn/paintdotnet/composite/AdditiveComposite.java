package org.trypticon.pdn.paintdotnet.composite;

/**
 * Custom composite operation for Multiply.
 *
 * This is still not 100% pixel perfect when compared to whatever is being done
 * by Paint.NET, but the result is imperceptible to the human eye.
 */
public class AdditiveComposite extends AbstractComposite {
    public static final AdditiveComposite INSTANCE = new AdditiveComposite();

    private AdditiveComposite() {
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
        int dstOutR = clamp(srcR + dstInR);
        int dstOutG = clamp(srcG + dstInG);
        int dstOutB = clamp(srcB + dstInB);
        int dstOutA = clamp(srcA + dstInA);

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }
}
