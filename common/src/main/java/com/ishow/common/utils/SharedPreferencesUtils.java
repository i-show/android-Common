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

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Set;

@Deprecated
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

    /**
     * 保存SharePreference 分2个模块
     * 1. 保存缓存信息的 例如请求的的Json 缓存
     * 2. 保存基本信息 例如用户名 以及配置信息
     * <p/>
     * 区分的目的： 版本升级 cache类型的可以进行清除 但是基本信息的不能清理
     */
    private static WeakReference<SharedPreferences> mSharedPreferences;
    private static WeakReference<SharedPreferences> mCacheSharedPreferences;


    @SuppressWarnings("WeakerAccess")
    public static SharedPreferences getSharedPreferences(Context context) {
        return getSharedPreferences(context, false);
    }

    @SuppressWarnings("WeakerAccess")
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

    /**
     * 保存为缓存
     */
    public static void saveCache(Context context, String key, Object value) {
        save(context, key, value, true);
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
    public static <T> T getCache(Context context, String key, T defaultValue) {
        return get(context, key, defaultValue, true);
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

    /**
     * 移除某一组数据
     */
    public static void remove(Context context, String key) {
        remove(context, key, false);
    }

    /**
     * 移除某一组数据
     */
    @SuppressWarnings("WeakerAccess")
    public static void remove(Context context, String key, boolean cache) {
        SharedPreferences sp = getSharedPreferences(context, cache);
        sp.edit().remove(key).apply();
    }

    /**
     * 清空数据
     */
    @SuppressWarnings("WeakerAccess")
    public static void cleanCache(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context, true);
        sharedPreferences.edit().clear().apply();
    }

    /**
     * 清空所有数据
     */
    public static void cleanAll(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        sharedPreferences.edit().clear().apply();
        cleanCache(context);
    }
}
