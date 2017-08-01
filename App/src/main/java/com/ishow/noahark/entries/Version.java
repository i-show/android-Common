/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.noahark.entries;

import android.content.Context;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Bright.Yu on 2017/1/20.
 * 版本信息
 */
@SuppressWarnings("WeakerAccess,unused")
public class Version {
    public String id;
    /**
     * 状态
     */

    public int status;
    /**
     * 版本编号
     */
    public int versionCode;
    /**
     * 版本名称
     */
    public String versionName;
    /**
     * 下载地址
     */
    public String address;
    /**
     * 更新描述
     */
    public String description;
    /**
     * 更新时间
     */
    public String updateTime;

    /**
     * 是否强制升级
     */
    @JSONField(serialize = false, deserialize = false)
    public boolean isForceUpdate() {
        return status == Key.STATUS_FORCE_UPDATE;
    }

    /**
     * 下载路径
     */
    @JSONField(serialize = false, deserialize = false)
    public String getDownloadPath() {
        return address;
    }

    /**
     * 获取描述
     */
    @JSONField(serialize = false, deserialize = false)
    public String getDescription(Context context) {
        return description;
    }

    public static final class Key {
        /**
         * 已经是最新版本
         */
        public static final int STATUS_ALREAY_NEWEST = -1;
        /**
         * 有最新版本不需要强制升级
         */
        public static final int STATUS_CAN_UPDATE = 0;
        /**
         * 强制升级
         */
        public static final int STATUS_FORCE_UPDATE = 1;

        /**
         * 缓存
         */
        public static final String CACHE = "key_version_cache";

        /**
         * 是否需要升级缓存
         */
        public static final String IGNORE_NOW = "key_notify_ignore_this_enter";

        /**
         * 当前版本给忽略掉
         */
        public static final String IGNORE_VERSION = "key_notify_ignore_version";
    }
}
