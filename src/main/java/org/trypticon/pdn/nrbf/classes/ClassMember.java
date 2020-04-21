package org.trypticon.pdn.nrbf.classes;

public class ClassMember {
    private final MemberMetadata metadata;
    private final Object value;

    public ClassMember(MemberMetadata metadata, Object value) {
        this.metadata = metadata;
        this.value = value;
    }

    public MemberMetadata getMetadata() {
        return metadata;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return metadata.getName() + " = " + value;
    }
}
