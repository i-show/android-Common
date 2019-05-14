/*
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

package com.ishow.noah.manager

import com.ishow.common.widget.watermark.WaterMarkHelp
import com.ishow.noah.BuildConfig
import com.ishow.noah.modules.init.splash.SplashActivity

/**
 * 配置管理器
 */

class ConfigureManager private constructor() {

    /**
     * 配置文件初始化
     */
    @Suppress("UNUSED_PARAMETER")
    fun init(context: SplashActivity) {

        WaterMarkHelp.show(BuildConfig.VERSION_TYPE != BuildConfig.VERSION_PROD)
        WaterMarkHelp.defaultText(BuildConfig.VERSION_DESCRIPTION)
    }

    companion object {
        private val TAG = "ConfigureManager"
        /**
         * 这个东西使用后可以被回收
         */
        @Volatile
        private var sInstance: ConfigureManager? = null

        val instance: ConfigureManager
            get() {

                if (sInstance == null) {
                    synchronized(ConfigureManager::class.java) {
                        if (sInstance == null) {
                            sInstance = ConfigureManager()
                        }
                    }
                }

                return sInstance!!
            }
    }


}
