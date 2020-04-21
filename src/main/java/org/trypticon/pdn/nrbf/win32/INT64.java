package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.23 INT64
 */
@SuppressWarnings("UnstableApiUsage")
public class INT64 implements PrimitiveType {
    private final long value;

    public INT64(long value) {
        this.value = value;
    }

    public long asLong() {
        return value;
    }

    public static INT64 readFrom(LittleEndianDataInputStream stream) throws IOException {
        long value = stream.readLong();
        return new INT64(value);
    }

    @Override
    public Long toJavaValue() {
        return asLong();
    }

    @Override
    public String toString() {
        return Long.toString(asLong());
    }
}
