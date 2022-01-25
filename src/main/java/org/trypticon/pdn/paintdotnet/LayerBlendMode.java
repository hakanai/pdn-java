package org.trypticon.pdn.paintdotnet;

import java.awt.*;
import java.util.Map;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;
import org.trypticon.pdn.paintdotnet.composite.MultiplyComposite;
import org.trypticon.pdn.paintdotnet.composite.XorComposite;

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

            case 1: // Multiply
                return MultiplyComposite.INSTANCE;

            case 13: // Xor
                return XorComposite.INSTANCE;

            default:
                throw new UnsupportedOperationException("Unsupported blend mode: " + getValue());
        }
    }
}
