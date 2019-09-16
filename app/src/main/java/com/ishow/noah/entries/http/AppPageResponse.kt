package com.ishow.noah.entries.http

class AppPageResponse<T> : AppHttpResponse<Page<T>>() {
    val listData: MutableList<T>?
        get() = data?.list

    val isLastPage: Boolean?
        get() = data?.isLastPage
}

class Page<T> {
    var isLastPage: Boolean? = null
    var list: MutableList<T>? = null
}
