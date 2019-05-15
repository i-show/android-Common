package com.ishow.common.utils.cache;

import java.util.LinkedHashMap;

/**
 * Created by yuhaiyang on 2018/9/21.
 * Least Recently Used
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 5853563362972200456L;

    private final int MAX_SIZE;

    public LRUCache(int size) {
        super(size, 0.75f, true);
        MAX_SIZE = size;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }
}

