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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.activity.CropImageActivity;
import com.bright.common.constant.Shift;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;
import com.bright.common.widget.loading.LoadingDialog;

import java.io.File;
import java.util.UUID;

/**
 * 上传图片以及添加图片的封装类
 */
public class SelectPhotoUtils {
    private static final String TAG = SelectPhotoUtils.class.getSimpleName();
    private static final String CACHE_FOLDER = "nuskin";

    private static final int SELECT_PHOTO_CAMERA = 0;
    private static final int SELECT_PHOTO_GALLERY = 1;

    public static final int MODE_COMPRESS = 0;
    public static final int MODE_CROP = 1;
    private int mMode;
    private int mScaleX;
    private int mScaleY;

    private Uri mCameraFileUri = Uri.EMPTY;

    private LoadingDialog mLoadingDialog;
    private Activity mContext;

    private CallBack mCallBack;

    public SelectPhotoUtils(Activity context) {
        mContext = context;
    }

    public void select(int mode) {
        select(mode, 1, 1);
    }

    public void select(int mode, int x, int y) {
        mMode = mode;
        mScaleX = x;
        mScaleY = y;

        BaseDialog.Builder builder = new BaseDialog.Builder(mContext, R.style.Dialog_Bottom_IOS);
        builder.setTitle(R.string.select_photo_title)
                .setItems(R.array.select_photos, mDialogClickListener)
                .setNegativeButton(android.R.string.cancel, null)
                .isShowFromBottom(true)
                .create()
                .show();
    }

    private DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent;
            switch (which) {
                case SELECT_PHOTO_CAMERA:
                    // 跳转相机拍照 我暂时没有更好的方法
                    mCameraFileUri = generateUri();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);
                    mContext.startActivityForResult(intent, Request.REQUEST_CAMERA);
                    break;
                case SELECT_PHOTO_GALLERY:
                    intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    mContext.startActivityForResult(intent, Request.REQUEST_PICK);
                    break;
            }
        }
    };

    // 用来接管activity的result

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String picPath;
        if (resultCode == Activity.RESULT_CANCELED) {
            YToast.makeText(mContext, R.string.cancle_photo, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case Request.REQUEST_CAMERA:
                switch (mMode) {
                    case MODE_COMPRESS:
                        mLoadingDialog = LoadingDialog.show(mContext);
                        new Thread(new CompressRunnable(mCameraFileUri)).start();
                        break;
                    case MODE_CROP:
                        picPath = mCameraFileUri.getPath();
                        goToCrop(picPath);
                        break;
                }
                break;
            case Request.REQUEST_PICK:
                picPath = ImageUtils.getPicturePath(data, mContext);
                Log.d("bitmapImage", "picPath = " + picPath);
                switch (mMode) {
                    case MODE_COMPRESS:
                        mLoadingDialog = LoadingDialog.show(mContext);
                        final Uri originUri = Uri.parse(picPath);
                        new Thread(new CompressRunnable(originUri)).start();
                        break;
                    case MODE_CROP:
                        goToCrop(picPath);
                        break;
                }
                break;
            case Request.REQUEST_CROP:
                picPath = data.getStringExtra(CropImageActivity.KEY_RESULT_PATH);
                final Uri reuslt = Uri.parse("file://" + picPath);
                if (mCallBack != null) {
                    mCallBack.onResult(reuslt);
                }
                break;
        }
    }


    private void goToCrop(String path) {
        Intent intent = new Intent(mContext, CropImageActivity.class);
        intent.putExtra(CropImageActivity.KEY_PATH, path);
        intent.putExtra(CropImageActivity.KEY_RATIO_X, mScaleX);
        intent.putExtra(CropImageActivity.KEY_RATIO_Y, mScaleY);
        mContext.startActivityForResult(intent, Request.REQUEST_CROP);
    }

    private Uri generateUri() {
        File cacheFolder = mContext.getExternalCacheDir();
        if (null == cacheFolder) {
            File target = Environment.getExternalStorageDirectory();
            cacheFolder = new File(target + File.separator + CACHE_FOLDER);
        }

        Log.d(TAG, "cacheFolder path = " + cacheFolder.getAbsolutePath());
        if (!cacheFolder.exists()) {
            try {
                boolean result = cacheFolder.mkdir();
                Log.d(TAG, "generateUri " + cacheFolder + " result: " + (result ? "succeeded" : "failed"));
            } catch (Exception e) {
                Log.e(TAG, "generateUri failed: " + cacheFolder, e);
            }
        }
        String name = Utils.plusString(UUID.randomUUID().toString().toUpperCase(), ".jpg");
        return Uri
                .fromFile(cacheFolder)
                .buildUpon()
                .appendPath(name)
                .build();
    }

    private class CompressRunnable implements Runnable {
        Uri original;

        public CompressRunnable(Uri original) {
            this.original = original;
        }

        @Override
        public void run() {
            Uri compressUri = generateUri();
            CompressImageUtils.compreeImage(original, compressUri);
            Message message = new Message();
            message.obj = compressUri;
            mHandler.sendMessage(message);
        }
    }

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


    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onResult(Uri url);
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
