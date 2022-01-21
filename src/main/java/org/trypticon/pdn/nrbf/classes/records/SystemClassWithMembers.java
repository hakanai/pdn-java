package org.trypticon.pdn.nrbf.classes.records;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.classes.structs.ClassInfo;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.io.IOException;

/**
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
