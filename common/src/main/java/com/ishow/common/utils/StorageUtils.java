package com.ishow.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuhaiyang on 2017/12/12.
 * 存储服务
 */

public class StorageUtils {
    private static final String TAG = "StorageUtils";
    /**
     * 保存在手机里面的文件名
     */
    private static final String GROUP_MAIN = "share_main_date";
    /**
     * 时间超时保存的db
     * 这里故意多加了2个e
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static final String EXPIRE_SUFFIX = "enter_port_expireee";

    public static Executor with(Context context) {
        return new Executor(context);
    }


    @SuppressWarnings("unused")
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

        /**
         * 过期时间
         */
        private long expireTime;

        private Executor(Context context) {
            this.context = context;
            this.group = GROUP_MAIN;
        }

        /**
         * 分组信息
         */
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

        /**
         * 过期时间
         *
         * @param time 多久过期
         * @param unit 时间单位
         */
        public Executor expire(@IntRange(from = 1) int time, @NonNull TimeUnit unit) {
            expireTime = TimeUnit.MILLISECONDS.convert(time, unit);
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

            if (expireTime > 0) {
                SharedPreferences expireSp = context.getSharedPreferences(group + EXPIRE_SUFFIX, Context.MODE_PRIVATE);
                SharedPreferences.Editor expireEditor = expireSp.edit();
                expireEditor.putLong(key, System.currentTimeMillis() + expireTime);
                expireEditor.apply();
            }

        }


        public <T> T get(T defaultValue) {
            SharedPreferences expireSp = context.getSharedPreferences(group + EXPIRE_SUFFIX, Context.MODE_PRIVATE);
            long expireTime = expireSp.getLong(key, 0);
            if (expireTime > 0 && expireTime < System.currentTimeMillis()) {
                remove();
                return defaultValue;
            }

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

            SharedPreferences expireSp = context.getSharedPreferences(group + EXPIRE_SUFFIX, Context.MODE_PRIVATE);
            SharedPreferences.Editor expireEditor = expireSp.edit();
            expireEditor.remove(key).apply();
        }

        public void clear() {
            SharedPreferences sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();

            SharedPreferences expireSp = context.getSharedPreferences(group + EXPIRE_SUFFIX, Context.MODE_PRIVATE);
            SharedPreferences.Editor expireEditor = expireSp.edit();
            expireEditor.clear().apply();
        }
    }

}
