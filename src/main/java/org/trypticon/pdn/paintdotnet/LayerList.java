package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.arrays.records.ArraySingleObject;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.LayerList}.
 * Actually extends .NET class ArrayList, but I figured Java has something like that, so let's use it.
 */
public class LayerList extends ArrayList<BitmapLayer> {
    private final Document parent;

    public LayerList(Document parent, NrbfClassRecord record) {
        this.parent = parent;

        Map<String, Object> map = record.asMap();
        int size = ((Number) map.get("ArrayList+_size")).intValue();
        List<Object> items = ((ArraySingleObject) map.get("ArrayList+_items")).getMembers();
        for (int i = 0; i < size; i++) {
            add(new BitmapLayer((NrbfClassRecord) items.get(i)));
        }
    }

    public Document getParent() {
        return parent;
    }
}
