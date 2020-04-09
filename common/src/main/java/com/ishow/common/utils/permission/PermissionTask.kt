package com.ishow.common.utils.permission

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ishow.common.extensions.parseJSON

class PermissionTask internal constructor(val id: Int, val context: Context?) {

    private var permissions: Array<out String>? = null
    private var callback: RequestPermissionCallback? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val manager = LocalBroadcastManager.getInstance(context)
            manager.unregisterReceiver(this)

            if (intent == null) {
                callback?.invoke(PermissionInfo(PermissionInfo.Status.Failed))
                return
            }

            val infoStr = intent.getStringExtra(PermissionManager.KEY_PERMISSION_DETAIL)
            if (infoStr.isNullOrEmpty()) {
                callback?.invoke(PermissionInfo(PermissionInfo.Status.Failed))
                return
            }

            val info: PermissionInfo = infoStr.parseJSON()
            callback?.invoke(info)
        }
    }

    init {

        context?.let {
            val filter = IntentFilter(PermissionManager.ACTION_PERMISSION_RESULT + id)

            val manager = LocalBroadcastManager.getInstance(context)
            manager.registerReceiver(receiver, filter)
        }

    }

    fun permissions(vararg permissions: String): PermissionTask {
        this.permissions = permissions
        return this
    }

    fun callback(callback: RequestPermissionCallback?): PermissionTask {
        this.callback = callback
        return this
    }

    fun request(): PermissionTask {
        if (context == null || permissions == null) {
            callback?.invoke(PermissionInfo(PermissionInfo.Status.Failed))
            return this
        }

        val permissionArray = permissions!!

        if (PermissionManager.hasPermission(context, *permissionArray)) {
            val info = PermissionInfo(PermissionInfo.Status.Success)
            info.granted = permissionArray.toList()
            callback?.invoke(info)
            return this
        }

        val intent = Intent(context, RequestPermissionActivity::class.java)
        intent.putExtra(KEY_TASK_ID, id)
        intent.putExtra(KEY_PERMISSIONS, permissionArray)
        context.startActivity(intent)
        return this
    }


    companion object {
        internal const val KEY_TASK_ID = "key_request_permission_id"
        internal const val KEY_PERMISSIONS = "key_request_permissions"
    }

}

typealias RequestPermissionCallback = ((info: PermissionInfo) -> Unit)