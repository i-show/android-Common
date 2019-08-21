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
package com.ishow.common.utils.image

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config
import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import com.ishow.common.extensions.rotate
import com.ishow.common.extensions.save
import com.ishow.common.utils.AppUtils
import com.ishow.common.utils.StringUtils
import java.io.File
import java.util.*


object ImageUtils {
    private const val TAG = "ImageUtils"
    /**
     * 压缩图片后最大边长度
     */
    private const val MAX_SIZE = 2000
    /**
     * 默认的的压缩图片质量
     */
    private const val DEFAULT_COMPRESS_QUALITY = 90

    /**
     * 获取一个纯色的Bitmap
     */
    fun getPureBitmap(width: Int, height: Int, color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(color)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        return bitmap
    }

    /**
     * 获取 图片旋转 的角度
     */
    fun getExifOrientation(filepath: String): Int {
        var degree = 0
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(filepath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (exif == null) {
            Log.i(TAG, "getExifOrientation: exif is null")
            return degree
        }

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        }
        return degree
    }

    /**
     * 压缩图片
     */
    @JvmOverloads
    fun compressImage(
        context: Context,
        photoPath: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        quality: Int = DEFAULT_COMPRESS_QUALITY
    ): String? {
        var bitmap: Bitmap? = null
        val resultPath = genImageName(context, format)
        try {
            val opts = BitmapFactory.Options()
            opts.inJustDecodeBounds = true
            BitmapFactory.decodeFile(photoPath, opts)
            opts.inSampleSize = calculateInSampleSize(opts)
            opts.inJustDecodeBounds = false
            val angle = getExifOrientation(photoPath)
            bitmap = BitmapFactory.decodeFile(photoPath, opts)
            bitmap = bitmap.rotate(angle)
            bitmap.save(resultPath, format, quality)
        } catch (e: Exception) {
            return null
        } finally {
            bitmap?.recycle()
        }

        return resultPath
    }

    /**
     * 计算压缩比例值
     *
     * @param options 解析图片的配置信息
     */
    fun calculateInSampleSize(options: BitmapFactory.Options): Int {
        // 保存图片原宽高值
        val width = options.outWidth
        val height = options.outHeight
        // 初始化压缩比例为1
        var inSampleSize = 1
        // 压缩比例值每次循环两倍增加,
        // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
        if (width > height) {
            while (width / inSampleSize >= MAX_SIZE) {
                inSampleSize *= 2
            }
        } else {
            while (height / inSampleSize >= MAX_SIZE) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 生成图片名称
     */
    @JvmOverloads
    fun genImageFile(
        context: Context,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): File {
        var folder = context.externalCacheDir
        if (null == folder) {
            val target = Environment.getExternalStorageDirectory()
            folder = File(target, AppUtils.getAppName(context))
        }

        if (!folder.exists()) {
            folder.mkdirs()
        }
        val suffix = when (format) {
            Bitmap.CompressFormat.JPEG -> ".jpg"
            Bitmap.CompressFormat.PNG -> ".png"
            Bitmap.CompressFormat.WEBP -> ".webp"
        }

        val name = UUID.randomUUID().toString().replace("-", "") + suffix
        return File(folder, name)
    }

    /**
     * 生成随机的名字
     */
    @JvmOverloads
    fun genImageName(
        context: Context,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): String {
        return genImageFile(context, format).absolutePath
    }
}
