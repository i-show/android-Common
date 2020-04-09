package com.ishow.common.utils.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ishow.common.app.activity.BaseActivity

/**
 * 请求权限的Activity
 */
class RequestPermissionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = intent.getStringArrayExtra(KEY_PERMISSIONS)
        requestPermission(permissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        parseResult(requestCode, permissions, grantResults)
        PermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    private fun requestPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE)
    }

    private fun parseResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != REQUEST_PERMISSION_CODE) return
        val granted = arrayListOf<String>()
        val denied = arrayListOf<String>()

        for ((i, permission) in permissions.withIndex()) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission)
            } else {
                denied.add(permission)
            }
        }

        val intent = Intent(PermissionManager2.ACTION_PERMISSION_RESULT)
        intent.putExtra("g", granted.toArray())
        intent.putExtra("d", denied.toArray())

        val manager = LocalBroadcastManager.getInstance(this)
        manager.sendBroadcast(intent)

        finish()
    }


    companion object {
        private const val REQUEST_PERMISSION_CODE = 2020
        internal const val KEY_PERMISSIONS = "key_permissions"
    }
}