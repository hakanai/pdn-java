package org.trypticon.pdn.nrbf.classes.records;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.win32.INT32;
import org.trypticon.pdn.nrbf.classes.ClassMember;
import org.trypticon.pdn.nrbf.classes.ClassMetadata;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.io.IOException;
import java.util.List;

/**
 * [MS-NRBF] 2.3.2.5 ClassWithId
 */
@SuppressWarnings("UnstableApiUsage")
public class ClassWithId extends NrbfClassRecord {
    private final INT32 objectId;
    private final INT32 metadataId;
    private final ClassMetadata metadata;
    private final List<Object> members;

    public ClassWithId(
            INT32 objectId,
            INT32 metadataId,
            ClassMetadata metadata,
            List<Object> members) {

        super(RecordTypeEnumeration.ClassWithId);

        this.objectId = objectId;
        this.metadataId = metadataId;
        this.metadata = metadata;
        this.members = members;
    }

    @Override
    public int getObjectId() {
        return objectId.asInt();
    }

    public int getMetadataId() {
        return metadataId.asInt();
    }

    @Override
    public ClassMetadata getMetadata() {
        return metadata;
    }

    @Override
    public ImmutableList<ClassMember> getClassMembers() {
        return Streams.zip(
                metadata.getMemberMetadata().stream(),
                members.stream(),
                ClassMember::new)
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public List<Object> getMembers() {
        return members;
    }

    public static ClassWithId readFrom(LittleEndianDataInputStream stream, Registry registry) throws IOException {
        INT32 objectId = INT32.readFrom(stream);
        INT32 metadataId = INT32.readFrom(stream);
        ClassMetadata metadata = registry.lookupClassById(metadataId.asInt()).getMetadata();
        List<Object> members = readClassMembers(stream, registry, metadata);
        return new ClassWithId(objectId, metadataId, metadata, members);
    }
}
