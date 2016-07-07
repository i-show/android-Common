/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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
package com.bright.common.utils;

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
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public final class ImageUtils {
    private static final String TAG = "ImageUtils";

    /**
     * Transfer drawable to bitmap
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
     * Bitmap to drawable
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
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
     *
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
     * Get rounded corner images
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

    public static String getPicturePath(Intent data, Context context) {

        String path = "";
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = context.getContentResolver();
        // 此处的用于判断接收的Activity是不是你想要的那个
        Uri originalUri = null;
        try {
            originalUri = data.getData(); // 获得图片的uri
            // 这里开始的第二部分，获取图片的路径：
            String[] proj = {MediaStore.Images.Media.DATA};
            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = resolver.query(originalUri, proj, null, null, null);
            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径
            path = cursor.getString(column_index);
            if (TextUtils.isEmpty(path)) {
                return originalUri.getPath();
            }
            return path;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            if (TextUtils.isEmpty(path)) {
                return originalUri.getPath();
            }
            return path;
        }

    }
}