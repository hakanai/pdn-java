package org.trypticon.pdn.nrbf.arrays.records;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.arrays.NrbfArrayRecord;
import org.trypticon.pdn.nrbf.arrays.structs.ArrayInfo;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.references.MemberPrimitiveUnTyped;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * [MS-NRBF] 2.4.3.3 ArraySinglePrimitive
 */
@SuppressWarnings("UnstableApiUsage")
public class ArraySinglePrimitive extends NrbfArrayRecord<MemberPrimitiveUnTyped> {
    private final ArrayInfo arrayInfo;
    private final PrimitiveTypeEnumeration primitiveTypeEnum;
    private final List<MemberPrimitiveUnTyped> members;

    public ArraySinglePrimitive(ArrayInfo arrayInfo, PrimitiveTypeEnumeration primitiveTypeEnum, List<MemberPrimitiveUnTyped> members) {
        super(RecordTypeEnumeration.ArraySinglePrimitive);

        this.arrayInfo = arrayInfo;
        this.primitiveTypeEnum = primitiveTypeEnum;
        this.members = members;
    }

    public ArrayInfo getArrayInfo() {
        return arrayInfo;
    }

    public PrimitiveTypeEnumeration getPrimitiveTypeEnum() {
        return primitiveTypeEnum;
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
    public List<MemberPrimitiveUnTyped> getMembers() {
        return members;
    }

    public static ArraySinglePrimitive readFrom(LittleEndianDataInputStream stream) throws IOException {
        ArrayInfo arrayInfo = ArrayInfo.readFrom(stream);
        PrimitiveTypeEnumeration primitiveTypeEnum = PrimitiveTypeEnumeration.readFrom(stream);
        List<MemberPrimitiveUnTyped> members = new ArrayList<>(arrayInfo.getLength());
        for (int i = 0; i < arrayInfo.getLength(); i++) {
            members.add(MemberPrimitiveUnTyped.readFrom(stream, primitiveTypeEnum));
        }
        return new ArraySinglePrimitive(arrayInfo, primitiveTypeEnum, members);
    }
}
