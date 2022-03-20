package org.trypticon.pdn.nrbf.classes.records;

import java.io.IOException;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.classes.structs.ClassInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.INT32;

/**
 * A less verbose alternative to {@link ClassWithMembersAndTypes}.
 *
 * It does not contain information about the Remoting Type information of the Members.
 * This record can be used when the information is deemed unnecessary because it is
 * known out of band or can be inferred from context.
 *
 * [MS-NRBF] 2.3.2.2 ClassWithMembers
 */
@SuppressWarnings("UnstableApiUsage")
public class ClassWithMembers extends NrbfRecord {
    private final ClassInfo classInfo;
    private final INT32 libraryId;

    public ClassWithMembers(ClassInfo classInfo,
                            INT32 libraryId) {

        super(RecordTypeEnumeration.ClassWithMembers);

        this.classInfo = classInfo;
        this.libraryId = libraryId;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public int getLibraryId() {
        return libraryId.asInt();
    }

    public static ClassWithMembers readFrom(LittleEndianDataInputStream stream) throws IOException {
        ClassInfo classInfo = ClassInfo.readFrom(stream);
        INT32 libraryId = INT32.readFrom(stream);
        return new ClassWithMembers(classInfo, libraryId);
    }
}
