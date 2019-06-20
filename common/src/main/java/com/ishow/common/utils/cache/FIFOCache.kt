package com.ishow.common.utils.cache

import java.util.*

/**
 * Created by yuhaiyang on 2018/9/21.
 * First In First Out - 先进先出的缓存规则
 */
class FIFOCache<K, V>(private val MAX_SIZE: Int) : LinkedHashMap<K, V>() {

    /**
     * 重写淘汰机制
     */
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return size > MAX_SIZE
    }

    companion object {
        private const val serialVersionUID = 436014030358073695L
    }

}
