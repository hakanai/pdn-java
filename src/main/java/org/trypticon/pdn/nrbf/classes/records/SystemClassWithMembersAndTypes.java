package org.trypticon.pdn.nrbf.classes.records;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.classes.ClassMetadata;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;
import org.trypticon.pdn.nrbf.classes.structs.ClassInfo;
import org.trypticon.pdn.nrbf.classes.structs.MemberTypeInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.io.IOException;
import java.util.List;

/**
 * [MS-NRBF] 2.3.2.3 SystemClassWithMembersAndTypes
 */
@SuppressWarnings("UnstableApiUsage")
public class SystemClassWithMembersAndTypes extends NrbfClassRecord {
    private final ClassInfo classInfo;
    private final MemberTypeInfo memberTypeInfo;
    private final ClassMetadata metadata;
    private final List<Object> members;

    public SystemClassWithMembersAndTypes(ClassInfo classInfo,
                                          MemberTypeInfo memberTypeInfo,
                                          ClassMetadata metadata,
                                          List<Object> members) {

        super(RecordTypeEnumeration.SystemClassWithMembersAndTypes);

        this.classInfo = classInfo;
        this.memberTypeInfo = memberTypeInfo;
        this.metadata = metadata;
        this.members = members;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public MemberTypeInfo getMemberTypeInfo() {
        return memberTypeInfo;
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

    public static SystemClassWithMembersAndTypes readFrom(LittleEndianDataInputStream stream, Registry registry) throws IOException {
        ClassInfo classInfo = ClassInfo.readFrom(stream);
        MemberTypeInfo memberTypeInfo = MemberTypeInfo.readFrom(stream, classInfo);
        ClassMetadata metadata = ClassMetadata.composeFrom(null, classInfo, memberTypeInfo);
        List<Object> members = readClassMembers(stream, registry, metadata);
        return new SystemClassWithMembersAndTypes(classInfo, memberTypeInfo, metadata, members);
    }
}
