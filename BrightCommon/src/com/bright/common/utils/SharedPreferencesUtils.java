/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
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
    private static final String TYPE_NAME = "share_main_date";
    /**
     * 保存缓存的文件
     */
    private static final String TYPE_CACHE = "share_cache_date";

    private static WeakReference<SharedPreferences> mSharedPreferences;
    private static WeakReference<SharedPreferences> mCacheSharedPreferences;


    public static SharedPreferences getSharedPreferences(Context context) {
        return getSharedPreferences(context, false);
    }

    public static SharedPreferences getSharedPreferences(Context context, boolean cache) {
        SharedPreferences sharedPreferences;

        if (cache) {
            if (mCacheSharedPreferences == null || mCacheSharedPreferences.get() == null) {
                sharedPreferences = context.getSharedPreferences(TYPE_CACHE, Context.MODE_PRIVATE);
                mCacheSharedPreferences = new WeakReference<>(sharedPreferences);
            } else {
                sharedPreferences = mCacheSharedPreferences.get();
            }
        } else {
            if (mSharedPreferences == null || mSharedPreferences.get() == null) {
                sharedPreferences = context.getSharedPreferences(TYPE_NAME, Context.MODE_PRIVATE);
                mSharedPreferences = new WeakReference<>(sharedPreferences);
            } else {
                sharedPreferences = mSharedPreferences.get();
            }
        }


        return sharedPreferences;
    }

    public static void save(Context context, String key, Object value) {
        save(context, key, value, false);
    }

    public static void save(Context context, String key, Object value, boolean isCache) {

        SharedPreferences.Editor editor = getSharedPreferences(context, isCache).edit();

        if (value == null || value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
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
    public static <T> T get(Context context, String key, T defaultValue) {
        return get(context, key, defaultValue, false);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static <T> T get(Context context, String key, T defaultValue, boolean isCache) {
        SharedPreferences sp = getSharedPreferences(context, isCache);

        Object result;
        if (defaultValue == null || defaultValue instanceof String) {
            result = sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            result = sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            result = sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Float) {
            result = sp.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            result = sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Set) {
            result = sp.getStringSet(key, (Set<String>) defaultValue);
        } else {
            throw new IllegalArgumentException("没有当前类型");
        }

        if (result != null) {
            return (T) result;
        }
        return null;
    }
}
