package com.ishow.common.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import com.ishow.common.utils.log.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Modify by y.haiyang on 2018/6/26
 */
@SuppressWarnings("unused")
public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 1KB
     */
    private static final long KB_1 = 1024;
    /**
     * 1MB
     */
    private static final long MB_1 = 1024 * 1024;
    /**
     * 1GB
     */
    private static final long GB_1 = 1024 * 1024 * 1024;

    /**
     * 转换文件大小
     * 默认2位小数
     */
    public static String formatFileSize(final long fileSize) {
        return formatFileSize(fileSize, 2);
    }

    /**
     * 转换文件大小
     *
     * @param fileSize 文件大小 单位B
     * @param rounding 保存的小数位数
     */
    @SuppressWarnings("WeakerAccess")
    public static String formatFileSize(final long fileSize, final int rounding) {
        if (fileSize < KB_1) {
            return MathUtils.rounding(fileSize, rounding);
        } else if (fileSize < MB_1) {
            return MathUtils.rounding((double) fileSize / KB_1, rounding);
        } else if (fileSize < GB_1) {
            return MathUtils.rounding((double) fileSize / MB_1, rounding);
        } else {
            return MathUtils.rounding((double) fileSize / GB_1, rounding);
        }
    }

    /**
     * 获取目录文件个数
     */
    @SuppressWarnings("WeakerAccess")
    public static long getFileList(File dir) {
        long count;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);
                count--;
            }
        }
        return count;
    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     * 注意： 返回的单位为 kb
     */
    public static long getFreeDiskSpace() {
        // 外置SD卡没有安装
        if (!TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState())) {
            return -1;
        }
        long freeSpace = 0;
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            freeSpace = availableBlocks * blockSize / 1024;
        } catch (Exception e) {
            LogManager.e(TAG, e.getMessage());
        }

        return freeSpace;
    }

    /**
     * 重命名
     *
     * @param oldName 当前文件的名称
     * @param newName 需要修正为的名称
     * @return 是否修改成功
     */
    public static boolean rename(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }


    /**
     * 删除文件
     *
     * @param path 路径地址
     * @return '
     * -------- true 删除成功
     * -------- false 删除失败
     */
    public static boolean delete(String path) {
        return delete(new File(path));
    }

    /**
     * 删除文件
     *
     * @param path 路径地址
     * @param name 文件名称
     * @return '
     * -------- true 删除成功
     * -------- false 删除失败
     */
    public static boolean delete(String path, String name) {
        return delete(new File(path, name));
    }

    /**
     * 删除文件
     *
     * @param filePath 路径地址
     * @return '
     * -------- true 删除成功
     * -------- false 删除失败
     */
    public static boolean delete(File filePath) {
        if (filePath == null) {
            return false;
        }
        if (!filePath.exists()) {
            return true;
        }

        if (filePath.isFile()) {
            return filePath.delete();
        }

        for (File file : filePath.listFiles()) {
            delete(file);
        }
        return true;
    }


    /**
     * 获取当前文件夹下，所有的文件（文件夹除外）
     */
    public static ArrayList<String> getFileList(String path) {
        ArrayList<String> result = new ArrayList<>();

        File root = new File(path);
        if (!root.exists()) {
            LogManager.e(TAG, "getFileList: path is not exists");
            return result;
        }

        File[] fileList = root.listFiles();
        if (fileList == null || fileList.length == 0) {
            return result;
        }

        for (File file : fileList) {
            if (file.isFile()) {
                result.add(file.getName());
            }
        }

        return result;
    }


    /**
     * 获取当前文件夹下，所有的文件的绝对路径（文件夹除外）
     */
    public static List<String> getListAbsolutePath(String path) {
        ArrayList<String> result = new ArrayList<>();

        File root = new File(path);
        if (!root.exists()) {
            LogManager.e(TAG, "getFileList: path is not exists");
            return result;
        }

        File[] fileList = root.listFiles();
        if (fileList == null || fileList.length == 0) {
            return result;
        }

        for (File file : fileList) {
            if (file.isFile()) {
                result.add(file.getAbsolutePath());
            }
        }

        return result;
    }

    /**
     * 截取路径名
     *
     * @return 文件路径
     */
    public static String getPathName(String path) {
        int start = path.lastIndexOf(File.separator) + 1;
        int end = path.length();
        return path.substring(start, end);
    }


    /**
     * 文件复制
     *
     * @param oldPathStr 原路径
     * @param newPathStr 新路径
     */
    public static void copy(String oldPathStr, String newPathStr) {
        LogManager.i(TAG, "copy: oldPathStr = " + oldPathStr);
        LogManager.i(TAG, "copy: newPathStr = " + newPathStr);

        if (TextUtils.isEmpty(oldPathStr) || TextUtils.isEmpty(newPathStr)) {
            LogManager.e(TAG, "copy: oldPath or newPath is empty");
            return;
        }
        if (isChild(oldPathStr, newPathStr)) {
            throw new IllegalStateException("newPath must not oldPath child");
        }

        File oldPath = new File(oldPathStr);
        if (oldPath.isFile()) {
            LogManager.e(TAG, "copy: oldPath is a file not a path");
            return;
        }

        File[] oldFileList = oldPath.listFiles();
        if (oldFileList == null || oldFileList.length == 0) {
            Log.e(TAG, "copy: oldFileList is empty");
            return;
        }

        File newPath = new File(newPathStr);
        if (!newPath.exists()) {
            //noinspection ResultOfMethodCallIgnored
            newPath.mkdirs();
        }

        try {
            FileInputStream inputStream;
            FileOutputStream outputStream;
            File newFile;
            for (File oldFile : oldFileList) {
                newFile = new File(newPath, oldFile.getName());
                if (oldFile.isFile()) {
                    inputStream = new FileInputStream(oldFile);
                    outputStream = new FileOutputStream(newFile);
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = inputStream.read(b)) != -1) {
                        outputStream.write(b, 0, len);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } else {
                    //noinspection ResultOfMethodCallIgnored
                    newFile.mkdir();
                    copy(oldFile.getAbsolutePath(), newFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.e(TAG, "copy: e = " + e.toString());
        }
    }


    /**
     * 获取SDcard路径
     */
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
        } else {
            return null;
        }

    }

    /**
     * 是否是子路径
     * 判断 target 是否是 original 的子路径
     *
     * @param original 原始路径
     * @param target   目标路径
     */
    public static boolean isChild(String original, String target) {
        if (original == null || target == null) {
            LogManager.e(TAG, "isChild: original or target is null");
            return false;
        }

        if (original.endsWith(File.separator)) {
            original = original.substring(0, original.length() - 1);
        }

        if (target.endsWith(File.separator)) {
            target = target.substring(0, target.length() - 1);
        }

        if (TextUtils.equals(original, target)) {
            return true;
        }

        String result = target.substring(original.length(), target.length());
        return result.startsWith(File.separator);
    }


}
