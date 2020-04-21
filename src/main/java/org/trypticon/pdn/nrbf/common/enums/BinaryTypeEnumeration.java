package org.trypticon.pdn.nrbf.common.enums;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;

import java.io.IOException;
import java.util.Arrays;

/**
 * [MS-NRBF] 2.1.2.2 BinaryTypeEnumeration
 */
@SuppressWarnings("UnstableApiUsage")
public enum BinaryTypeEnumeration {

    /**
     * The Remoting Type is defined in PrimitiveTypeEnumeration and the Remoting Type is not a string.
     */
    Primitive(0),

    /**
     * The Remoting Type is a LengthPrefixedString.
     */
    String(1),

    /**
     * The Remoting Type is System.Object.
     */
    Object(2),

    /**
     * The Remoting Type is one of the following:
     * - A Class (2) in the System Library
     * - An Array whose Ultimate Array Item Type is a Class (2) in the System Library
     * - An Array whose Ultimate Array Item Type is System.Object, String, or a Primitive Type but does not meet
     *   the definition of ObjectArray, StringArray, or PrimitiveArray.
     */
    SystemClass(3),

    /**
     * The Remoting Type is a Class (2) or an Array whose Ultimate Array Item Type is a Class (2) that is not
     * in the System Library.
     */
    Class(4),

    /**
     * The Remoting Type is a single-dimensional Array of System.Object with a lower bound of 0.
     */
    ObjectArray(5),

    /**
     * The Remoting Type is a single-dimensional Array of String with a lower bound of 0.
     */
    StringArray(6),

    /**
     * The Remoting Type is a single-dimensional Array of a Primitive Type with a lower bound of 0.
     */
    PrimitiveArray(7),

    ;

    private final int value;

    BinaryTypeEnumeration(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BinaryTypeEnumeration forValue(int value) throws NrbfException {
        return Arrays.stream(BinaryTypeEnumeration.values())
                .filter(recordType -> recordType.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new NrbfException("Invalid BinaryType: " + value));
    }

    public static BinaryTypeEnumeration readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte b = stream.readByte();
        return forValue(b);
    }
}
