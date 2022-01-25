package org.trypticon.pdn.paintdotnet.composite;

import java.awt.AlphaComposite;

/**
 * Custom composite operation for XOR.
 *
 * Because {@link AlphaComposite#Xor} somehow gives the wrong result compared to Paint.NET.
 */
public class XorComposite extends AbstractComposite {
    public static final XorComposite INSTANCE = new XorComposite();

    private XorComposite() {
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
        int dstOutR = dstInR ^ srcR;
        int dstOutG = dstInG ^ srcG;
        int dstOutB = dstInB ^ srcB;
        int dstOutA = Math.min(255, srcA + dstInA - (srcA * dstInA) / 255);

        dstOutPixels[off] = dstOutR;
        dstOutPixels[off + 1] = dstOutG;
        dstOutPixels[off + 2] = dstOutB;
        dstOutPixels[off + 3] = dstOutA;
    }
}
