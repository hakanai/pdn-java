package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.22 INT32
 */
@SuppressWarnings("UnstableApiUsage")
public class INT32 implements PrimitiveType {
    private final int value;

    public INT32(int value) {
        this.value = value;
    }

    public int asInt() {
        return value;
    }

    public static INT32 readFrom(LittleEndianDataInputStream stream) throws IOException {
        int value = stream.readInt();
        return new INT32(value);
    }

    @Override
    public Integer toJavaValue() {
        return asInt();
    }

    @Override
    public String toString() {
        return Integer.toString(asInt());
    }
}
