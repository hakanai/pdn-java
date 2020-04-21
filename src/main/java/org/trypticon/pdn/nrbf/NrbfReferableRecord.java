package org.trypticon.pdn.nrbf;

import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;

/**
 * Abstraction of records which can be referred to by reference.
 */
public abstract class NrbfReferableRecord extends NrbfRecord {
    protected NrbfReferableRecord(RecordTypeEnumeration recordTypeEnum) {
        super(recordTypeEnum);
    }

    /**
     * Gets the object ID.
     *
     * @return the object ID.
     */
    public abstract int getObjectId();
}
