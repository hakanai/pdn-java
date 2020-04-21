package org.trypticon.pdn.nrbf.classes;

import org.trypticon.pdn.nrbf.common.enums.BinaryTypeEnumeration;

import javax.annotation.Nullable;

/**
 * Representation of info about a single member.
 */
public class MemberMetadata {
    private final String name;
    private final BinaryTypeEnumeration binaryType;
    @Nullable
    private final Object additionalInfo;

    public MemberMetadata(String name, BinaryTypeEnumeration binaryType, @Nullable Object additionalInfo) {
        this.name = name;
        this.binaryType = binaryType;
        this.additionalInfo = additionalInfo;
    }

    public String getName() {
        return name;
    }

    public BinaryTypeEnumeration getBinaryType() {
        return binaryType;
    }

    @Nullable
    public Object getAdditionalInfo() {
        return additionalInfo;
    }

}
