package org.trypticon.pdn.paintdotnet.composite;

/**
 * Custom composite operation for Multiply.
 *
 * This is still not 100% pixel perfect when compared to whatever is being done
 * by Paint.NET, but the result is imperceptible to the human eye.
 */
public class MultiplyComposite extends AbstractComposite {
    public static final MultiplyComposite INSTANCE = new MultiplyComposite();

    private MultiplyComposite() {
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
        int dstOutR = (srcR * dstInR) / 255;
        int dstOutG = (srcG * dstInG) / 255;
        int dstOutB = (srcB * dstInB) / 255;
        int dstOutA = Math.min(255, srcA + dstInA);

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }
}
