package org.trypticon.pdn.nrbf.arrays.records;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.arrays.NrbfArrayRecord;
import org.trypticon.pdn.nrbf.arrays.enums.BinaryArrayTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.BinaryTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.ClassTypeInfo;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;
import java.util.List;

/**
 * [MS-NRBF] 2.4.3.1 BinaryArray
 */
@SuppressWarnings("UnstableApiUsage")
public class BinaryArray extends NrbfArrayRecord<Object> {
    private final INT32 objectId;
    private final BinaryArrayTypeEnumeration binaryArrayTypeEnum;
    private final INT32 rank;
    private final ImmutableList<INT32> lengths;
    private final ImmutableList<INT32> lowerBounds;
    private final BinaryTypeEnumeration typeEnum;
    private final Object additionalTypeInfo;
    private final List<Object> members;

    public BinaryArray(INT32 objectId,
                       BinaryArrayTypeEnumeration binaryArrayTypeEnum,
                       INT32 rank,
                       ImmutableList<INT32> lengths,
                       ImmutableList<INT32> lowerBounds,
                       BinaryTypeEnumeration typeEnum,
                       Object additionalTypeInfo,
                       List<Object> members) {

        super(RecordTypeEnumeration.BinaryArray);

        this.objectId = objectId;
        this.binaryArrayTypeEnum = binaryArrayTypeEnum;
        this.rank = rank;
        this.lengths = lengths;
        this.lowerBounds = lowerBounds;
        this.typeEnum = typeEnum;
        this.additionalTypeInfo = additionalTypeInfo;
        this.members = members;
    }

    public BinaryArrayTypeEnumeration getBinaryArrayTypeEnum() {
        return binaryArrayTypeEnum;
    }

    public ImmutableList<Integer> getLowerBounds() {
        if (lowerBounds == null) {
            return null;
        }
        return lowerBounds.stream()
                .map(INT32::asInt)
                .collect(ImmutableList.toImmutableList());
    }

    public BinaryTypeEnumeration getTypeEnum() {
        return typeEnum;
    }

    public Object getAdditionalTypeInfo() {
        return additionalTypeInfo;
    }

    @Override
    public int getObjectId() {
        return objectId.asInt();
    }

    @Override
    public int getRank() {
        return rank.asInt();
    }

    @Override
    public ImmutableList<Integer> getLengths() {
        return lengths.stream()
                .map(INT32::asInt)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<Object> getMembers() {
        return members;
    }

    public static BinaryArray readFrom(LittleEndianDataInputStream stream, Registry registry) throws IOException {
        INT32 objectId = INT32.readFrom(stream);
        BinaryArrayTypeEnumeration binaryArrayTypeEnum = BinaryArrayTypeEnumeration.readFrom(stream);
        INT32 rank = INT32.readFrom(stream);
        ImmutableList.Builder<INT32> lengthsBuilder = ImmutableList.builder();
        for (int i = 0; i < rank.asInt(); i++) {
            lengthsBuilder.add(INT32.readFrom(stream));
        }
        ImmutableList<INT32> lengths = lengthsBuilder.build();
        ImmutableList<INT32> lowerBounds = null;
        if (binaryArrayTypeEnum == BinaryArrayTypeEnumeration.SingleOffset ||
            binaryArrayTypeEnum == BinaryArrayTypeEnumeration.JaggedOffset ||
            binaryArrayTypeEnum == BinaryArrayTypeEnumeration.RectangularOffset) {

            ImmutableList.Builder<INT32> lowerBoundsBuilder = ImmutableList.builder();
            for (int i = 0; i < rank.asInt(); i++) {
                lowerBoundsBuilder.add(INT32.readFrom(stream));
            }
            lowerBounds = lowerBoundsBuilder.build();
        }
        BinaryTypeEnumeration typeEnum = BinaryTypeEnumeration.readFrom(stream);
        Object additionalTypeInfo = null;
        switch (typeEnum) {
            case Primitive:
            case PrimitiveArray:
                additionalTypeInfo = PrimitiveTypeEnumeration.readFrom(stream);
                break;
            case SystemClass:
                additionalTypeInfo = LengthPrefixedString.readFrom(stream);
                break;
            case Class:
                additionalTypeInfo = ClassTypeInfo.readFrom(stream);
                break;
        }
        int memberCount = computeMemberCount(lengths);
        List<Object> members = readArrayMembers(stream, registry, memberCount);
        return new BinaryArray(objectId, binaryArrayTypeEnum, rank, lengths, lowerBounds,
                typeEnum, additionalTypeInfo, members);
    }

    private static int computeMemberCount(ImmutableList<INT32> lengths) {
        return lengths.stream()
                .mapToInt(INT32::asInt)
                .reduce(1, (a, product) -> a * product);
    }
}
