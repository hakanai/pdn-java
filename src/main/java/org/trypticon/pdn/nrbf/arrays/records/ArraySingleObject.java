package org.trypticon.pdn.nrbf.arrays.records;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.arrays.NrbfArrayRecord;
import org.trypticon.pdn.nrbf.arrays.structs.ArrayInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.io.IOException;
import java.util.List;

/**
 * [MS-NRBF] 2.4.3.2 ArraySingleObject
 */
@SuppressWarnings("UnstableApiUsage")
public class ArraySingleObject extends NrbfArrayRecord<Object> {
    private final ArrayInfo arrayInfo;
    private final List<Object> members;

    public ArraySingleObject(ArrayInfo arrayInfo, List<Object> members) {
        super(RecordTypeEnumeration.ArraySingleObject);
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
    public List<Object> getMembers() {
        return members;
    }

    public static ArraySingleObject readFrom(LittleEndianDataInputStream stream, Registry registry) throws IOException {
        ArrayInfo arrayInfo = ArrayInfo.readFrom(stream);
        List<Object> members = readArrayMembers(stream, registry, arrayInfo.getLength());
        return new ArraySingleObject(arrayInfo, members);
    }
}
