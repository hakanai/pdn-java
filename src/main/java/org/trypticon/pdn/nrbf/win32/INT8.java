package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.20 INT8
 */
@SuppressWarnings("UnstableApiUsage")
public class INT8 implements PrimitiveType {
    private final byte value;

    public INT8(byte value) {
        this.value = value;
    }

    public byte asByte() {
        return value;
    }

    public static INT8 readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte value = stream.readByte();
        return new INT8(value);
    }

    @Override
    public Byte toJavaValue() {
        return asByte();
    }

    @Override
    public String toString() {
        return Byte.toString(asByte());
    }
}
