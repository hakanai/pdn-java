package org.trypticon.pdn.nrbf.invocations.structs;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;

import java.io.IOException;

/**
 * [MS-NRBF] 2.2.2.2 StringValueWithCode
 */
@SuppressWarnings("UnstableApiUsage")
public class StringValueWithCode extends ValueWithCode {
    public StringValueWithCode(LengthPrefixedString value) {
        super(PrimitiveTypeEnumeration.String, value);
    }

    @Override
    public LengthPrefixedString getValue() {
        return (LengthPrefixedString) super.getValue();
    }

    public static StringValueWithCode readFrom(LittleEndianDataInputStream stream) throws IOException {
        ValueWithCode generic = ValueWithCode.readFrom(stream);
        if (generic.getPrimitiveTypeEnum() != PrimitiveTypeEnumeration.String) {
            throw new IllegalArgumentException("PrimitiveType must be 18 for StringValueWithCode");
        }
        return new StringValueWithCode((LengthPrefixedString) generic.getValue());
    }
}
