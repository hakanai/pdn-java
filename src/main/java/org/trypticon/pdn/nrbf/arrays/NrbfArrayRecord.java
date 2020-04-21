package org.trypticon.pdn.nrbf.arrays;

import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.NrbfCompositeRecord;
import org.trypticon.pdn.nrbf.NrbfRecord;
import org.trypticon.pdn.nrbf.NrbfReferableRecord;
import org.trypticon.pdn.nrbf.Registry;
import org.trypticon.pdn.nrbf.common.enums.RecordTypeEnumeration;
import org.trypticon.pdn.nrbf.other.BinaryLibrary;
import org.trypticon.pdn.nrbf.references.ObjectNull;
import org.trypticon.pdn.nrbf.references.ObjectNullMultiple;
import org.trypticon.pdn.nrbf.references.ObjectNullMultiple256;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of records which store an array of member records.
 *
 * @param <M> the type of member records.
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class NrbfArrayRecord<M> extends NrbfCompositeRecord<M> {

    protected NrbfArrayRecord(RecordTypeEnumeration recordTypeEnum) {
        super(recordTypeEnum);
    }

    /**
     * Gets the rank (dimensionality) of the array.
     *
     * @return the rank.
     */
    public abstract int getRank();

    /**
     * Gets the lengths for each dimension of the array.
     *
     * @return the lengths.
     */
    public abstract List<Integer> getLengths();

    /**
     * Common logic to read array members.
     *
     * @param stream the stream to read from.
     * @param registry the registry.
     * @param length the length of the array.
     * @return the list of members.
     * @throws IOException if an error occurs reading data.
     */
    protected static List<Object> readArrayMembers(
            LittleEndianDataInputStream stream,
            Registry registry,
            int length)
            throws IOException {

        List<Object> members = new ArrayList<>(length);
        int memberIndex = 0;

        while (memberIndex < length) {
            NrbfRecord value = readFrom(stream, registry);
            if (value instanceof NrbfReferableRecord) {
                registry.registerObject((NrbfReferableRecord) value);
            }
            if (value instanceof BinaryLibrary) {
                continue;
            } else if (value instanceof ObjectNullMultiple) {
                for (int i = 0; i < ((ObjectNullMultiple) value).getNullCount(); i++) {
                    members.add(ObjectNull.INSTANCE);
                }
                memberIndex += ((ObjectNullMultiple) value).getNullCount();
                continue;
            } else if (value instanceof ObjectNullMultiple256) {
                for (int i = 0; i < ((ObjectNullMultiple256) value).getNullCount(); i++) {
                    members.add(ObjectNull.INSTANCE);
                }
                memberIndex += ((ObjectNullMultiple256) value).getNullCount();
                continue;
            }
            members.add(value);
            memberIndex ++;
        }

        return members;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
