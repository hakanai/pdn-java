package org.trypticon.pdn.nrbf.arrays.structs;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.4.2.1 ArrayInfo
 */
@SuppressWarnings("UnstableApiUsage")
public class ArrayInfo {
    private final INT32 objectId;
    private final INT32 length;

    public ArrayInfo(INT32 objectId, INT32 length) {
        this.objectId = objectId;
        this.length = length;
    }

    public int getObjectId() {
        return objectId.asInt();
    }

    public int getLength() {
        return length.asInt();
    }

    public static ArrayInfo readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 objectId = INT32.readFrom(stream);
        INT32 length = INT32.readFrom(stream);
        return new ArrayInfo(objectId, length);
    }
}
