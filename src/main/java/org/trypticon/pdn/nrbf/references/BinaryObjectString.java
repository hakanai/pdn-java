package org.trypticon.pdn.nrbf.references;

import com.google.common.base.MoreObjects;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfReferableRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.5.7 BinaryObjectString
 */
@SuppressWarnings("UnstableApiUsage")
public class BinaryObjectString extends NrbfReferableRecord {
    private final INT32 objectId;
    private final LengthPrefixedString value;

    public BinaryObjectString(INT32 objectId, LengthPrefixedString value) {
        super(RecordTypeEnumeration.BinaryObjectString);

        this.objectId = objectId;
        this.value = value;
    }

    @Override
    public int getObjectId() {
        return objectId.asInt();
    }

    public LengthPrefixedString getValue() {
        return value;
    }

    public static BinaryObjectString readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 objectId = INT32.readFrom(stream);
        LengthPrefixedString value = LengthPrefixedString.readFrom(stream);
        return new BinaryObjectString(objectId, value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("objectId", getObjectId())
                .add("value", getValue())
                .toString();
    }
}
