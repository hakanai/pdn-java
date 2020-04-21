package org.trypticon.pdn.nrbf.classes.structs;

import com.google.common.collect.ImmutableList;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.common.types.LengthPrefixedString;
import org.trypticon.pdn.nrbf.win32.INT32;

import java.io.IOException;

/**
 * [MS-NRBF] 2.3.1.1 ClassInfo
 */
@SuppressWarnings("UnstableApiUsage")
public class ClassInfo {
    private final INT32 objectId;
    private final LengthPrefixedString name;
    private final ImmutableList<LengthPrefixedString> memberNames;

    public ClassInfo(INT32 objectId, LengthPrefixedString name, ImmutableList<LengthPrefixedString> memberNames) {
        this.objectId = objectId;
        this.name = name;
        this.memberNames = memberNames;
    }

    public int getObjectId() {
        return objectId.asInt();
    }

    public String getName() {
        return name.asString();
    }

    public ImmutableList<String> getMemberNames() {
        return memberNames.stream()
                .map(LengthPrefixedString::asString)
                .collect(ImmutableList.toImmutableList());
    }

    public int getMemberCount() {
        return memberNames.size();
    }

    public static ClassInfo readFrom(LittleEndianDataInputStream stream) throws IOException {
        INT32 objectId = INT32.readFrom(stream);
        LengthPrefixedString name = LengthPrefixedString.readFrom(stream);
        int memberCount = INT32.readFrom(stream).asInt();
        ImmutableList.Builder<LengthPrefixedString> memberNames = ImmutableList.builderWithExpectedSize(memberCount);
        for (int i = 0; i < memberCount; i++) {
            LengthPrefixedString memberName = LengthPrefixedString.readFrom(stream);
            memberNames.add(memberName);
        }
        return new ClassInfo(objectId, name, memberNames.build());
    }
}
