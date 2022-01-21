package org.trypticon.pdn.nrbf.common.enums;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfException;

import java.io.IOException;
import java.util.Arrays;

/**
 * [MS-NRBF] 2.1.2.1 RecordTypeEnumeration
 */
@SuppressWarnings("UnstableApiUsage")
public enum RecordTypeEnumeration {

    /**
     * Identifies the SerializationHeaderRecord.
     */
    SerializedStreamHeader(0),

    /**
     * Identifies a ClassWithId record.
     */
    ClassWithId(1),

    /**
     * Identifies a SystemClassWithMembers record.
     */
    SystemClassWithMembers(2),

    /**
     * Identifies a ClassWithMembers record.
     */
    ClassWithMembers(3),

    /**
     * Identifies a SystemClassWithMembersAndTypes record.
     */
    SystemClassWithMembersAndTypes(4),

    /**
     * Identifies a ClassWithMembersAndTypes record.
     */
    ClassWithMembersAndTypes(5),

    /**
     * Identifies a BinaryObjectString record.
     */
    BinaryObjectString(6),

    /**
     * Identifies a BinaryArray record.
     */
    BinaryArray(7),

    /**
     * Identifies a MemberPrimitiveTyped record.
     */
    MemberPrimitiveTyped(8),

    /**
     * Identifies a MemberReference record.
     */
    MemberReference(9),

    /**
     * Identifies an ObjectNull record.
     */
    ObjectNull(10),

    /**
     * Identifies a MessageEnd record.
     */
    MessageEnd(11),

    /**
     * Identifies a BinaryLibrary record.
     */
    BinaryLibrary(12),

    /**
     * Identifies an ObjectNullMultiple256 record.
     */
    ObjectNullMultiple256(13),

    /**
     * Identifies an ObjectNullMultiple record
     */
    ObjectNullMultiple(14),

    /**
     * Identifies an ArraySinglePrimitive.
     */
    ArraySinglePrimitive(15),

    /**
     * Identifies an ArraySingleObject record.
     */
    ArraySingleObject(16),

    /**
     * Identifies an ArraySingleString record.
     */
    ArraySingleString(17),

    /**
     * Identifies a BinaryMethodCall record.
     */
    BinaryMethodCall(21),

    /**
     * Identifies a BinaryMethodReturn record.
     */
    BinaryMethodReturn(22),

    ;

    private final int value;

    RecordTypeEnumeration(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RecordTypeEnumeration forValue(int value) throws NrbfException {
        return Arrays.stream(RecordTypeEnumeration.values())
                .filter(recordType -> recordType.getValue() == value)
                .findFirst()
                .orElseThrow(() ->
                        new NrbfException("Invalid RecordType: " + value));
    }

    public static RecordTypeEnumeration readFrom(LittleEndianDataInputStream stream) throws IOException {
        byte b = stream.readByte();
        return forValue(b);
    }
}
