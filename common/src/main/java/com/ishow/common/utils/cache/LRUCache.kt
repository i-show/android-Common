package com.ishow.common.utils.cache

import java.util.LinkedHashMap

/**
 * Created by yuhaiyang on 2018/9/21.
 * Least Recently Used
 * 最近最久未使用
 */
class LRUCache<K, V>(private val MAX_SIZE: Int) : LinkedHashMap<K, V>(MAX_SIZE, 0.75f, true) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>): Boolean {
        return size > MAX_SIZE
    }

    companion object {
        private const val serialVersionUID = 5853563362972200456L
    }
}

