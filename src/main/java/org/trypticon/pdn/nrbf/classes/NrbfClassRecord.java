package org.trypticon.pdn.nrbf.classes;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfCompositeRecord;
import org.trypticon.pdn.nrbf.NrbfReferableRecord;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.common.enums.BinaryTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.PrimitiveTypeEnumeration;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.other.BinaryLibrary;
import org.trypticon.pdn.nrbf.references.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstraction of records which represent an instance of a class, i.e. an object.
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class NrbfClassRecord extends NrbfCompositeRecord<Object> {

    protected NrbfClassRecord(RecordTypeEnumeration recordTypeEnum) {
        super(recordTypeEnum);
    }

    /**
     * Gets the metadata for the class.
     *
     * @return the metadata for the class.
     */
    public abstract ClassMetadata getMetadata();

    /**
     * Gets the members of the object.
     *
     * @return the members of the object.
     */
    public ImmutableList<ClassMember> getClassMembers() {
        return Streams.zip(
                getMetadata().getMemberMetadata().stream(),
                getMembers().stream(),
                ClassMember::new)
                .collect(ImmutableList.toImmutableList());
    }

    /**
     * Convenience method to convert one level deep into a Java map.
     * The returned map cannot be modified. It isn't an {@link com.google.common.collect.ImmutableMap}
     * because the values are {@link javax.annotation.Nullable}.
     *
     * @return the map.
     */
    public Map<String, Object> asMap() {
        ImmutableList<MemberMetadata> memberMetadata = getMetadata().getMemberMetadata();
        List<Object> members = getMembers();
        Map<String, Object> map = Maps.newLinkedHashMapWithExpectedSize(memberMetadata.size());
        for (int i = 0; i < memberMetadata.size(); i++) {
            Object value = members.get(i);
            if (value instanceof MemberPrimitiveUnTyped) {
                value = ((MemberPrimitiveUnTyped) value).getValue().toJavaValue();
            } else if (value instanceof BinaryObjectString) {
                value = ((BinaryObjectString) value).getValue().toJavaValue();
            }
            map.put(memberMetadata.get(i).getName(), value);
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Common logic to read class members.
     *
     * @param stream the stream to read from.
     * @param registry the registry.
     * @param classMetadata the metadata for the class.
     * @return the list of members.
     * @throws IOException if an error occurs reading data.
     */
    protected static List<Object> readClassMembers(
            LittleEndianDataInputStream stream,
            Registry registry,
            ClassMetadata classMetadata) throws IOException {

        ImmutableList<MemberMetadata> classMemberMetadata = classMetadata.getMemberMetadata();
        List<Object> members = new ArrayList<>(classMemberMetadata.size());
        int memberIndex = 0;

        while (memberIndex < classMemberMetadata.size()) {
            MemberMetadata memberMetadata = classMemberMetadata.get(memberIndex);
            Object value;
            if (memberMetadata.getBinaryType() == BinaryTypeEnumeration.Primitive) {
                PrimitiveTypeEnumeration primitiveTypeEnum = (PrimitiveTypeEnumeration)
                        memberMetadata.getAdditionalInfo();
                assert primitiveTypeEnum != null;
                value = MemberPrimitiveUnTyped.readFrom(stream, primitiveTypeEnum);
            } else {
                value = readFrom(stream, registry);
                if (value instanceof NrbfReferableRecord) {
                    registry.registerObject((NrbfReferableRecord) value);
                }
                if (value instanceof BinaryLibrary) {
                    continue;
                } else if (value instanceof ObjectNullMultiple) {
                    for (int i = 0; i < ((ObjectNullMultiple) value).getNullCount(); i++) {
                        members.add(ObjectNull.INSTANCE);
                    }
                    continue;
                } else if (value instanceof ObjectNullMultiple256) {
                    for (int i = 0; i < ((ObjectNullMultiple256) value).getNullCount(); i++) {
                        members.add(ObjectNull.INSTANCE);
                    }
                    continue;
                }
            }
            members.add(value);
            memberIndex ++;
        }

        return members;
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper toStringHelper =
                MoreObjects.toStringHelper(getMetadata().getName());
        for (ClassMember member : getClassMembers()) {
            toStringHelper.add(member.getMetadata().getName(), member.getValue());
        }
        return toStringHelper.toString();
    }
}
