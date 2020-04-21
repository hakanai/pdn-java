package org.trypticon.pdn.nrbf.invocations.records;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.arrays.records.ArraySingleObject;
import org.trypticon.pdn.nrbf.invocations.enums.MessageFlags;

import java.io.IOException;

/**
 * [MS-NRBF] 2.2.3.2 MethodCallArray
 */
@SuppressWarnings("UnstableApiUsage")
public class MethodCallArray {
    private final ArraySingleObject array;

    public MethodCallArray(ArraySingleObject array) {
        this.array = array;
    }

    public static MethodCallArray readFrom(LittleEndianDataInputStream stream, Registry registry, BinaryMethodCall precedingRecord) throws IOException {
        if (precedingRecord.getMessageEnum().contains(MessageFlags.ArgsInArray)) {
            // Has Input Arguments
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.GenericMethod)) {
            // Has Generic Type Arguments
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.MethodSignatureInArray)) {
            // Has Method Signature
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.ContextInArray)) {
            // Has Call Context
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.PropertiesInArray)) {
            // Has Message Properties
        }
        ArraySingleObject array = ArraySingleObject.readFrom(stream, registry);
        return new MethodCallArray(array);
    }
}
