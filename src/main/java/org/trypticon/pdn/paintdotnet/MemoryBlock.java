package org.trypticon.pdn.paintdotnet;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Stand-in for Paint.NET class {@code PaintDotNet.MemoryBlock}.
 */
public class MemoryBlock {
    private final long length64;
    private final boolean hasParent;
    private final boolean deferred;

    private ByteBuffer data;

    public MemoryBlock(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        length64 = (long) map.get("length64");
        hasParent = (boolean) map.get("hasParent");
        deferred = (boolean) map.get("deferred");
    }

    public long getLength64() {
        return length64;
    }

    public boolean getHasParent() {
        return hasParent;
    }

    public boolean isDeferred() {
        return deferred;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }
}
