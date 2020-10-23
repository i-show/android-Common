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

package com.ishow.common.utils

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.ishow.common.extensions.appScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by yuhaiyang on 2017/5/25.
 * Toast 显示工具类
 */
object ToastUtils {
    private const val TAG = "ToastUtils"

    @JvmStatic
    @JvmOverloads
    fun show(context: Context?, @StringRes text: Int, duration: Int = Toast.LENGTH_SHORT) {
        if (context == null || text == 0) {
            Log.i(TAG, "makeText: context is null")
            return
        }
        val content = context.getString(text)
        show(context, content, duration)
    }

    @JvmStatic
    @JvmOverloads
    fun show(context: Context?, text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        if (context == null || text.isNullOrBlank()) {
            Log.i(TAG, "makeText: context is null")
            return
        }

        if (context is Activity) {
            val activity = context as Activity?
            if (activity!!.isFinishing) {
                Log.i(TAG, "makeText: activity is already finish")
                return
            }
        }

        if (isMainThread()) {
            Toast.makeText(context, text, duration).show()
        } else {
            mainThread { Toast.makeText(context, text, duration).show() }
        }
    }


    /**
     * 判断是否在主线程
     */
    @Suppress("MemberVisibilityCanBePrivate")
    private fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 通过协程  在主线程上运行
     */
    private fun mainThread(block: () -> Unit) = appScope.launch(Dispatchers.Main) {
        block()
    }
}
