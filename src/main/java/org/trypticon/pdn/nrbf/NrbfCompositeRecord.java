package org.trypticon.pdn.nrbf;

import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

import java.util.List;

/**
 * Abstraction of commonality between arrays and classes.
 */
public abstract class NrbfCompositeRecord<M> extends NrbfReferableRecord {

    protected NrbfCompositeRecord(RecordTypeEnumeration recordTypeEnum) {
        super(recordTypeEnum);
    }

    /**
     * Gets the members of the array.
     *
     * @return the members of the array.
     */
    public abstract List<M> getMembers();
}
