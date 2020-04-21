package org.trypticon.pdn.nrbf;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.arrays.records.ArraySingleObject;
import org.trypticon.pdn.nrbf.arrays.records.ArraySinglePrimitive;
import org.trypticon.pdn.nrbf.arrays.records.ArraySingleString;
import org.trypticon.pdn.nrbf.arrays.records.BinaryArray;
import org.trypticon.pdn.nrbf.classes.records.*;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.invocations.records.BinaryMethodCall;
import org.trypticon.pdn.nrbf.invocations.records.BinaryMethodReturn;
import org.trypticon.pdn.nrbf.other.BinaryLibrary;
import org.trypticon.pdn.nrbf.other.MessageEnd;
import org.trypticon.pdn.nrbf.other.SerializationHeaderRecord;
import org.trypticon.pdn.nrbf.references.*;

import java.io.IOException;

/**
 * Abstract base class for all NRBF records.
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class NrbfRecord {
    private final RecordTypeEnumeration recordTypeEnum;

    protected NrbfRecord(RecordTypeEnumeration recordTypeEnum) {
        this.recordTypeEnum = recordTypeEnum;
    }

    public RecordTypeEnumeration getRecordTypeEnum() {
        return recordTypeEnum;
    }

    public static NrbfRecord readFrom(LittleEndianDataInputStream stream, Registry registry) throws IOException {
        RecordTypeEnumeration recordTypeEnum = RecordTypeEnumeration.readFrom(stream);
        switch (recordTypeEnum) {
            case SerializedStreamHeader:
                return SerializationHeaderRecord.readFrom(stream);
            case ClassWithId:
                return ClassWithId.readFrom(stream, registry);
            case SystemClassWithMembers:
                return SystemClassWithMembers.readFrom(stream);
            case ClassWithMembers:
                return ClassWithMembers.readFrom(stream);
            case SystemClassWithMembersAndTypes:
                return SystemClassWithMembersAndTypes.readFrom(stream, registry);
            case ClassWithMembersAndTypes:
                return ClassWithMembersAndTypes.readFrom(stream, registry);
            case BinaryObjectString:
                return BinaryObjectString.readFrom(stream);
            case BinaryArray:
                return BinaryArray.readFrom(stream, registry);
            case MemberPrimitiveTyped:
                return MemberPrimitiveTyped.readFrom(stream);
            case MemberReference:
                return MemberReference.readFrom(stream);
            case ObjectNull:
                return ObjectNull.readFrom(stream);
            case MessageEnd:
                return MessageEnd.readFrom(stream);
            case BinaryLibrary:
                return BinaryLibrary.readFrom(stream);
            case ObjectNullMultiple256:
                return ObjectNullMultiple256.readFrom(stream);
            case ObjectNullMultiple:
                return ObjectNullMultiple.readFrom(stream);
            case ArraySinglePrimitive:
                return ArraySinglePrimitive.readFrom(stream);
            case ArraySingleObject:
                return ArraySingleObject.readFrom(stream, registry);
            case ArraySingleString:
                return ArraySingleString.readFrom(stream);
            case BinaryMethodCall:
                return BinaryMethodCall.readFrom(stream);
            case BinaryMethodReturn:
                return BinaryMethodReturn.readFrom(stream);
            default:
                throw new AssertionError();
        }
    }
}
