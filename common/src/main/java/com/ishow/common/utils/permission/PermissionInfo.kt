package com.ishow.common.utils.permission

class PermissionInfo(val status: Int) {
    var granted: List<String>? = null
    var denied: List<String>? = null

    object Status {
        const val Success = 0
        const val Failed = 1
    }
}