package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.21 INT16
 */
@SuppressWarnings("UnstableApiUsage")
public class INT16 implements PrimitiveType {
    private final short value;

    public INT16(short value) {
        this.value = value;
    }

    public short asShort() {
        return value;
    }

    public static INT16 readFrom(LittleEndianDataInputStream stream) throws IOException {
        short value = stream.readShort();
        return new INT16(value);
    }

    @Override
    public Short toJavaValue() {
        return asShort();
    }

    @Override
    public String toString() {
        return Short.toString(asShort());
    }
}
