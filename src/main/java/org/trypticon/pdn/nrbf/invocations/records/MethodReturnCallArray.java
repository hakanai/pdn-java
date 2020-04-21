package org.trypticon.pdn.nrbf.invocations.records;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.arrays.records.ArraySingleObject;
import org.trypticon.pdn.nrbf.invocations.enums.MessageFlags;

import java.io.IOException;

/**
 * [MS-NRBF] 2.2.3.4 MethodReturnCallArray
 */
@SuppressWarnings("UnstableApiUsage")
public class MethodReturnCallArray {
    private final ArraySingleObject array;

    public MethodReturnCallArray(ArraySingleObject array) {
        this.array = array;
    }

    public static MethodReturnCallArray readFrom(LittleEndianDataInputStream stream, Registry registry, BinaryMethodReturn precedingRecord) throws IOException {
        if (precedingRecord.getMessageEnum().contains(MessageFlags.ReturnValueInArray)) {
            // Has Return Value
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.ArgsInArray)) {
            // Has Output Arguments
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.ExceptionInArray)) {
            // Has Exception
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.ContextInArray)) {
            // Has Call Context
        }
        if (precedingRecord.getMessageEnum().contains(MessageFlags.PropertiesInArray)) {
            // Has Message Properties
        }
        ArraySingleObject array = ArraySingleObject.readFrom(stream, registry);
        return new MethodReturnCallArray(array);
    }
}
