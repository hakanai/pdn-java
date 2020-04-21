package org.trypticon.pdn.nrbf.references;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.BYTE;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.6 ObjectNullMultiple256
 */
@SuppressWarnings("UnstableApiUsage")
public class ObjectNullMultiple256 extends NrbfRecord {
    private final BYTE nullCount;

    public ObjectNullMultiple256(BYTE nullCount) {
        super(RecordTypeEnumeration.ObjectNullMultiple256);
        this.nullCount = nullCount;
    }

    public int getNullCount() {
        return nullCount.asByte();
    }

    public static ObjectNullMultiple256 readFrom(LittleEndianDataInputStream stream) throws IOException {
        BYTE nullCount = BYTE.readFrom(stream);
        return new ObjectNullMultiple256(nullCount);
    }
}
