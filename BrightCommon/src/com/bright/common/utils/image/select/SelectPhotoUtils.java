/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.bright.common.utils.image.select;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.constant.Shift;
import com.bright.common.utils.PackagesUtils;
import com.bright.common.utils.StringUtils;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;
import com.bright.common.widget.loading.LoadingDialog;

import java.io.File;
import java.util.UUID;

/**
 * 基类
 */
public abstract class SelectPhotoUtils implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private static final String TAG = "SelectPhotoUtils";
    /**
     * 图片是压缩模式
     */
    public static final int MODE_COMPRESS = 0;
    /**
     * 图片是剪切模式
     */
    public static final int MODE_CROP = 1;

    /**
     * 通过拍照 来选择
     */
    public static final int SELECT_PHOTO_CAMERA = 0;
    /**
     * 通过画廊来选择
     */
    public static final int SELECT_PHOTO_GALLERY = 1;


    protected Activity mContext;

    protected Uri mCameraFileUri = Uri.EMPTY;
    /**
     * 选择方式的Dialog
     */
    private BaseDialog mSelectDialog;
    
    protected LoadingDialog mLoadingDialog;

    public SelectPhotoUtils(Activity context) {
        mContext = context;
    }

    /**
     * 显示选择框
     */
    protected void showSelectDialog() {
        if (mSelectDialog == null) {
            mSelectDialog = new BaseDialog.Builder(mContext, R.style.Dialog_Bottom_IOS)
                    .setTitle(R.string.select_photo_title)
                    .setNegativeButton(R.string.cancel, null)
                    .setItems(R.array.select_photos, this)
                    .isShowFromBottom(true)
                    .setOnDismissListener(this)
                    .create();
        }

        if (!mSelectDialog.isShowing()) {
            mSelectDialog.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mSelectDialog = null;
    }

    /**
     * 用来接管activity的result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED) {
            YToast.makeText(mContext, R.string.cancle_photo, Toast.LENGTH_SHORT).show();
            return;
        }
    }


    /**
     * 公共类方便调用 request
     */
    public class Request {
        public static final int REQUEST_CAMERA = 1 << Shift.UTILS;
        public static final int REQUEST_PICK = REQUEST_CAMERA + 1;
        public static final int REQUEST_CROP = REQUEST_CAMERA + 2;
    }
}
