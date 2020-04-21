package org.trypticon.pdn.nrbf.common.types;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.1.1.8 ClassTypeInfo
 */
@SuppressWarnings("UnstableApiUsage")
public class ClassTypeInfo {
    // Trivia: The format of the string is specified in [MS-NRTP] section 2.2.1.2.
    private final LengthPrefixedString typeName;
    private final INT32 libraryId;

    public ClassTypeInfo(LengthPrefixedString typeName, INT32 libraryId) {
        this.typeName = typeName;
        this.libraryId = libraryId;
    }

    public static ClassTypeInfo readFrom(LittleEndianDataInputStream stream) throws IOException {
        LengthPrefixedString typeName = LengthPrefixedString.readFrom(stream);
        INT32 libraryId = INT32.readFrom(stream);
        return new ClassTypeInfo(typeName, libraryId);
    }
}
