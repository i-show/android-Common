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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.bright.common.app.activity.CropImageActivity;
import com.bright.common.app.activity.SelectorPicturesActivity;
import com.bright.common.entries.SelectorPicture;
import com.bright.common.utils.image.ImageUtils;
import com.bright.common.widget.loading.LoadingDialog;

/**
 * 上传图片以及添加图片的封装类
 * android:configChanges="orientation|keyboardHidden|screenSize"
 * 照片处理的Activity 需要添加
 */
public class SingleSelectPhotoUtils extends SelectPhotoUtils implements DialogInterface.OnClickListener {
    private static final String TAG = "SingleSelectPhotoUtils";
    /**
     * 图片是压缩模式
     */
    public static final int MODE_COMPRESS = 0;
    /**
     * 图片是剪切模式
     */
    public static final int MODE_CROP = 1;

    private int mMode;
    private int mScaleX;
    private int mScaleY;

    private CallBack mCallBack;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Uri uri = (Uri) msg.obj;
            LoadingDialog.dismiss(mLoadingDialog);
            if (mCallBack != null) {
                mCallBack.onResult(uri);
            }
        }
    };

    public SingleSelectPhotoUtils(Activity context) {
        super(context, SelectMode.SINGLE);
    }

    /**
     * 选择图片模式 压缩或者剪切
     *
     * @param mode MODE_COMPRESS or MODE_CROP
     */
    public void select(int mode) {
        if (mode == MODE_CROP) {
            select(1, 1);
        } else {
            mMode = MODE_COMPRESS;
        }
    }

    /**
     * 选择图片模式为剪切模式
     */
    public void select(int x, int y) {
        mMode = MODE_CROP;
        mScaleX = x;
        mScaleY = y;

        showSelectDialog();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent;
        switch (which) {
            case SELECT_PHOTO_CAMERA:
                mCameraFileUri = Uri.fromFile(ImageUtils.generateRandomPhotoFile(mContext));
                Log.i(TAG, "onClick: mCameraFileUri = " + mCameraFileUri);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);
                mContext.startActivityForResult(intent, Request.REQUEST_SINGLE_CAMERA);
                break;
            case SELECT_PHOTO_GALLERY:
                intent = new Intent(mContext, SelectorPicturesActivity.class);
                intent.putExtra(SelectorPicture.Key.EXTRA_SELECT_MODE, SelectorPicture.Key.MODE_SINGLE);
                mContext.startActivityForResult(intent, Request.REQUEST_SINGLE_PICK);
                break;
        }
    }

    /**
     * 用来接管activity的result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        String picPath;
        switch (requestCode) {
            case Request.REQUEST_SINGLE_CAMERA:
                switch (mMode) {
                    case MODE_COMPRESS:
                        mLoadingDialog = LoadingDialog.show(mContext, mLoadingDialog);
                        new Thread(new CompressRunnable(mCameraFileUri)).start();
                        break;
                    case MODE_CROP:
                        picPath = mCameraFileUri.getPath();
                        goToCrop(picPath);
                        break;
                }
                break;
            case Request.REQUEST_SINGLE_PICK:
                picPath = data.getStringExtra(SelectorPicture.Key.EXTRA_RESULT);
                Log.d(TAG, "picPath = " + picPath);
                switch (mMode) {
                    case MODE_COMPRESS:
                        mLoadingDialog = LoadingDialog.show(mContext, mLoadingDialog);
                        final Uri originUri = Uri.parse(picPath);
                        new Thread(new CompressRunnable(originUri)).start();
                        break;
                    case MODE_CROP:
                        goToCrop(picPath);
                        break;
                }
                break;
            case Request.REQUEST_CROP_IMAGE:
                picPath = data.getStringExtra(CropImageActivity.KEY_RESULT_PATH);
                final Uri reuslt = Uri.parse("file://" + picPath);
                if (mCallBack != null) {
                    mCallBack.onResult(reuslt);
                }
                break;
        }
    }

    /**
     * 跳转剪切
     */
    private void goToCrop(String path) {
        Intent intent = new Intent(mContext, CropImageActivity.class);
        intent.putExtra(CropImageActivity.KEY_PATH, path);
        intent.putExtra(CropImageActivity.KEY_RATIO_X, mScaleX);
        intent.putExtra(CropImageActivity.KEY_RATIO_Y, mScaleY);
        mContext.startActivityForResult(intent, Request.REQUEST_CROP_IMAGE);
    }


    /**
     * 设置回调
     */
    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 定义回调结果
     */
    public interface CallBack {
        void onResult(Uri url);
    }

    private class CompressRunnable implements Runnable {
        Uri original;

        CompressRunnable(Uri original) {
            this.original = original;
        }

        @Override
        public void run() {
            String result = ImageUtils.compressImage(mContext, original.getPath());
            Message message = new Message();
            message.obj = Uri.parse(result);
            mHandler.sendMessage(message);
        }
    }


}
