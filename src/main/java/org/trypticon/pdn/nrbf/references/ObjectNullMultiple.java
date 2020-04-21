package org.trypticon.pdn.nrbf.references;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.5 ObjectNullMultiple
 */
@SuppressWarnings("UnstableApiUsage")
public class ObjectNullMultiple extends NrbfRecord {
    private final INT32 nullCount;

    public ObjectNullMultiple(INT32 nullCount) {
        super(RecordTypeEnumeration.ObjectNullMultiple);
        this.nullCount = nullCount;
    }

    public int getNullCount() {
        return nullCount.asInt();
    }

    public static ObjectNullMultiple readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 nullCount = INT32.readFrom(stream);
        return new ObjectNullMultiple(nullCount);
    }
}
