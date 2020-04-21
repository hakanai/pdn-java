package org.trypticon.pdn.nrbf.references;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;
import org.trypticon.pdn.nrbf.PrimitiveType;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.Double;
import org.trypticon.pdn.nrbf.common.types.*;
import org.trypticon.pdn.nrbf.win32.*;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.2 MemberPrimitiveUnTyped
 */
@SuppressWarnings("UnstableApiUsage")
public class MemberPrimitiveUnTyped {
    private final PrimitiveType value;

    public MemberPrimitiveUnTyped(PrimitiveType value) {
        this.value = value;
    }

    public PrimitiveType getValue() {
        return value;
    }

    public static MemberPrimitiveUnTyped readFrom(LittleEndianDataInputStream stream, PrimitiveTypeEnumeration primitiveTypeEnum) throws IOException {
        PrimitiveType value;
        switch (primitiveTypeEnum) {
            case Boolean:
                value = BOOLEAN.readFrom(stream);
                break;
            case Byte:
                value = BYTE.readFrom(stream);
                break;
            case Char:
                value = Char.readFrom(stream);
                break;
            case Reserved:
                throw new NrbfException("Reserved value");
            case Decimal:
                value = Decimal.readFrom(stream);
                break;
            case Double:
                value = Double.readFrom(stream);
                break;
            case Int16:
                value = INT16.readFrom(stream);
                break;
            case Int32:
                value = INT32.readFrom(stream);
                break;
            case Int64:
                value = INT64.readFrom(stream);
                break;
            case SByte:
                value = INT8.readFrom(stream);
                break;
            case Single:
                value = Single.readFrom(stream);
                break;
            case TimeSpan:
                value = TimeSpan.readFrom(stream);
                break;
            case DateTime:
                value = DateTime.readFrom(stream);
                break;
            case UInt16:
                value = UINT16.readFrom(stream);
                break;
            case UInt32:
                value = UINT32.readFrom(stream);
                break;
            case UInt64:
                value = UINT64.readFrom(stream);
                break;
            case Null:
                value = null;
                break;
            case String:
                value = LengthPrefixedString.readFrom(stream);
                break;
            default:
                throw new AssertionError();
        }
        return new MemberPrimitiveUnTyped(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
