package org.trypticon.pdn.nrbf;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteSource;
import com.google.common.io.LittleEndianDataInputStream;
import org.trypticon.pdn.nrbf.other.MessageEnd;
import org.trypticon.pdn.nrbf.other.SerializationHeaderRecord;
import org.trypticon.pdn.nrbf.references.MemberReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 2.7 Binary Record Grammar
 *
 * Basically the top-level format.
 */
@SuppressWarnings("UnstableApiUsage")
public class Nrbf {
    private final Registry registry;
    private final List<NrbfRecord> records;

    private Nrbf(Registry registry, List<NrbfRecord> records) {
        this.registry = registry;
        this.records = records;
    }

    @VisibleForTesting
    List<NrbfRecord> getRecords() {
        return records;
    }

    /**
     * Gets the root of the object graph.
     *
     * @return the root object.
     */
    public NrbfReferableRecord getRoot() {
        SerializationHeaderRecord header = (SerializationHeaderRecord) records.get(0);
        return registry.lookupObjectById(header.getRootId());
    }

    /**
     * Reads NRBF from the given byte source.
     *
     * @param byteSource the byte source to read from.
     * @return the NRBF model.
     * @throws IOException if an error occurs.
     */
    public static Nrbf readFrom(ByteSource byteSource) throws IOException {
        try (InputStream stream = byteSource.openBufferedStream()) {
            return readFrom(stream);
        }
    }

    /**
     * Reads NRBF from the given input stream. The stream should be buffered
     * if you expect good performance.
     *
     * @param stream the input stream.
     * @return the NRBF model.
     * @throws IOException if an error occurs.
     */
    public static Nrbf readFrom(InputStream stream) throws IOException {
        return readFrom(new LittleEndianDataInputStream(stream));
    }

    private static Nrbf readFrom(LittleEndianDataInputStream stream) throws IOException {
        Registry registry = new Registry();
        List<NrbfRecord> records = readRecords(stream, registry);
        resolveReferences(registry, records);
        return new Nrbf(registry, records);
    }

    private static List<NrbfRecord> readRecords(LittleEndianDataInputStream stream, Registry registry)
            throws IOException {

        List<NrbfRecord> records = new ArrayList<>(64);
        while (true) {
            NrbfRecord record = NrbfRecord.readFrom(stream, registry);
            records.add(record);
            if (record instanceof NrbfReferableRecord) {
                registry.registerObject((NrbfReferableRecord) record);
            } else if (record instanceof MessageEnd) {
                break;
            }
        }
        return records;
    }

    // Unavoidable without reified generics.
    @SuppressWarnings("unchecked")
    private static <E> void resolveReferences(Registry registry, List<E> records) {

        for (int i = 0; i < records.size(); i++) {
            E record = records.get(i);
            E newRecord = record;

            if (record instanceof NrbfCompositeRecord<?>) {

                // Bit of a dance to avoid unnecessarily copying the record if the list of members didn't change.
                List<?> members = ((NrbfCompositeRecord<? >) record).getMembers();
                resolveReferences(registry, members);

                registry.registerObject((NrbfReferableRecord) newRecord);
                records.set(i, newRecord);
            }

            if (record instanceof MemberReference) {
                newRecord = (E) registry.lookupObjectById(((MemberReference) record).getIdRef());
                records.set(i, newRecord);
            }
        }
    }
}
