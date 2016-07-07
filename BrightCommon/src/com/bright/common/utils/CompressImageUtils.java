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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import net.bither.util.NativeUtil;

/**
 * 图片压缩工具
 */
public class CompressImageUtils {

    public static final String TAG = CompressImageUtils.class.getSimpleName();
    public static final int MIN_SIZE = 720;
    public static final int MAX_SIZE = 2000;

    public static void compreeImage(String originPath, String compressPath) {
        Bitmap bitmapImage = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(originPath, opts);
            opts.inSampleSize = calculateInSampleSize(opts);
            opts.inJustDecodeBounds = false;
            Log.i(TAG, "inSampleSize = " + opts.inSampleSize);
            int angle = FileUtils.getExifOrientation(originPath);
            bitmapImage = BitmapFactory.decodeFile(originPath, opts);
            try {
                bitmapImage = FileUtils.rotaingImageView(angle, bitmapImage);
                NativeUtil.compressBitmap(bitmapImage, 60, compressPath, true);
            } catch (Exception e) {
            }
        } finally {
            if (bitmapImage != null) {
                bitmapImage.recycle();
            }
        }
    }


    public static void compreeImage(Uri originUri, Uri compressUri) {
        Bitmap bitmapImage = null;
        try {
            String path = originUri.getPath();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            opts.inSampleSize = calculateInSampleSize(opts);
            opts.inJustDecodeBounds = false;
            Log.i(TAG, "inSampleSize url = " + opts.inSampleSize);
            int angle = FileUtils.getExifOrientation(path);
            bitmapImage = BitmapFactory.decodeFile(path, opts);
            try {
                bitmapImage = FileUtils.rotaingImageView(angle, bitmapImage);
                NativeUtil.compressBitmap(bitmapImage, 60, compressUri.getPath(), true);
            } catch (Exception e) {
                Log.i(TAG, "e = " + e);
            }
        } finally {
            if (bitmapImage != null) {
                bitmapImage.recycle();
            }
        }
    }

    /**
     * 计算压缩比例值
     *
     * @param options 解析图片的配置信息
     * @return
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
                Log.i(TAG, "width inSampleSize = " + inSampleSize);
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
}
