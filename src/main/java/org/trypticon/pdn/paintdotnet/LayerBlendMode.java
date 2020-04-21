package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.util.Map;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.LayerBlendMode}.
 */
public class LayerBlendMode {
    private final int value;

    public LayerBlendMode(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        value = (int) map.get("value__");
    }

    public int getValue() {
        return value;
    }
}
