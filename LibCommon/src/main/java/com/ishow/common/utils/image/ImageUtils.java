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

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.ishow.common.utils.PackagesUtils;
import com.ishow.common.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public final class ImageUtils {
    private static final String TAG = "ImageUtils";
    /**
     * 压缩图片后最大边长度
     */
    private static final int MAX_SIZE = 2000;
    /**
     * 默认的的压缩图片质量
     */
    private static final int DEFAULT_COMPRESS_QUALITY = 75;

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
     * Input stream to bitmap
     */
    public static Bitmap inputStreamToBitmap(InputStream inputStream) throws Exception {
        return BitmapFactory.decodeStream(inputStream);
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
     * Bitmap transfer to bytes
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }

    /**
     * Drawable transfer to bytes
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        byte[] bytes = bitmapToBytes(bitmap);
        ;
        return bytes;
    }

    /**
     * 倒影效果
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
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
     * Get rounded corner photoList
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static Bitmap decodeUriAsBitmap(Context context, String uriStr) {
        if (TextUtils.isEmpty(uriStr)) {
            return null;
        }
        Uri uri = Uri.parse(uriStr);
        return decodeUriAsBitmap(context, uri);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 从 intent中获取图片路径
     */
    public static String getPicturePathFromIntent(Intent intent, Context context) {

        if (intent == null) {
            Log.i(TAG, "getPicturePathFromIntent:  intent is null");
            return StringUtils.EMPTY;
        }

        Uri url = intent.getData();// 获得图片的uri
        if (url == null) {
            Log.i(TAG, "getPicturePathFromIntent: url is null");
            return StringUtils.EMPTY;
        }

        ContentResolver resolver = context.getContentResolver();
        try {
            // 这里开始的第二部分，获取图片的路径：
            String[] proj = {MediaStore.Images.Media.DATA};
            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = resolver.query(url, proj, null, null, null);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 最后根据索引值获取图片路径
            String path = cursor.getString(column_index);
            if (TextUtils.isEmpty(path)) {
                return url.getPath();
            }
            cursor.close();
            return path;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return url.getPath();
        }
    }

    /**
     * 获取 图片旋转 的角度
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
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
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap;
    }


    /**
     * 压缩并保存Bitmap
     *
     * @param image       要保存的图片
     * @param pictureSize 要保存图片的大小
     */
    public static String compressBitmap(Context context, Bitmap image, int pictureSize) {
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 循环判断如果压缩后图片是否大于指定大小,大于继续压缩
        while (baos.toByteArray().length / 1024 > pictureSize) {
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;// 每次都减少10
        }
        return saveBitmap(context, image, options);
    }

    /**
     * 压缩图片
     *
     * @return 压缩后的图片路径
     */
    public static String compressImage(Context context, String photoPath) {
        return compressImage(context, photoPath, DEFAULT_COMPRESS_QUALITY);
    }

    /**
     * 压缩图片
     *
     * @return 压缩后的图片路径
     */
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
     *
     * @param options 压缩的比例
     * @return
     */
    public static String saveBitmap(Context context, Bitmap bitmap, int options) {
        Log.e(TAG, "保存图片");

        File cache = generateRandomPhotoFile(context);
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
            }
        }
    }

    /**
     * 把Bitmap输出到本地
     */
    public static String saveBitmap(Bitmap bitmap, String fileName, int options) {
        Log.e(TAG, "保存图片 fileName =  " + fileName);

        File cache = new File(fileName);
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
    public static String generateRandomPhotoName(Context context) {
        File cacheFolder = context.getExternalCacheDir();
        if (null == cacheFolder) {
            File target = Environment.getExternalStorageDirectory();
            cacheFolder = new File(target + File.separator + PackagesUtils.getAppName(context));
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