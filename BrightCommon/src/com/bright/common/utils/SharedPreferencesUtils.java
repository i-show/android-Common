/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Set;

public class SharedPreferencesUtils {

    private static final String TAG = "SharedPreferencesUtils";
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";

    private static WeakReference<SharedPreferences> mSharedPreferences;


    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences;
        if (mSharedPreferences == null || mSharedPreferences.get() == null) {
            sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences = new WeakReference<>(sharedPreferences);
        } else {
            sharedPreferences = mSharedPreferences.get();
        }
        return sharedPreferences;
    }

    public static void save(Context context, String key, Object value) {

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        if (value == null || value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException("没有当前类型");
        }

        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static <T> T get(Context context, String key, T value) {
        SharedPreferences sp = getSharedPreferences(context);

        Object result;
        if (value == null || value instanceof String) {
            result = sp.getString(key, (String) value);
        } else if (value instanceof Integer) {
            result = sp.getInt(key, (Integer) value);
        } else if (value instanceof Float) {
            result = sp.getFloat(key, (Float) value);
        } else if (value instanceof Long) {
            result = sp.getLong(key, (Long) value);
        } else if (value instanceof Set) {
            result = sp.getStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException("没有当前类型");
        }

        if (result != null) {
            return (T) result;
        }
        return null;
    }
}
