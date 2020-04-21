package org.trypticon.pdn.nrbf.other;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.io.IOException;

/**
 * [MS-NRBF] 2.6.3 MessageEnd
 */
@SuppressWarnings("UnstableApiUsage")
public class MessageEnd extends NrbfRecord {
    public MessageEnd() {
        super(RecordTypeEnumeration.MessageEnd);
    }

    public static MessageEnd readFrom(LittleEndianDataInputStream stream) throws IOException {
        return new MessageEnd();
    }
}
