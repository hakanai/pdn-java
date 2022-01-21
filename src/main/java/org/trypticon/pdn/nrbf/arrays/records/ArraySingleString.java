package org.trypticon.pdn.nrbf.arrays.records;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.arrays.NrbfArrayRecord;
import org.trypticon.pdn.nrbf.arrays.structs.ArrayInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * [MS-NRBF] 2.4.3.4 ArraySingleString
 */
@SuppressWarnings("UnstableApiUsage")
public class ArraySingleString extends NrbfArrayRecord<LengthPrefixedString> {
    private final ArrayInfo arrayInfo;
    private final List<LengthPrefixedString> members;

    public ArraySingleString(ArrayInfo arrayInfo,
                             List<LengthPrefixedString> members) {

        super(RecordTypeEnumeration.ArraySingleString);

        this.arrayInfo = arrayInfo;
        this.members = members;
    }

    public ArrayInfo getArrayInfo() {
        return arrayInfo;
    }

    @Override
    public int getObjectId() {
        return arrayInfo.getObjectId();
    }

    @Override
    public int getRank() {
        return 1;
    }

    @Override
    public List<Integer> getLengths() {
        return ImmutableList.of(arrayInfo.getLength());
    }

    @Override
    public List<LengthPrefixedString> getMembers() {
        return members;
    }

    public static ArraySingleString readFrom(LittleEndianDataInputStream stream) throws IOException {
        ArrayInfo arrayInfo = ArrayInfo.readFrom(stream);
        List<LengthPrefixedString> members = new ArrayList<>(arrayInfo.getLength());
        for (int i = 0; i < arrayInfo.getLength(); i++) {
            //members.add(LengthPrefixedString.readFrom(stream));
        }
        return new ArraySingleString(arrayInfo, members);
    }
}
