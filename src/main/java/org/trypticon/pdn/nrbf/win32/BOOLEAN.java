package org.trypticon.pdn.nrbf.win32;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-DTYP] 2.2.4 BOOLEAN
 */
@SuppressWarnings("UnstableApiUsage")
public class BOOLEAN implements PrimitiveType {
    private final boolean value;

    public BOOLEAN(boolean value) {
        this.value = value;
    }

    public boolean asBoolean() {
        return value;
    }

    public static BOOLEAN readFrom(LittleEndianDataInputStream stream) throws IOException {
        boolean value = stream.readBoolean();
        return new BOOLEAN(value);
    }

    @Override
    public Boolean toJavaValue() {
        return asBoolean();
    }

    @Override
    public String toString() {
        return Boolean.toString(asBoolean());
    }
}
