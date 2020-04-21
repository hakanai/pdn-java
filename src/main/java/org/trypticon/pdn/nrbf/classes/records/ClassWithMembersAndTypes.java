package org.trypticon.pdn.nrbf.classes.records;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.classes.ClassMetadata;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;
import org.trypticon.pdn.nrbf.classes.structs.ClassInfo;
import org.trypticon.pdn.nrbf.classes.structs.MemberTypeInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;
import java.util.List;

/**
 * [MS-NRBF] 2.3.2.1 ClassWithMembersAndTypes
 */
@SuppressWarnings("UnstableApiUsage")
public class ClassWithMembersAndTypes extends NrbfClassRecord {
    private final ClassInfo classInfo;
    private final MemberTypeInfo memberTypeInfo;
    private final INT32 libraryId;
    private final ClassMetadata metadata;
    private final List<Object> members;

    public ClassWithMembersAndTypes(ClassInfo classInfo,
                                    MemberTypeInfo memberTypeInfo,
                                    INT32 libraryId,
                                    ClassMetadata metadata,
                                    List<Object> members) {

        super(RecordTypeEnumeration.ClassWithMembersAndTypes);

        this.classInfo = classInfo;
        this.memberTypeInfo = memberTypeInfo;
        this.libraryId = libraryId;
        this.metadata = metadata;
        this.members = members;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public MemberTypeInfo getMemberTypeInfo() {
        return memberTypeInfo;
    }

    public int getLibraryId() {
        return libraryId.asInt();
    }

    @Override
    public int getObjectId() {
        return classInfo.getObjectId();
    }

    @Override
    public ClassMetadata getMetadata() {
        return metadata;
    }

    @Override
    public List<Object> getMembers() {
        return members;
    }

    public static ClassWithMembersAndTypes readFrom(LittleEndianDataInputStream stream, Registry registry) throws IOException {
        ClassInfo classInfo = ClassInfo.readFrom(stream);
        MemberTypeInfo memberTypeInfo = MemberTypeInfo.readFrom(stream, classInfo);
        INT32 libraryId = INT32.readFrom(stream);
        String libraryName = registry.lookupLibraryById(libraryId.asInt()).getLibraryName();
        ClassMetadata metadata = ClassMetadata.composeFrom(libraryName, classInfo, memberTypeInfo);
        List<Object> members = readClassMembers(stream, registry, metadata);
        return new ClassWithMembersAndTypes(classInfo, memberTypeInfo, libraryId, metadata, members);
    }
}
