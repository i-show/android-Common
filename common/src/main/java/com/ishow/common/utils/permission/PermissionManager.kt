package com.ishow.common.utils.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionManager {

    companion object {
        internal const val ACTION_PERMISSION_RESULT = "com.ishow.permission.ACTION_PERMISSION"
        internal const val KEY_PERMISSION_DETAIL = "key_permission_detail_"

        private var taskId = 0
        fun newTask(context: Context?): PermissionTask {
            return PermissionTask(taskId++, context)
        }


        /**
         * Check if the calling context has a set of permissions.
         *
         * @param context     [Context].
         * @param permissions one or more permissions.
         * @return true, other wise is false.
         */
        fun hasPermission(context: Context, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true

            for (permission in permissions) {
                val hasPermission = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                if (!hasPermission) return false
            }
            return true
        }
    }
}