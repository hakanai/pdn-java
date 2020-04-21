package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.49 UINT32
 */
@SuppressWarnings("UnstableApiUsage")
public class UINT32 implements PrimitiveType {
    private final int value;

    public UINT32(int value) {
        this.value = value;
    }

    public int asInt() {
        return value;
    }

    public static UINT32 readFrom(LittleEndianDataInputStream stream) throws IOException {
        int value = stream.readInt();
        return new UINT32(value);
    }

    @Override
    public UnsignedInteger toJavaValue() {
        return UnsignedInteger.fromIntBits(asInt());
    }

    @Override
    public String toString() {
        return Integer.toUnsignedString(asInt());
    }
}
