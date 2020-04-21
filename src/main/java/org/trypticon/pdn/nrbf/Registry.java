package org.trypticon.pdn.nrbf;

import com.google.common.collect.Maps;
import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;
import org.trypticon.pdn.nrbf.other.BinaryLibrary;

import java.util.Map;

/**
 * Context kept while reading NRBF.
 */
public class Registry {
    private final Map<Integer, NrbfReferableRecord> objectsById = Maps.newHashMap();
    private final Map<Integer, NrbfClassRecord> classesById = Maps.newHashMap();
    private final Map<Integer, BinaryLibrary> librariesById = Maps.newHashMap();

    public void registerObject(NrbfReferableRecord referableType) {
        objectsById.put(referableType.getObjectId(), referableType);
        if (referableType instanceof NrbfClassRecord) {
            classesById.put(referableType.getObjectId(), (NrbfClassRecord) referableType);
        }
        if (referableType instanceof BinaryLibrary) {
            librariesById.put(referableType.getObjectId(), (BinaryLibrary) referableType);
        }
    }

    public NrbfReferableRecord lookupObjectById(int id) {
        return objectsById.get(id);
    }

    public NrbfClassRecord lookupClassById(int id) {
        return classesById.get(id);
    }

    public BinaryLibrary lookupLibraryById(int id) {
        return librariesById.get(id);
    }
}
