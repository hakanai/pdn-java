package org.trypticon.pdn.nrbf.references;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.4 ObjectNull
 */
@SuppressWarnings("UnstableApiUsage")
public class ObjectNull extends NrbfRecord {
    public static final ObjectNull INSTANCE = new ObjectNull();

    public ObjectNull() {
        super(RecordTypeEnumeration.ObjectNull);
    }

    public static ObjectNull readFrom(LittleEndianDataInputStream stream) throws IOException {
        return INSTANCE;
    }
}
