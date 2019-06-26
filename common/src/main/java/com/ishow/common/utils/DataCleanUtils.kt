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

import android.content.Context
import android.os.Environment

import java.io.File
import java.math.BigDecimal

@Suppress("MemberVisibilityCanBePrivate")
object DataCleanUtils {
    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    fun cleanInternalCache(context: Context) {
        deleteFilesByDirectory(context.cacheDir)
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    fun cleanDatabases(context: Context) {
        var file = context.getDatabasePath("cache")
        file = file.parentFile
        deleteFilesByDirectory(file)
    }


    /**
     * 按名字清除本应用数据库
     */
    fun cleanDatabaseByName(context: Context, dbName: String) {
        context.deleteDatabase(dbName)
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    fun cleanFiles(context: Context) {
        deleteFilesByDirectory(context.filesDir)
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFilesByDirectory(context.externalCacheDir)
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    fun cleanCustomCache(filePath: String) {
        deleteFilesByDirectory(File(filePath))
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    fun cleanApplicationData(context: Context, vararg filepath: String) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanFiles(context)
        for (filePath in filepath) {
            cleanCustomCache(filePath)
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private fun deleteFilesByDirectory(directory: File?) {
        if (directory != null && directory.exists() && directory.isDirectory) {
            for (item in directory.listFiles()) {
                item.delete()
            }
        }
    }

    /**
     * 获取缓存大小
     */
    @Throws(Exception::class)
    fun getCacheSize(file: File): String {
        return getFormatSize(getFolderSize(file).toDouble())
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 获取对应文件夹大小
     */
    @Throws(Exception::class)
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (child in fileList) {
                size += if (child.isDirectory) {
                    getFolderSize(child)
                } else {
                    child.length()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }


}
