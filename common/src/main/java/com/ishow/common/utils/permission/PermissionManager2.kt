package com.ishow.common.utils.permission

class PermissionManager2 {

    companion object {
        internal const val ACTION_PERMISSION_RESULT = "com.ishow.permission.ACTION_PERMISSION"

        fun newTask(): PermissionTask {
            return PermissionTask()
        }
    }
}