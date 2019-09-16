package com.ishow.noah.entries.http

class AppPageResponse<T> : AppHttpResponse<Page<T>>()

class Page<T> {
    var totalCount: Int? = null
    var list: List<T>? = null
}
