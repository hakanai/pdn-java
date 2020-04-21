package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.UnsignedLong;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.50 UINT64
 */
@SuppressWarnings("UnstableApiUsage")
public class UINT64 implements PrimitiveType {
    private final long value;

    public UINT64(long value) {
        this.value = value;
    }

    public long asLong() {
        return value;
    }

    public static UINT64 readFrom(LittleEndianDataInputStream stream) throws IOException {
        long value = stream.readLong();
        return new UINT64(value);
    }

    @Override
    public UnsignedLong toJavaValue() {
        return UnsignedLong.fromLongBits(asLong());
    }

    @Override
    public String toString() {
        return Long.toUnsignedString(asLong());
    }
}
