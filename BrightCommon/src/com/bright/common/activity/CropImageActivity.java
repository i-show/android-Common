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

package com.bright.common.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;

import com.bright.common.BaseActivity;
import com.bright.common.R;
import com.bright.common.utils.FileUtils;
import com.bright.common.utils.SelectPhotoUtils;
import com.bright.common.widget.CropImageView;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.loading.LoadingDialog;
import com.bumptech.glide.Glide;

import java.io.IOException;

public class CropImageActivity extends BaseActivity {

    public static final String KEY_PATH = "crop_image_path";
    public static final String KEY_RESULT_PATH = "result_croped_image_path";
    public static final String KEY_RATIO_X = "result_croped_image_ratio_x";
    public static final String KEY_RATIO_Y = "result_croped_image_ratio_y";
    private CropImageView mCropView;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        Intent intent = getIntent();
        mPath = intent.getStringExtra(KEY_PATH);
        Glide.with(this)
                .load(mPath)
                .asBitmap()
                .into(mCropView);

        int x = intent.getIntExtra(KEY_RATIO_X, 1);
        int y = intent.getIntExtra(KEY_RATIO_Y, 1);
        mCropView.setCustomRatio(x, y);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topbar = (TopBar) findViewById(R.id.top_bar);
        topbar.setOnTopBarListener(this);

        mCropView = (CropImageView) findViewById(R.id.crop);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        LoadingDialog dialog = LoadingDialog.show(this);
        Bitmap bitmap = mCropView.getCroppedBitmap();
        String cachePath = FileUtils.compressImageAndSaveCache(this, bitmap, 300);
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_PATH, cachePath);
        setResult(SelectPhotoUtils.Request.REQUEST_CROP, intent);
        dialog.dismiss();
        finish();
    }

    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }


    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
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
}
