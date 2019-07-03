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

package com.ishow.common.modules.image.cutter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.ishow.common.R;
import com.ishow.common.app.activity.BaseActivity;
import com.ishow.common.utils.image.ImageUtils;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.common.utils.image.select.SelectPhotoUtils;
import com.ishow.common.widget.TopBar;
import com.ishow.common.widget.imageview.CropImageView;
import com.ishow.common.widget.loading.LoadingDialog;

/**
 * 图片剪切界面
 * 需要使用的Theme  android:theme="@Style/Theme.NoActionBar.Fullscreen"
 */
public class PhotoCutterActivity extends BaseActivity {
    private static final String TAG = "PhotoCutterActivity";
    /**
     * 图片路径
     */
    public static final String KEY_PATH = "crop_image_path";
    /**
     * 生成图片的路径
     */
    public static final String KEY_RESULT_PATH = "result_croped_image_path";
    /**
     * x 轴比例
     */
    public static final String KEY_RATIO_X = "result_croped_image_ratio_x";
    /**
     * y 轴比例
     */
    public static final String KEY_RATIO_Y = "result_croped_image_ratio_y";
    /**
     * format
     */
    public static final String KEY_FOTMAT = "result_croped_image_formater";

    private CropImageView mCropView;

    private Bitmap.CompressFormat mCompressFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        Intent intent = getIntent();
        String path = intent.getStringExtra(KEY_PATH);

        ImageLoader.with(this)
                .load(path)
                .into(mCropView);

        Object format = intent.getSerializableExtra(KEY_FOTMAT);
        if (format == null || !(format instanceof Bitmap.CompressFormat)) {
            mCompressFormat = Bitmap.CompressFormat.WEBP;
        } else {
            mCompressFormat = (Bitmap.CompressFormat) format;
        }

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
        LoadingDialog dialog = LoadingDialog.Companion.show(this, null);
        Bitmap bitmap = mCropView.getCroppedBitmap();
        String cachePath = ImageUtils.compressBitmap(this, bitmap, mCompressFormat);
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_PATH, cachePath);
        setResult(SelectPhotoUtils.Request.REQUEST_CROP_IMAGE, intent);
        dialog.dismiss();
        finish();
    }
}
