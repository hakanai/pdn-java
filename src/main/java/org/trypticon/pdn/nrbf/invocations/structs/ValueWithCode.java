package org.trypticon.pdn.nrbf.invocations.structs;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.PrimitiveType;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.references.MemberPrimitiveUnTyped;

import java.io.IOException;

/**
 * [MS-NRBF] 2.2.2.1 ValueWithCode
 */
@SuppressWarnings("UnstableApiUsage")
public class ValueWithCode {
    private final PrimitiveTypeEnumeration primitiveTypeEnum;
    private final PrimitiveType value;

    public ValueWithCode(PrimitiveTypeEnumeration primitiveTypeEnum, PrimitiveType value) {
        this.primitiveTypeEnum = primitiveTypeEnum;
        this.value = value;
    }

    public PrimitiveTypeEnumeration getPrimitiveTypeEnum() {
        return primitiveTypeEnum;
    }

    public PrimitiveType getValue() {
        return value;
    }

    public static ValueWithCode readFrom(LittleEndianDataInputStream stream) throws IOException {
        PrimitiveTypeEnumeration primitiveTypeEnumeration = PrimitiveTypeEnumeration.readFrom(stream);
        PrimitiveType value = MemberPrimitiveUnTyped.readFrom(stream, primitiveTypeEnumeration).getValue();
        return new ValueWithCode(primitiveTypeEnumeration, value);
    }

    @Override
    public String toString() {
        return getPrimitiveTypeEnum() + ": " + getValue();
    }
}
