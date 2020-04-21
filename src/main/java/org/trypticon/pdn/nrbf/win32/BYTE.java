package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.6 BYTE
 */
@SuppressWarnings("UnstableApiUsage")
public class BYTE implements PrimitiveType {
    private final byte value;

    public BYTE(byte value) {
        this.value = value;
    }

    public byte asByte() {
        return value;
    }

    public static BYTE readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte value = stream.readByte();
        return new BYTE(value);
    }

    @Override
    public UnsignedInteger toJavaValue() {
        return UnsignedInteger.fromIntBits(Byte.toUnsignedInt(asByte()));
    }

    @Override
    public String toString() {
        return Integer.toString(Byte.toUnsignedInt(asByte()));
    }
}
