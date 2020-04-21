package org.trypticon.pdn.nrbf.other;

import com.google.common.base.MoreObjects;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfReferableRecord;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * 2.6.2 BinaryLibrary
 */
@SuppressWarnings("UnstableApiUsage")
public class BinaryLibrary extends NrbfReferableRecord {
    private final INT32 libraryId;
    private final LengthPrefixedString libraryName;

    public BinaryLibrary(INT32 libraryId, LengthPrefixedString libraryName) {
        super(RecordTypeEnumeration.BinaryLibrary);

        this.libraryId = libraryId;
        this.libraryName = libraryName;
    }

    @Override
    public int getObjectId() {
        return libraryId.asInt();
    }

    public String getLibraryName() {
        return libraryName.asString();
    }

    public static BinaryLibrary readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 libraryId = INT32.readFrom(stream);
        LengthPrefixedString libraryName = LengthPrefixedString.readFrom(stream);
        return new BinaryLibrary(libraryId, libraryName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("objectId", getObjectId())
                .add("libraryName", getLibraryName())
                .toString();
    }
}
