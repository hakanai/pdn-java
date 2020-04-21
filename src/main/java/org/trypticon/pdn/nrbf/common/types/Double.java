package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;

/**
 * [MS-NRBF] 2.1.1.2 Double
 */
@SuppressWarnings("UnstableApiUsage")
public class Double implements PrimitiveType {
    private final double value;

    public Double(double value) {
        this.value = value;
    }

    public double asDouble() {
        return value;
    }

    public static Double readFrom(LittleEndianDataInputStream stream) throws IOException {
        double value = stream.readDouble();
        return new Double(value);
    }

    @Override
    public java.lang.Double toJavaValue() {
        return asDouble();
    }

    @Override
    public String toString() {
        return java.lang.Double.toString(asDouble());
    }
}
