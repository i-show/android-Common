package com.ishow.common.extensions

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import com.ishow.common.utils.image.ImageUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * 转换成Base64
 */
fun Bitmap.toBase64(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): String {
    var result = ""
    val outputStream = ByteArrayOutputStream()
    try {
        compress(format, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        val bitmapBytes = outputStream.toByteArray()
        result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return result
}

/**
 * 转换成Drawable
 */
fun Bitmap.toDrawable(context: Context): Drawable {
    return BitmapDrawable(context.resources, this)
}

/**
 * 转换成ByteArray
 */
fun Bitmap.toByte(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): ByteArray {
    val outputStream = ByteArrayOutputStream()
    compress(format, 100, outputStream)
    return outputStream.toByteArray()
}

/**
 * 把Bitmap附着颜色
 */
fun Bitmap.tint(color: Int): Bitmap {
    val canvas = Canvas(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    val background = ImageUtils.getPureBitmap(width, height, color)
    canvas.drawBitmap(background, 0f, 0f, paint)
    background.recycle()
    return this
}

/**
 * 旋转角度
 */
fun Bitmap.rotate(angle: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * 压缩Bitmap
 * @param format 压缩后的格式
 * @param maxSize 最大大小
 */
fun Bitmap.compress(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, maxSize: Int = 2048): Bitmap {
    var quality = 100
    val outputStream = ByteArrayOutputStream()
    compress(format, quality, outputStream)
    // 循环判断如果压缩后图片是否大于指定大小,大于继续压缩
    while (outputStream.toByteArray().size / 1024 > maxSize) {
        outputStream.reset()
        quality -= 10
        compress(format, quality, outputStream)
    }
    return outputStream.toByteArray().toBitmap()
}

/**
 * 保存
 */
fun Bitmap.save(
    context: Context,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): String {
    val path = ImageUtils.genImageName(context, format)
    return save(path, format, quality)
}

/**
 * 保存
 */
fun Bitmap.save(
    path: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): String {
    val file = File(path)
    val out = FileOutputStream(file)
    try {
        compress(format, quality, out)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        out.flush()
        out.close()
        recycle()
        return file.absolutePath
    }
}