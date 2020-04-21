package org.trypticon.pdn.nrbf.other;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.6.1 SerializationHeaderRecord
 */
@SuppressWarnings("UnstableApiUsage")
public class SerializationHeaderRecord extends NrbfRecord {
    private final INT32 rootId;
    private final INT32 headerId;
    private final INT32 majorVersion;
    private final INT32 minorVersion;

    public SerializationHeaderRecord(INT32 rootId, INT32 headerId,
                                     INT32 majorVersion, INT32 minorVersion) {

        super(RecordTypeEnumeration.SerializedStreamHeader);
        this.rootId = rootId;
        this.headerId = headerId;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public int getRootId() {
        return rootId.asInt();
    }

    public int getHeaderId() {
        return headerId.asInt();
    }

    public int getMajorVersion() {
        return majorVersion.asInt();
    }

    public int getMinorVersion() {
        return minorVersion.asInt();
    }

    public static SerializationHeaderRecord readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 rootId = INT32.readFrom(stream);
        INT32 headerId = INT32.readFrom(stream);
        INT32 majorVersion = INT32.readFrom(stream);
        INT32 minorVersion = INT32.readFrom(stream);
        return new SerializationHeaderRecord(rootId, headerId, majorVersion, minorVersion);
    }
}
