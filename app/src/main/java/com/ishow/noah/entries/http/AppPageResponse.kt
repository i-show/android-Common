package com.ishow.noah.entries.http

/**
 * 分页返回数据
 */
class AppPageResponse<T> : AppHttpResponse<Page<T>>() {
    val listData: MutableList<T>?
        get() = data?.list

    val isLastPage: Boolean
        get() = data?.lastPage ?: true
}

class Page<T> {
    var lastPage: Boolean? = null
    var list: MutableList<T>? = null
}
