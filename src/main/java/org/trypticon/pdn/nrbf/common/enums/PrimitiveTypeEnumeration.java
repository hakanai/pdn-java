package org.trypticon.pdn.nrbf.common.enums;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;

import java.io.IOException;
import java.util.Arrays;

/**
 * [MS-NRBF] 2.1.2.3 PrimitiveTypeEnumeration
 */
@SuppressWarnings("UnstableApiUsage")
public enum PrimitiveTypeEnumeration {

    /**
     * Identifies a BOOLEAN as specified in [MS-DTYP] section 2.2.4.
     */
    Boolean(1),

    /**
     * Identifies a BYTE as specified in [MS-DTYP] section 2.2.6.
     */
    Byte(2),

    /**
     * Identifies a Char (section 2.1.1.1) type.
     */
    Char(3),

    /**
     * The value is not used in the protocol.
     */
    Reserved(4),

    /**
     * Identifies a Decimal (section 2.1.1.7).
     */
    Decimal(5),

    /**
     * Identifies a Double (section 2.1.1.2).
     */
    Double(6),

    /**
     * Identifies an INT16 as specified in [MS-DTYP] section 2.2.21.
     */
    Int16(7),

    /**
     * Identifies an INT32 as specified in [MS-DTYP] section 2.2.22.
     */
    Int32(8),

    /**
     * Identifies an INT64 as specified in [MS-DTYP] section 2.2.23.
     */
    Int64(9),

    /**
     * Identifies an INT8 as specified in [MS-DTYP] section 2.2.20.
     */
    SByte(10),

    /**
     * Identifies a Single (section 2.1.1.3).
     */
    Single(11),

    /**
     * Identifies a TimeSpan (section 2.1.1.4).
     */
    TimeSpan(12),

    /**
     * Identifies a DateTime (section 2.1.1.5).
     */
    DateTime(13),

    /**
     * Identifies a UINT16 as specified in [MS-DTYP] section 2.2.48.
     */
    UInt16(14),

    /**
     * Identifies a UINT32 as specified in [MS-DTYP] section 2.2.49.
     */
    UInt32(15),

    /**
     * Identifies a UINT64 as specified in [MS-DTYP] section 2.2.50.
     */
    UInt64(16),

    /**
     * Identifies a Null Object.
     */
    Null(17),

    /**
     * Identifies a LengthPrefixedString (section 2.1.1.6) value.
     */
    String(18),

    ;

    private final int value;

    PrimitiveTypeEnumeration(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PrimitiveTypeEnumeration forValue(int value) throws NrbfException {
        return Arrays.stream(PrimitiveTypeEnumeration.values())
                .filter(recordType -> recordType.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new NrbfException("Invalid PrimitiveType: " + value));
    }

    public static PrimitiveTypeEnumeration readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte b = stream.readByte();
        return forValue(b);
    }
}
