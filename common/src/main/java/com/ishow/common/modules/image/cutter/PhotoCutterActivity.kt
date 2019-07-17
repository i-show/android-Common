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

package com.ishow.common.modules.image.cutter

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.ishow.common.R
import com.ishow.common.app.activity.BaseActivity
import com.ishow.common.extensions.compress
import com.ishow.common.extensions.loadUrl
import com.ishow.common.extensions.save
import com.ishow.common.utils.image.select.SelectPhotoUtils
import com.ishow.common.widget.loading.LoadingDialog
import kotlinx.android.synthetic.main.activity_crop_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 图片剪切界面
 * 需要使用的Theme  android:theme="@Style/Theme.NoActionBar.Fullscreen"
 */
class PhotoCutterActivity : BaseActivity() {

    private lateinit var mCompressFormat: Bitmap.CompressFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)
        val intent = intent
        val path = intent.getStringExtra(KEY_PATH)
        val x = intent.getIntExtra(KEY_RATIO_X, 1)
        val y = intent.getIntExtra(KEY_RATIO_Y, 1)
        cropView.setCustomRatio(x, y)
        cropView.loadUrl(path)
    }


    override fun initNecessaryData() {
        super.initNecessaryData()
        val format = intent.getSerializableExtra(KEY_FORMAT)
        mCompressFormat = if (format == null) {
            Bitmap.CompressFormat.JPEG
        } else {
            format as Bitmap.CompressFormat
        }
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        val dialog = LoadingDialog.show(this, null)
        val cachePath = cropView.croppedBitmap.compress(mCompressFormat).save(context, mCompressFormat)
        val intent = Intent()
        intent.putExtra(KEY_RESULT_PATH, cachePath)
        setResult(SelectPhotoUtils.Request.REQUEST_CROP_IMAGE, intent)
        LoadingDialog.dismiss(dialog)
        finish()
    }

    companion object {
        /**
         * 图片路径
         */
        const val KEY_PATH = "crop_image_path"
        /**
         * 生成图片的路径
         */
        const val KEY_RESULT_PATH = "result_cutting_image_path"
        /**
         * x 轴比例
         */
        const val KEY_RATIO_X = "result_cutting_image_ratio_x"
        /**
         * y 轴比例
         */
        const val KEY_RATIO_Y = "result_cutting_image_ratio_y"
        /**
         * format
         */
        const val KEY_FORMAT = "result_cutting_image_format"
    }
}
