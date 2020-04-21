package org.trypticon.pdn.dotnet.system.collections.generic;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.util.Map;

/**
 * Stand-in for .NET class {@code System.Collections.Generic.KeyValuePair}.
 */
public class KeyValuePair<K, V> {
    private final K key;
    private final V value;

    // Can't do much about this without reified generics.
    @SuppressWarnings("unchecked")
    public KeyValuePair(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        this.key = (K) map.get("key");
        this.value = (V) map.get("value");
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
