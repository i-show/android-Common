/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.ishow.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.ishow.common.R

/**
 * Intent 跳转工具类
 */

object IntentUtils {
    /**
     * 微信包名
     */

    const val WE_CHAT_PACKAGE_NAME = "com.tencent.mm"

    /**
     * 跳转拨号盘界面
     */
    @JvmStatic
    fun gotoDial(context: Context, number: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number.trim { it <= ' ' }))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastUtils.show(context, R.string.exception_intent_open)
        }

    }

    /**
     * 直接拨打电话
     */
    @JvmStatic
    @SuppressLint("MissingPermission")
    fun call(context: Context, number: String) {
        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.trim { it <= ' ' }))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastUtils.show(context, R.string.exception_intent_open)
        }

    }

    /**
     * 跳转 市场进行评分
     */
    @JvmStatic
    @JvmOverloads
    fun gotoMarket(context: Context, packageName: String = context.packageName) {

        if (TextUtils.isEmpty(packageName)) {
            return
        }

        try {
            val uri = Uri.parse("market://details?id=" + packageName.trim { it <= ' ' })
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastUtils.show(context, R.string.exception_intent_open)
        }

    }

    /**
     * 跳转浏览器
     */
    @JvmStatic
    fun gotoBrowser(context: Context, url: String?) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            ToastUtils.show(context, R.string.exception_intent_open)
        }
    }

    /**
     * 打开某一个app
     */
    @JvmStatic
    fun openApp(context: Context, appPackageName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(appPackageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.show(context, R.string.exception_intent_open, Toast.LENGTH_SHORT)
        }

    }

    /**
     * 打开某一个app
     */
    @JvmStatic
    fun openApp(context: Context, appPackageName: String, errorMessage: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(appPackageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.show(context, errorMessage, Toast.LENGTH_SHORT)
        }

    }

    /**
     * 打开某一个app
     */
    @JvmStatic
    fun openApp(context: Context, appPackageName: String, @StringRes errorMessage: Int) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(appPackageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.show(context, errorMessage, Toast.LENGTH_SHORT)
        }
    }

    /**
     * 选择联系人
     */
    @JvmStatic
    fun selectContact(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        activity.startActivityForResult(intent, requestCode)
    }


    /**
     * 跳转 App应用设置界面
     */
    @JvmStatic
    @JvmOverloads
    fun gotoAppSettings(context: Context, packageName: String = context.packageName) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.show(context, R.string.exception_intent_open, Toast.LENGTH_SHORT)
        }

    }
}

