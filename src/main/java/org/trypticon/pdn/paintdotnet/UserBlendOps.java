package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.UserBlendOps}.
 */
public class UserBlendOps {
    public static abstract class BlendOp {
    }

    public static class NormalBlendOp extends BlendOp {
        public NormalBlendOp(NrbfClassRecord record) {
            // no fields
        }
    }

    public static class MultiplyBlendOp extends BlendOp {
        public MultiplyBlendOp(NrbfClassRecord record) {
            // no fields
        }
    }

    public static class AdditiveBlendOp extends BlendOp {
        public AdditiveBlendOp(NrbfClassRecord record) {
            // no fields
        }
    }

    public static class ColorBurnBlendOp extends BlendOp {
        public ColorBurnBlendOp(NrbfClassRecord record) {
            // no fields
        }
    }

    public static class ColorDodgeBlendOp extends BlendOp {
        public ColorDodgeBlendOp(NrbfClassRecord record) {
            // no fields
        }
    }

    public static class XorBlendOp extends BlendOp {
        public XorBlendOp(NrbfClassRecord record) {
            // no fields
        }
    }
}
