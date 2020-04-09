package com.ishow.common.utils.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ishow.common.app.activity.BaseActivity
import com.ishow.common.extensions.toJSON

/**
 * 请求权限的Activity
 */
class RequestPermissionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = intent.getStringArrayExtra(PermissionTask.KEY_PERMISSIONS)
        val taskId = intent.getIntExtra(PermissionTask.KEY_TASK_ID, 0)
        requestPermission(taskId, permissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        parseResult(requestCode, permissions, grantResults)
    }

    private fun requestPermission(taskId: Int, permissions: Array<String>?) {
        if (permissions == null) {
            requestPermissionFailed(taskId)
            return
        }
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE + taskId)
    }

    private fun parseResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val taskId = requestCode - REQUEST_PERMISSION_CODE
        val granted = arrayListOf<String>()
        val denied = arrayListOf<String>()

        for ((i, permission) in permissions.withIndex()) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission)
            } else {
                denied.add(permission)
            }
        }

        if (denied.size <= 0) {
            requestPermissionSuccess(taskId, granted)
        } else {
            requestPermissionFailed(taskId, granted, denied)
        }
    }

    private fun requestPermissionSuccess(taskId: Int, granted: ArrayList<String>? = null) {
        val info = PermissionInfo(PermissionInfo.Status.Success)
        info.granted = granted

        val intent = Intent(PermissionManager.ACTION_PERMISSION_RESULT + taskId)
        intent.putExtra(PermissionManager.KEY_PERMISSION_DETAIL, info.toJSON())

        val manager = LocalBroadcastManager.getInstance(this)
        manager.sendBroadcast(intent)

        finish()
    }

    private fun requestPermissionFailed(taskId: Int, granted: ArrayList<String>? = null, denied: ArrayList<String>? = null) {
        val info = PermissionInfo(PermissionInfo.Status.Failed)
        info.granted = granted
        info.denied = denied

        val intent = Intent(PermissionManager.ACTION_PERMISSION_RESULT + taskId)
        intent.putExtra(PermissionManager.KEY_PERMISSION_DETAIL, info.toJSON())

        val manager = LocalBroadcastManager.getInstance(this)
        manager.sendBroadcast(intent)
        finish()
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 2020
    }
}