package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedInteger;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.48 UINT16
 */
@SuppressWarnings("UnstableApiUsage")
public class UINT16 implements PrimitiveType {
    private final short value;

    public UINT16(short value) {
        this.value = value;
    }

    public short asShort() {
        return value;
    }

    public static UINT16 readFrom(LittleEndianDataInputStream stream) throws IOException {
        short value = stream.readShort();
        return new UINT16(value);
    }

    @Override
    public UnsignedInteger toJavaValue() {
       return UnsignedInteger.fromIntBits(Short.toUnsignedInt(asShort()));
    }

    @Override
    public String toString() {
        return Integer.toString(Short.toUnsignedInt(asShort()));
    }
}
