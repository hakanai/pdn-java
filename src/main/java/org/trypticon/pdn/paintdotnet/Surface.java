package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.util.Map;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.Surface}.
 */
public class Surface {
    private final int width;
    private final int height;
    private final int stride;
    private final MemoryBlock scan0;

    public Surface(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        width = (int) map.get("width");
        height = (int) map.get("height");
        stride = (int) map.get("stride");
        scan0 = new MemoryBlock((NrbfClassRecord) map.get("scan0"));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStride() {
        return stride;
    }

    public MemoryBlock getScan0() {
        return scan0;
    }
}
