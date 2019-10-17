package com.ishow.common.utils

import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.ishow.common.utils.log.LogUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

/**
 * Modify by y.haiyang on 2018/6/26
 */
@Suppress("unused")
object FileUtils {
    private const val TAG = "FileUtils"

    /**
     * 1KB
     */
    private const val KB_1: Long = 1024
    /**
     * 1MB
     */
    private const val MB_1 = (1024 * 1024).toLong()
    /**
     * 1GB
     */
    private const val GB_1 = (1024 * 1024 * 1024).toLong()

    /**
     * 转换文件大小
     *
     * @param fileSize 文件大小 单位B
     * @param rounding 保存的小数位数
     */
    @JvmOverloads
    fun formatFileSize(fileSize: Long, rounding: Int = 2): String {
        return when {
            fileSize < KB_1 -> {
                MathUtils.rounding(fileSize.toFloat(), rounding)
            }
            fileSize < MB_1 -> {
                MathUtils.rounding(fileSize.toDouble() / KB_1, rounding)
            }
            fileSize < GB_1 -> {
                MathUtils.rounding(fileSize.toDouble() / MB_1, rounding)
            }
            else -> {
                MathUtils.rounding(fileSize.toDouble() / GB_1, rounding)
            }
        }
    }

    /**
     * 获取目录文件个数
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun getFileList(dir: File): Long {
        var count: Long = 0
        val files = dir.listFiles() ?: return count
        count = files.size.toLong()

        files.forEach {
            if (it.isDirectory) {
                count += getFileList(it)
                count--
            }
        }
        return count
    }

    /**
     * 重命名
     *
     * @param oldName 当前文件的名称
     * @param newName 需要修正为的名称
     * @return 是否修改成功
     */
    fun rename(oldName: String, newName: String): Boolean {
        val f = File(oldName)
        return f.renameTo(File(newName))
    }


    /**
     * 删除文件
     *
     * @param path 路径地址
     */
    fun delete(path: String): Boolean {
        return delete(File(path))
    }

    /**
     * 删除文件
     *
     * @param path 路径地址
     * @param name 文件名称
     */
    fun delete(path: String, name: String): Boolean {
        return delete(File(path, name))
    }

    /**
     * 删除文件
     * @param filePath 路径地址
     */
    fun delete(filePath: File?): Boolean {
        if (filePath == null) {
            return false
        }
        if (!filePath.exists()) {
            return true
        }

        if (filePath.isFile) {
            return filePath.delete()
        }

        filePath.listFiles()?.forEach {
            delete(it)
        }

        return true
    }


    /**
     * 获取当前文件夹下，所有的文件（文件夹除外）
     */
    fun getFileList(path: String): ArrayList<String> {
        val result = ArrayList<String>()

        val root = File(path)
        if (!root.exists()) {
            LogUtils.e(TAG, "getFileList: path is not exists")
            return result
        }

        val fileList = root.listFiles()
        if (fileList.isNullOrEmpty()) {
            return result
        }

        for (file in fileList) {
            if (file.isFile) {
                result.add(file.name)
            }
        }
        return result
    }


    /**
     * 获取当前文件夹下，所有的文件的绝对路径（文件夹除外）
     */
    fun getListAbsolutePath(path: String): List<String> {
        val result = ArrayList<String>()

        val root = File(path)
        if (!root.exists()) {
            LogUtils.e(TAG, "getFileList: path is not exists")
            return result
        }

        val fileList = root.listFiles()
        if (fileList.isNullOrEmpty()) {
            return result
        }

        for (file in fileList) {
            if (file.isFile) {
                result.add(file.absolutePath)
            }
        }

        return result
    }

    /**
     * 截取路径名
     *
     * @return 文件路径
     */
    fun getPathName(path: String): String {
        val start = path.lastIndexOf(File.separator) + 1
        return path.substring(start, path.length)
    }


    /**
     * 文件复制
     *
     * @param oldPathStr 原路径
     * @param newPathStr 新路径
     */
    fun copy(oldPathStr: String, newPathStr: String) {
        if (TextUtils.isEmpty(oldPathStr) || TextUtils.isEmpty(newPathStr)) {
            LogUtils.e(TAG, "copy: oldPath or newPath is empty")
            return
        }
        check(!isChild(oldPathStr, newPathStr)) { "newPath must not oldPath child" }

        val oldPath = File(oldPathStr)
        if (oldPath.isFile) {
            LogUtils.e(TAG, "copy: oldPath is a file not a path")
            return
        }

        val oldFileList = oldPath.listFiles()
        if (oldFileList.isNullOrEmpty()) {
            Log.e(TAG, "copy: oldFileList is empty")
            return
        }

        val newPath = File(newPathStr)
        if (!newPath.exists()) {
            newPath.mkdirs()
        }

        try {
            var inputStream: FileInputStream
            var outputStream: FileOutputStream
            var newFile: File
            for (oldFile in oldFileList) {
                newFile = File(newPath, oldFile.name)
                if (oldFile.isFile) {
                    inputStream = FileInputStream(oldFile)
                    outputStream = FileOutputStream(newFile)
                    val b = ByteArray(8192)
                    do {
                        val length = inputStream.read(b)
                        if (length < 0) break
                        outputStream.write(b, 0, length)
                    } while (true)

                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                } else {
                    newFile.mkdir()
                    copy(oldFile.absolutePath, newFile.absolutePath)
                }
            }
        } catch (e: Exception) {
            LogUtils.e(TAG, "copy: e = $e")
        }

    }

    /**
     * 是否是子路径
     * 判断 target 是否是 original 的子路径
     *
     * @param original 原始路径
     * @param target   目标路径
     */
    private fun isChild(original: String?, target: String?): Boolean {
        var originalPath = original
        var targetPath = target
        if (originalPath == null || targetPath == null) {
            LogUtils.e(TAG, "isChild: original or target is null")
            return false
        }

        if (originalPath.endsWith(File.separator)) {
            originalPath = originalPath.substring(0, originalPath.length - 1)
        }

        if (targetPath.endsWith(File.separator)) {
            targetPath = targetPath.substring(0, targetPath.length - 1)
        }

        if (TextUtils.equals(originalPath, targetPath)) {
            return true
        }

        val result = targetPath.substring(originalPath.length, targetPath.length)
        return result.startsWith(File.separator)
    }

}
