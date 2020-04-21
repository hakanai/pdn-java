package org.trypticon.pdn.nrbf.references;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.3 MemberReference
 */
@SuppressWarnings("UnstableApiUsage")
public class MemberReference extends NrbfRecord {
    private final INT32 idRef;

    public MemberReference(INT32 idRef) {
        super(RecordTypeEnumeration.MemberReference);
        this.idRef = idRef;
    }

    public int getIdRef() {
        return idRef.asInt();
    }

    public static MemberReference readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 idRef = INT32.readFrom(stream);
        return new MemberReference(idRef);
    }

    @Override
    public String toString() {
        return "Reference -> " + idRef;
    }
}
