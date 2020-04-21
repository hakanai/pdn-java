package org.trypticon.pdn.nrbf.classes.structs;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.common.enums.BinaryTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.ClassTypeInfo;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;
import org.trypticon.pdn.nrbf.references.ObjectNull;

import java.io.IOException;

/**
 * [MS-NRBF] 2.3.1.2 MemberTypeInfo
 */
@SuppressWarnings("UnstableApiUsage")
public class MemberTypeInfo {
    private final ImmutableList<BinaryTypeEnumeration> binaryTypeEnums;
    private final ImmutableList<Object> additionalInfos;

    public MemberTypeInfo(ImmutableList<BinaryTypeEnumeration> binaryTypeEnums,
                          ImmutableList<Object> additionalInfos) {

        this.binaryTypeEnums = binaryTypeEnums;
        this.additionalInfos = additionalInfos;
    }

    public ImmutableList<BinaryTypeEnumeration> getBinaryTypeEnums() {
        return binaryTypeEnums;
    }

    public ImmutableList<Object> getAdditionalInfos() {
        return additionalInfos;
    }

    public static MemberTypeInfo readFrom(LittleEndianDataInputStream stream, ClassInfo classInfo) throws IOException {
        ImmutableList.Builder<BinaryTypeEnumeration> binaryTypeEnumsBuilder = ImmutableList.builder();
        for (int i = 0; i < classInfo.getMemberCount(); i++) {
            BinaryTypeEnumeration binaryTypeEnum = BinaryTypeEnumeration.readFrom(stream);
            binaryTypeEnumsBuilder.add(binaryTypeEnum);
        }
        ImmutableList<BinaryTypeEnumeration> binaryTypeEnums = binaryTypeEnumsBuilder.build();

        ImmutableList.Builder<Object> additionalInfosBuilder = ImmutableList.builder();
        for (BinaryTypeEnumeration binaryTypeEnum : binaryTypeEnums) {
            switch (binaryTypeEnum) {
                case Primitive:
                case PrimitiveArray:
                    additionalInfosBuilder.add(PrimitiveTypeEnumeration.readFrom(stream));
                    break;

                case SystemClass:
                    additionalInfosBuilder.add(LengthPrefixedString.readFrom(stream));
                    break;

                case Class:
                    additionalInfosBuilder.add(ClassTypeInfo.readFrom(stream));
                    break;

                case String:
                case Object:
                case ObjectArray:
                case StringArray:
                    // Adding an explicit null to align the entries.
                    additionalInfosBuilder.add(new ObjectNull());
                    break;

                default:
                    throw new AssertionError();
            }
        }
        ImmutableList<Object> additionalInfos = additionalInfosBuilder.build();

        return new MemberTypeInfo(binaryTypeEnums, additionalInfos);
    }
}
