package org.trypticon.pdn.nrbf.classes;

import com.google.common.collect.ImmutableList;
import org.trypticon.pdn.nrbf.classes.structs.ClassInfo;
import org.trypticon.pdn.nrbf.classes.structs.MemberTypeInfo;
import org.trypticon.pdn.nrbf.common.enums.BinaryTypeEnumeration;

import javax.annotation.Nullable;

/**
 * Holds all information about a class.
 */
public class ClassMetadata {
    @Nullable
    private final String libraryName;
    private final String name;
    private final ImmutableList<MemberMetadata> memberMetadata;

    public ClassMetadata(
            @Nullable String libraryName,
            String name,
            ImmutableList<MemberMetadata> memberMetadata) {

        this.libraryName = libraryName;
        this.name = name;
        this.memberMetadata = memberMetadata;
    }

    @Nullable
    public String getLibraryName() {
        return libraryName;
    }

    public String getName() {
        return name;
    }

    public ImmutableList<MemberMetadata> getMemberMetadata() {
        return memberMetadata;
    }

    /**
     * Reorganises info into a slightly more useful structure than how it's actually stored.
     *
     * @param libraryName the library name. Omitted if it is a system class.
     * @param classInfo the class info.
     * @param memberTypeInfo the member type info.
     * @return the class metadata.
     */
    public static ClassMetadata composeFrom(
            @Nullable String libraryName,
            ClassInfo classInfo,
            MemberTypeInfo memberTypeInfo) {

        ImmutableList.Builder<MemberMetadata> builder = ImmutableList.builder();
        ImmutableList<String> memberNames = classInfo.getMemberNames();
        ImmutableList<BinaryTypeEnumeration> binaryTypeEnums = memberTypeInfo.getBinaryTypeEnums();
        ImmutableList<Object> additionalInfos = memberTypeInfo.getAdditionalInfos();
        for (int i = 0; i < memberNames.size(); i++) {
            builder.add(new MemberMetadata(
                    memberNames.get(i),
                    binaryTypeEnums.get(i),
                    additionalInfos.get(i)));
        }
        return new ClassMetadata(libraryName, classInfo.getName(), builder.build());
    }
}
