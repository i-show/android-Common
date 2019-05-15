package com.ishow.common.utils.cache;

import java.util.LinkedHashMap;

/**
 * Created by yuhaiyang on 2018/9/21.
 * First In First Out - 先进先出的缓存规则
 */
public class FIFOCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 436014030358073695L;

    private final int MAX_SIZE;

    public FIFOCache(int size) {
        super();
        MAX_SIZE = size;
    }

    //重写淘汰机制
    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }

}
