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

package com.ishow.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by yuhaiyang on 2017/5/25.
 * Toast 显示工具类
 * <p>
 * ToastUtils 集成为单例Toast 预防多次提示
 *
 * @author yuhaiyang
 */
@SuppressWarnings("unused")
public class ToastUtils {
    private static final String TAG = "ToastUtils";

    private static WeakReference<Toast> mToast;


    public static void show(Context context, @StringRes int text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, @StringRes int text, int duration) {
        String content = context.getString(text);
        show(context, content, duration);
    }

    @SuppressLint("ShowToast")
    public static void show(Context context, CharSequence text, int duration) {
        if (context == null) {
            Log.i(TAG, "makeText: context is null");
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                Log.i(TAG, "makeText: activity is alreay finish");
                return;
            }
        }
        Toast toast;
        if (mToast == null || mToast.get() == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, duration);
            mToast = new WeakReference<>(toast);
        } else {
            toast = mToast.get();
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 隐藏Toast
     */
    public static void dismiss() {
        if (mToast == null || mToast.get() == null) {
            Log.i(TAG, "dismiss: toast is null");
            return;
        }

        Toast toast = mToast.get();
        toast.cancel();
    }

}
