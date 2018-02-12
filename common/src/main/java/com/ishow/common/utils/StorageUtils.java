package com.ishow.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by yuhaiyang on 2017/12/12.
 * 存储服务
 */

public class StorageUtils {
    private static final String TAG = "AppRouter";
    /**
     * 保存在手机里面的文件名
     */
    private static final String GROUP_MAIN = "share_main_date";

    public static Executor with(Context context) {
        return new Executor(context);
    }


    public static class Executor {
        private Context context;
        /**
         * 区块
         */
        private String group;
        /**
         * key
         */
        private String key;
        /**
         * Value
         */
        private Object value;

        private Executor(Context context) {
            this.context = context;
            this.group = GROUP_MAIN;
        }

        public Executor group(@NonNull String group) {
            this.group = group;
            return this;
        }

        /**
         * 参数
         */
        public Executor key(@NonNull String key) {
            this.key = key;
            return this;
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        public Executor param(@NonNull String key, int value) {
            this.key = key;
            this.value = value;
            return this;
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        public Executor param(@NonNull String key, boolean value) {
            this.key = key;
            this.value = value;
            return this;
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        public Executor param(@NonNull String key, float value) {
            this.key = key;
            this.value = value;
            return this;
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        public Executor param(@NonNull String key, long value) {
            this.key = key;
            this.value = value;
            return this;
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        public Executor param(@NonNull String key, @NonNull String value) {
            this.key = key;
            this.value = value;
            return this;
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        public Executor param(@NonNull String key, @NonNull Set<String> value) {
            this.key = key;
            this.value = value;
            return this;
        }

        public void save() {
            SharedPreferences sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (value == null || value instanceof String) {
                editor.putString(key, (String) value);
                editor.apply();
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
                editor.apply();
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
                editor.apply();
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
                editor.apply();
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
                editor.apply();
            } else if (value instanceof Set) {
                //noinspection unchecked
                editor.putStringSet(key, (Set<String>) value);
                editor.apply();
            }
        }


        public <T> T get(T defaultValue) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE);
            Object result;
            if (defaultValue == null || defaultValue instanceof String) {
                result = sharedPreferences.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                result = sharedPreferences.getBoolean(key, (Boolean) defaultValue);
            } else if (defaultValue instanceof Integer) {
                result = sharedPreferences.getInt(key, (Integer) defaultValue);
            } else if (defaultValue instanceof Float) {
                result = sharedPreferences.getFloat(key, (Float) defaultValue);
            } else if (defaultValue instanceof Long) {
                result = sharedPreferences.getLong(key, (Long) defaultValue);
            } else if (defaultValue instanceof Set) {
                result = sharedPreferences.getStringSet(key, (Set<String>) defaultValue);
            } else {
                throw new IllegalArgumentException("没有当前类型");
            }

            if (result != null) {
                return (T) result;
            }
            return null;
        }


        public void remove() {
            SharedPreferences sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key).apply();
        }

        public void clear(){
            SharedPreferences sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();
        }
    }

}
