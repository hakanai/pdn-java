package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * [MS-NRBF] 2.1.1.7 Decimal
 */
@SuppressWarnings("UnstableApiUsage")
public class Decimal implements PrimitiveType {
    private final LengthPrefixedString value;

    public Decimal(LengthPrefixedString value) {
        this.value = value;
    }

    public BigDecimal asBigDecimal() {
        return new BigDecimal(value.asString());
    }

    public static Decimal readFrom(LittleEndianDataInputStream stream) throws IOException {
        LengthPrefixedString value = LengthPrefixedString.readFrom(stream);
        return new Decimal(value);
    }

    @Override
    public BigDecimal toJavaValue() {
        return asBigDecimal();
    }

    @Override
    public String toString() {
        return value.asString();
    }
}
