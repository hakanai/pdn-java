package org.trypticon.pdn.paintdotnet;

import java.awt.*;
import java.util.Map;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

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
            case 0: // Over (normal)
                return GenericComposite.OVER;

            case 1: // Multiply
                return GenericComposite.MULTIPLY;

            case 2: // Additive
                return GenericComposite.ADDITIVE;

            case 3: // Color Burn
                return GenericComposite.COLOR_BURN;

            case 4: // Color Dodge
                return GenericComposite.COLOR_DODGE;

            case 5: // Reflect
                return GenericComposite.REFLECT;

            case 6: // Glow
                return GenericComposite.GLOW;

            case 7: // Overlay
                return GenericComposite.OVERLAY;

            case 8: // Difference
                return GenericComposite.DIFFERENCE;

            case 9: // Negation
                return GenericComposite.NEGATION;

            case 10: // Lighten
                return GenericComposite.LIGHTEN;

            case 11: // Darken
                return GenericComposite.DARKEN;

            case 12: // Screen
                return GenericComposite.SCREEN;

            case 13: // Xor
                return GenericComposite.XOR;

            default:
                throw new UnsupportedOperationException("Unsupported blend mode: " + getValue());
        }
    }
}
