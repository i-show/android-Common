package com.ishow.common.utils.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class PermissionTask {

    fun with(activity: AppCompatActivity): PermissionTask {
        return this
    }

    fun with(fragment: Fragment): PermissionTask {
        return this
    }

    fun permissions(vararg permissions: String?): PermissionTask {
        return this
    }

    fun request(): PermissionTask {
        return this
    }
}