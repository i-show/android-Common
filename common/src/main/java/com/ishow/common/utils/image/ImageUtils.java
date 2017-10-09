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
package com.ishow.common.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.ishow.common.utils.AppUtils;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.log.L;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;


@SuppressWarnings("unused")
public final class ImageUtils {
    private static final String TAG = "ImageUtils";
    /**
     * 压缩图片后最大边长度
     */
    private static final int MAX_SIZE = 2000;
    /**
     * 默认的的压缩图片质量
     */
    private static final int DEFAULT_COMPRESS_QUALITY = 85;
    /**
     * 默认压缩后图片大小
     */
    private static final int DEFAULT_COMPRESS_PHOTO_SIZE = 500;

    /**
     * Drawable转Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap 转 drawable
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }


    /**
     * Byte transfer to bitmap
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
        if (byteArray.length != 0) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * Byte transfer to drawable
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
        ByteArrayInputStream ins = null;
        if (byteArray != null) {
            ins = new ByteArrayInputStream(byteArray);
        }
        return Drawable.createFromStream(ins, null);
    }

    /**
     * Drawable transfer to bytes
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        if (!(drawable instanceof BitmapDrawable)) {
            L.e(TAG, "drawableToBytes:  not a bitmap drawable");
            return new byte[]{};
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmapToBytes(bitmap);
    }

    /**
     * Bitmap transfer to bytes
     */
    @SuppressWarnings("WeakerAccess")
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            L.e(TAG, "bitmap is null");
            return new byte[]{};
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * 把图片添加一个颜色
     */
    public static Bitmap getTintBitmap(Bitmap bitmap, final int color) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        Bitmap background = getPureBitmap(bitmap, color);
        canvas.drawBitmap(background, 0f, 0f, paint);
        background.recycle();
        return bitmap;
    }

    /**
     * 获取一个纯色的Bitmap
     */
    @SuppressWarnings("WeakerAccess")
    public static Bitmap getPureBitmap(Bitmap bitmap, final int color) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return bitmap;
    }

    /**
     * 获取 图片旋转 的角度
     */
    @SuppressWarnings("WeakerAccess")
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (exif == null) {
            Log.i(TAG, "getExifOrientation: exif is null");
            return degree;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
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
        return degree;
    }

    /*
     * 旋转图片
     */
    @SuppressWarnings("WeakerAccess")
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    /**
     * 压缩并保存Bitmap
     */
    public static String compressBitmap(Context context, Bitmap image) {
        return compressBitmap(context, image, Bitmap.CompressFormat.WEBP, DEFAULT_COMPRESS_PHOTO_SIZE);
    }

    /**
     * 压缩并保存Bitmap
     *
     * @param image       要保存的图片
     * @param pictureSize 要保存图片的大小
     */
    public static String compressBitmap(Context context, Bitmap image, int pictureSize) {
        return compressBitmap(context, image, Bitmap.CompressFormat.WEBP, pictureSize);
    }

    /**
     * 压缩并保存Bitmap
     */
    public static String compressBitmap(Context context, Bitmap image, Bitmap.CompressFormat format) {
        return compressBitmap(context, image, format, DEFAULT_COMPRESS_PHOTO_SIZE);
    }

    /**
     * 压缩并保存Bitmap
     *
     * @param image       要保存的图片
     * @param pictureSize 要保存图片的大小
     */
    @SuppressWarnings("WeakerAccess")
    public static String compressBitmap(Context context, Bitmap image, Bitmap.CompressFormat format, int pictureSize) {
        int quality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(format, 100, baos);
        // 循环判断如果压缩后图片是否大于指定大小,大于继续压缩
        while (baos.toByteArray().length / 1024 > pictureSize) {
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(format, quality, baos);
            quality -= 10;// 每次都减少10
        }
        return saveBitmap(context, image, quality);
    }

    /**
     * 压缩图片
     */
    public static String compressImage(Context context, String photoPath) {
        return compressImage(context, photoPath, DEFAULT_COMPRESS_QUALITY);
    }

    /**
     * 压缩图片
     */
    @SuppressWarnings("WeakerAccess")
    public static String compressImage(Context context, String photoPath, int quality) {
        Bitmap bitmap = null;
        String resultPath = generateRandomPhotoName(context);
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, opts);
            opts.inSampleSize = calculateInSampleSize(opts);
            opts.inJustDecodeBounds = false;
            Log.i(TAG, "inSampleSize = " + opts.inSampleSize);
            int angle = ImageUtils.getExifOrientation(photoPath);
            bitmap = BitmapFactory.decodeFile(photoPath, opts);
            bitmap = rotateBitmap(angle, bitmap);
            saveBitmap(bitmap, resultPath, quality);
        } catch (Exception e) {
            return null;
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }

        return resultPath;
    }


    /**
     * 计算压缩比例值
     *
     * @param options 解析图片的配置信息
     */
    @SuppressWarnings("WeakerAccess")
    public static int calculateInSampleSize(BitmapFactory.Options options) {
        // 保存图片原宽高值
        final int width = options.outWidth;
        final int height = options.outHeight;
        // 初始化压缩比例为1
        int inSampleSize = 1;

        if (width > height) {

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((width / inSampleSize) >= MAX_SIZE) {
                inSampleSize *= 2;
                Log.i(TAG, "with inSampleSize = " + inSampleSize);
            }
        } else {
            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((height / inSampleSize) >= MAX_SIZE) {
                inSampleSize *= 2;
                Log.i(TAG, "height inSampleSize = " + inSampleSize);
            }

        }
        return inSampleSize;
    }


    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Context context, Bitmap bitmap) {
        String fileName = generateRandomPhotoName(context);
        return saveBitmap(bitmap, Bitmap.CompressFormat.WEBP, fileName, DEFAULT_COMPRESS_QUALITY);
    }


    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Context context, Bitmap bitmap, int quality) {
        String fileName = generateRandomPhotoName(context);
        return saveBitmap(bitmap, Bitmap.CompressFormat.WEBP, fileName, quality);
    }

    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Context context, Bitmap.CompressFormat format, Bitmap bitmap) {
        String fileName = generateRandomPhotoName(context);
        return saveBitmap(bitmap, format, fileName, DEFAULT_COMPRESS_QUALITY);
    }

    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Context context, Bitmap.CompressFormat format, Bitmap bitmap, int quality) {
        String fileName = generateRandomPhotoName(context);
        return saveBitmap(bitmap, format, fileName, quality);
    }

    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Bitmap bitmap, String fileName) {
        return saveBitmap(bitmap, Bitmap.CompressFormat.WEBP, fileName, DEFAULT_COMPRESS_QUALITY);
    }

    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Bitmap bitmap, String fileName, int quality) {
        return saveBitmap(bitmap, Bitmap.CompressFormat.WEBP, fileName, quality);
    }

    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Bitmap bitmap, Bitmap.CompressFormat format, String fileName) {
        return saveBitmap(bitmap, format, fileName, DEFAULT_COMPRESS_QUALITY);
    }

    /**
     * 把Bitmap输出到本地
     */
    @SuppressWarnings("WeakerAccess")
    public static String saveBitmap(Bitmap bitmap, Bitmap.CompressFormat format, String fileName, int quality) {
        L.e(TAG, "保存图片 fileName =  " + fileName + " format =" + format);

        File cache = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(cache);
            bitmap.compress(format, quality, out);
            out.flush();
            out.close();
            Log.e(TAG, cache.getAbsolutePath());
            return cache.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 生成图片名称
     */
    public static File generateRandomPhotoFile(Context context) {
        return new File(generateRandomPhotoName(context));
    }

    /**
     * 生成随机的名字
     */
    @SuppressWarnings("WeakerAccess")
    public static String generateRandomPhotoName(Context context) {
        File cacheFolder = context.getExternalCacheDir();
        if (null == cacheFolder) {
            File target = Environment.getExternalStorageDirectory();
            cacheFolder = new File(target + File.separator + AppUtils.getAppName(context));
        }

        Log.d(TAG, "cacheFolder path = " + cacheFolder.getAbsolutePath());
        if (!cacheFolder.exists()) {
            try {
                boolean result = cacheFolder.mkdir();
                Log.d(TAG, " result: " + (result ? "succeeded" : "failed"));
            } catch (Exception e) {
                Log.e(TAG, "generateUri failed: " + e.toString());
            }
        }
        String name = StringUtils.plusString(UUID.randomUUID().toString().toUpperCase(), ".jpg");
        return StringUtils.plusString(cacheFolder.getAbsolutePath(), File.separator, name);
    }
}