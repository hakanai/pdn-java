package org.trypticon.pdn.nrbf.invocations.structs;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.2.2.3 ArrayOfValueWithCode
 */
@SuppressWarnings("UnstableApiUsage")
public class ArrayOfValueWithCode {
    private final ImmutableList<ValueWithCode> listOfValueWithCode;

    public ArrayOfValueWithCode(ImmutableList<ValueWithCode> listOfValueWithCode) {
        this.listOfValueWithCode = listOfValueWithCode;
    }

    public ImmutableList<ValueWithCode> getListOfValueWithCode() {
        return listOfValueWithCode;
    }

    public static ArrayOfValueWithCode readFrom(LittleEndianDataInputStream stream) throws IOException {
        int length = INT32.readFrom(stream).asInt();
        ImmutableList.Builder<ValueWithCode> builder = ImmutableList.builder();
        for (int i = 0; i < length; i++) {
            builder.add(ValueWithCode.readFrom(stream));
        }
        return new ArrayOfValueWithCode(builder.build());
    }
}
