package org.trypticon.pdn.nrbf.arrays.enums;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;

import java.io.IOException;
import java.util.Arrays;

/**
 * [MS-NRBF] 2.4.1.1 BinaryArrayTypeEnumeration
 */
@SuppressWarnings("UnstableApiUsage")
public enum BinaryArrayTypeEnumeration {

    /**
     * A single-dimensional Array.
     */
    Single(0),

    /**
     * An Array whose elements are Arrays. The elements of a jagged Array can be of different dimensions and sizes.
     */
    Jagged(1),

    /**
     * A multi-dimensional rectangular Array.
     */
    Rectangular(2),

    /**
     * A single-dimensional offset.
     */
    SingleOffset(3),

    /**
     * A jagged Array where the lower bound index is greater than 0.
     */
    JaggedOffset(4),

    /**
     * Multi-dimensional Arrays where the lower bound index of at least one of the dimensions is greater than 0.
     */
    RectangularOffset(5),

    ;

    private final int value;

    BinaryArrayTypeEnumeration(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BinaryArrayTypeEnumeration forValue(int value) throws NrbfException {
        return Arrays.stream(BinaryArrayTypeEnumeration.values())
                .filter(recordType -> recordType.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new NrbfException("Invalid BinaryArrayxType: " + value));
    }

    public static BinaryArrayTypeEnumeration readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte b = stream.readByte();
        return forValue(b);
    }
}
