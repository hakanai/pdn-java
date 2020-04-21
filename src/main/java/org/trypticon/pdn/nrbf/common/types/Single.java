package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-NRBF] 2.1.1.3 Single
 */
@SuppressWarnings("UnstableApiUsage")
public class Single implements PrimitiveType {
    private final float value;

    public Single(float value) {
        this.value = value;
    }

    public float asFloat() {
        return value;
    }

    public static Single readFrom(LittleEndianDataInputStream stream) throws IOException {
        float value = stream.readFloat();
        return new Single(value);
    }

    @Override
    public Float toJavaValue() {
        return asFloat();
    }

    @Override
    public String toString() {
        return Float.toString(asFloat());
    }
}
