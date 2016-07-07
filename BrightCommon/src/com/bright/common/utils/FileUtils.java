/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by yuhaiyang on 15-10-7.
 */
public class FileUtils {
    public static final String TAG = FileUtils.class.getSimpleName();
    public static final String LOCAL_URI_HEADER = "file://";
    public static final String CROP_CACHE_FOLDER = "nuskin";

    public static boolean delete(Uri uri) {
        String uriStr = uri.toString();
        String path = uriStr.substring(LOCAL_URI_HEADER.length());
        Log.i(TAG, "path = " + path);
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        } else {
            Log.i(TAG, "file is not exists");
            return false;
        }
    }

    public static String getPathFromUri(Uri uri) {
        String uriStr = uri.toString();
        return uriStr.substring(LOCAL_URI_HEADER.length());
    }

    public static File getFileFromUri(Uri uri) {
        String uriStr = uri.toString();
        String path = uriStr.substring(LOCAL_URI_HEADER.length());
        Log.i(TAG, "path = " + path);
        return new File(path);
    }

    public static String getFileName(String path) {
        String names[] = path.split("/");
        return names[names.length - 1];
    }


    public static String compressImageAndSaveCache(Context context, Bitmap image, int size) {
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;// 每次都减少20
        }
        return saveCacheBitmap(context, image, options);
    }


    public static File getCachePhoto(Context context) {
        File target = context.getExternalCacheDir();

        if (null == target) {
            target = Environment.getExternalStorageDirectory();
        }

        target = new File(target + File.separator + CROP_CACHE_FOLDER);

        if (!target.exists()) {
            try {
                boolean result = target.mkdir();
                Log.d(TAG, "generateUri " + target + " result: " + (result ? "succeeded" : "failed"));
            } catch (Exception e) {
                Log.e(TAG, "generateUri failed: " + target, e);
            }
        }
        String name = "cache.jpg";
        target = new File(target, name);
        return target;
    }

    public static File getChaceFile(Context context) {
        File target = context.getExternalCacheDir();

        if (null == target) {
            target = Environment.getExternalStorageDirectory();
        }

        target = new File(target + File.separator + CROP_CACHE_FOLDER);

        if (!target.exists()) {
            try {
                boolean result = target.mkdir();
                Log.d(TAG, "generateUri " + target + " result: " + (result ? "succeeded" : "failed"));
            } catch (Exception e) {
                Log.e(TAG, "generateUri failed: " + target, e);
            }
        }
        String name = Utils.plusString(UUID.randomUUID().toString().toUpperCase(), ".jpg");

        target = new File(target, name);
        return target;
    }

    /**
     * 保存方法
     */
    public static String saveCacheBitmap(Context context, Bitmap bitmap, int options) {
        Log.e(TAG, "保存图片");

        File cache = getChaceFile(context);

        try {
            FileOutputStream out = new FileOutputStream(cache);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
            out.flush();
            out.close();
            Log.e(TAG, cache.getAbsolutePath());
            return cache.getAbsolutePath();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }


    public static String compressImageAndSave(String path, Bitmap image, int size) {
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 20;// 每次都减少20
        }
        return saveBitmap(path, image, options);
    }


    /**
     * 保存方法
     */
    public static String saveBitmap(String srcPath, Bitmap bitmap, int options) {
        Log.e(TAG, "保存图片");
        String newPath = getTempPicPath(srcPath);
        File f = new File(newPath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
            out.flush();
            out.close();
            Log.e(TAG, f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    public static String getTempPicPath(String path) {
        int index = path.lastIndexOf(".");
        String temp = path.substring(0, index);
        String postfix = path.substring(index);
        String newPath = temp + "_temp" + postfix;
        return newPath;
    }

    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath);
        } else {
            Bitmap bm = loadBitmap(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    /**
     * 得到 图片旋转 的角度
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e("test", "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap;
    }


    public static File createTmpFile(Context context) {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(pic, fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }
    }
}
