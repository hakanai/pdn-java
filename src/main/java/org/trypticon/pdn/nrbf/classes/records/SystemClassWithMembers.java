package org.trypticon.pdn.nrbf.classes.records;

import java.io.IOException;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.classes.structs.ClassInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

/**
 * A less verbose alternative to {@link ClassWithMembersAndTypes}.
 *
 * It does not contain a LibraryId or the information about the Remoting Types of the Members.
 * This record implicitly specifies that the Class is in the System Library. This record can be used
 * when the information is deemed unnecessary because it is known out of band or can be inferred from context.
 *
 * [MS-NRBF] 2.3.2.4 SystemClassWithMembers
 */
@SuppressWarnings("UnstableApiUsage")
public class SystemClassWithMembers extends NrbfRecord {
    private final ClassInfo classInfo;

    public SystemClassWithMembers(ClassInfo classInfo) {
        super(RecordTypeEnumeration.SystemClassWithMembers);

        this.classInfo = classInfo;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public static SystemClassWithMembers readFrom(LittleEndianDataInputStream stream) throws IOException {
        ClassInfo classInfo = ClassInfo.readFrom(stream);
        return new SystemClassWithMembers(classInfo);
    }
}
