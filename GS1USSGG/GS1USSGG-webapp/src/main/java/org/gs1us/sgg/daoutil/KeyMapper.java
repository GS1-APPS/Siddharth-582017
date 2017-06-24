package org.gs1us.sgg.daoutil;

public interface KeyMapper<K>
{
    public String keyToId(K key);
    public K idToKey(String id);
}
