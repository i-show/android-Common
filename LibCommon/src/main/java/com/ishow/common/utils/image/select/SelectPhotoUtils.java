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

package com.ishow.common.utils.image.select;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.ishow.common.R;
import com.ishow.common.constant.Shift;
import com.ishow.common.entries.Photo;
import com.ishow.common.modules.image.cutter.PhotoCutterActivity;
import com.ishow.common.modules.image.select.PhotoSelectorActivity;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.image.ImageUtils;
import com.ishow.common.utils.log.L;
import com.ishow.common.widget.YToast;
import com.ishow.common.widget.dialog.BaseDialog;
import com.ishow.common.widget.loading.LoadingDialog;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基类
 */
public class SelectPhotoUtils implements
        DialogInterface.OnClickListener,
        DialogInterface.OnDismissListener {

    private static final String TAG = "SelectPhotoUtils";

    /**
     * 多选模式下最多可以选多少张图片
     */
    private static final int MAX_SELECT_COUNT = 9;
    /**
     * 最多几个线程进行同时压缩
     */
    private static final int MAX_THREAD = 3;
    /**
     * 通过拍照 来选择
     */
    private static final int SELECT_PHOTO_CAMERA = 0;
    /**
     * 通过画廊来选择
     */
    private static final int SELECT_PHOTO_GALLERY = 1;


    /**
     * 保存图片的地址的
     */
    private List<String> mPhotos = Collections.synchronizedList(new ArrayList<String>());

    private Activity mActivity;

    /**
     * Camera拍照输出的地址
     */
    private Uri mCameraFileUri;
    /**
     * 选择方式的Dialog
     */
    private BaseDialog mSelectDialog;

    private LoadingDialog mLoadingDialog;

    /**
     * 选择图片的Listener
     */
    private OnSelectPhotoListener mSelectPhotoListener;

    /**
     * 选择图片的类型， 单选还是多选
     */
    private int mSelectMode;
    /**
     * 图片选择后是 压缩还是剪切
     */
    private int mResultMode;
    /**
     * 剪切模式-X轴比例
     */
    private int mScaleX;
    /**
     * 剪切模式-Y轴比例
     */
    private int mScaleY;
    /**
     * 可以选择的最大数量
     */
    private int mMaxSelectCount;

    /**
     * 数据是否已经完成
     */
    private boolean isAlreadyOk;
    /**
     * 压缩图片的线程池
     */
    private ExecutorService mExecutorService;

    public SelectPhotoUtils(Activity context, @SelectMode int selectMode) {
        mActivity = context;
        mSelectMode = selectMode;
    }

    /**
     * 设置选择模式
     */
    public void setSelectMode(@SelectMode int selectMode) {
        mSelectMode = selectMode;
    }

    /**
     * 默认选择图片
     */
    public void select() {
        if (!hasPermission()) {
            Log.i(TAG, "select: no premission");
            return;
        }

        if (mSelectMode == SelectMode.SINGLE) {
            mResultMode = ResultMode.COMPRESS;
        } else {
            mResultMode = ResultMode.COMPRESS;
            mMaxSelectCount = MAX_SELECT_COUNT;
        }

        showSelectDialog();
    }

    /**
     * 选择图片
     */
    public void select(@IntRange(from = 1) int maxCount) {
        if (!hasPermission()) {
            Log.i(TAG, "select: no premission");
            return;
        }

        if (mSelectMode == SelectMode.SINGLE) {
            throw new IllegalStateException("only mult select SelectMode can select mulit count");
        }
        mMaxSelectCount = maxCount;
        mResultMode = ResultMode.COMPRESS;
        mMaxSelectCount = MAX_SELECT_COUNT;

        showSelectDialog();
    }


    /**
     * 选择图片模式为剪切模式
     *
     * @param scaleX 单选图片-X轴的比例
     * @param scaleY 单选图片-Y轴的比例
     */
    public void select(@IntRange(from = 1) int scaleX, @IntRange(from = 1) int scaleY) {
        if (!hasPermission()) {
            Log.i(TAG, "select: no premission");
            return;
        }

        if (mSelectMode == SelectMode.MULTIPLE) {
            throw new IllegalStateException("only single select SelectMode can set scaleX and scaleY");
        }
        mResultMode = ResultMode.CROP;
        mScaleX = scaleX;
        mScaleY = scaleY;

        showSelectDialog();
    }

    /**
     * 显示选择框
     */
    private void showSelectDialog() {
        if (mSelectDialog == null) {
            mSelectDialog = new BaseDialog.Builder(mActivity, R.style.AppDialog_Bottom_IOS_NoTitle)
                    .setTitle(R.string.select_photo_title)
                    .setNegativeButton(R.string.cancel, null)
                    .isShowFromBottom(true)
                    .setItems(R.array.select_photos, this)
                    .setOnDismissListener(this)
                    .create();
        }

        if (!mSelectDialog.isShowing()) {
            mSelectDialog.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case SELECT_PHOTO_CAMERA:
                selectPhotoByCamera();
                break;
            case SELECT_PHOTO_GALLERY:
                selectPhotoByGallery();
                break;
        }
    }

    /**
     * 通过相机来选择图片
     */
    private void selectPhotoByCamera() {
        String authority = StringUtils.plusString(mActivity.getPackageName(), ".fileprovider");
        File file = ImageUtils.generateRandomPhotoFile(mActivity);
        mCameraFileUri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(mActivity, authority, file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        switch (mSelectMode) {
            case SelectMode.SINGLE:
                mActivity.startActivityForResult(intent, Request.REQUEST_SINGLE_CAMERA);
                break;
            case SelectMode.MULTIPLE:
                mActivity.startActivityForResult(intent, Request.REQUEST_MULTI_CAMERA);
                break;
        }
    }

    /**
     * 通过相册来选择图片
     */
    private void selectPhotoByGallery() {
        Intent intent = new Intent(mActivity, PhotoSelectorActivity.class);
        switch (mSelectMode) {
            case SelectMode.SINGLE:
                intent.putExtra(Photo.Key.EXTRA_SELECT_MODE, Photo.Key.MODE_SINGLE);
                mActivity.startActivityForResult(intent, Request.REQUEST_SINGLE_PICK);
                break;
            case SelectMode.MULTIPLE:
                intent.putExtra(Photo.Key.EXTRA_SELECT_MODE, Photo.Key.MODE_MULTI);
                intent.putExtra(Photo.Key.EXTRA_SELECT_COUNT, mMaxSelectCount);
                mActivity.startActivityForResult(intent, Request.REQUEST_MULTI_PICK);
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mSelectDialog = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 用来接管activity的result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            resolveResultCanceled(requestCode);
            return;
        }


        switch (requestCode) {
            // 不管多选还是单选 相机处理是一样的
            case Request.REQUEST_SINGLE_CAMERA:
            case Request.REQUEST_MULTI_CAMERA:
                resolveSingleResult(mCameraFileUri.getPath());
                break;
            case Request.REQUEST_SINGLE_PICK:
                String path = data.getStringExtra(Photo.Key.EXTRA_RESULT);
                resolveSingleResult(path);
                break;
            case Request.REQUEST_MULTI_PICK:
                List<String> pathList = data.getStringArrayListExtra(Photo.Key.EXTRA_RESULT);
                resolveMultiResult(pathList);
                break;
            case Request.REQUEST_CROP_IMAGE:
                String picPath = data.getStringExtra(PhotoCutterActivity.KEY_RESULT_PATH);
                notifySelectPhoto(picPath);
                break;
            default:
                L.i(TAG, "requestCode = " + requestCode);
                break;
        }
    }


    /**
     * onActivityResult 裁掉caceled的事件
     */
    private void resolveResultCanceled(int requestCode) {
        switch (requestCode) {
            case Request.REQUEST_SINGLE_PICK:
            case Request.REQUEST_SINGLE_CAMERA:
            case Request.REQUEST_MULTI_PICK:
            case Request.REQUEST_MULTI_CAMERA:
            case Request.REQUEST_CROP_IMAGE:
                YToast.show(mActivity, R.string.cancle_photo);
                break;
        }
    }

    /**
     * onActivityResult 处理 单选
     */
    private void resolveSingleResult(String picPath) {
        if (TextUtils.isEmpty(picPath)) {
            Log.i(TAG, "resolveSingleResult: picPath is empty");
            return;
        }

        mLoadingDialog = LoadingDialog.show(mActivity, mLoadingDialog);

        List<String> photos = new ArrayList<>();
        photos.add(picPath);

        mPhotos.clear();
        mPhotos.add("single");

        switch (mResultMode) {
            case ResultMode.COMPRESS:
                mLoadingDialog = LoadingDialog.show(mActivity, mLoadingDialog);
                resolveResultPhotosForCompress(photos);
                break;
            case ResultMode.CROP:
                goToCrop(picPath);
                break;
        }
    }

    /**
     * onActivityResult 处理 单选
     */
    private void resolveMultiResult(List<String> pathList) {
        if (pathList == null || pathList.isEmpty()) {
            Log.i(TAG, "resolveMultiResult: pathList is empty");
            return;
        }
        // 把没有压缩前添加进入当占位符
        mPhotos.clear();
        mPhotos.addAll(pathList);

        mLoadingDialog = LoadingDialog.show(mActivity, mLoadingDialog);
        resolveResultPhotosForCompress(pathList);
    }

    /**
     * 处理返回的图片-通过压缩图片的方式
     */
    private void resolveResultPhotosForCompress(List<String> photots) {
        isAlreadyOk = false;
        // 设置最多线程同时上传图片
        mExecutorService = Executors.newFixedThreadPool(MAX_THREAD);
        for (int i = 0; i < photots.size(); i++) {
            String photo = photots.get(i);
            mExecutorService.execute(new CompressRunnable(photo, i));
        }
        // 关闭线程池
        mExecutorService.shutdown();
    }

    /**
     * 跳转剪切
     */
    private void goToCrop(String path) {
        Intent intent = new Intent(mActivity, PhotoCutterActivity.class);
        intent.putExtra(PhotoCutterActivity.KEY_PATH, path);
        intent.putExtra(PhotoCutterActivity.KEY_RATIO_X, mScaleX);
        intent.putExtra(PhotoCutterActivity.KEY_RATIO_Y, mScaleY);
        mActivity.startActivityForResult(intent, Request.REQUEST_CROP_IMAGE);
    }

    private class CompressRunnable implements Runnable {
        String path;
        int key;

        CompressRunnable(String path, int key) {
            this.path = path;
            this.key = key;
        }

        @Override
        public void run() {
            String resultPath = ImageUtils.compressImage(mActivity, path);
            mPhotos.set(key, resultPath);
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mSelectPhotoListener == null) {
                LoadingDialog.dismiss(mLoadingDialog);
                return;
            }

            if (mExecutorService.isTerminated() && !isAlreadyOk) {
                isAlreadyOk = true;
                notifySelectPhoto(mPhotos, mPhotos.get(0));
            }

        }
    };


    /**
     * 提示已经选了多少图片
     */
    private void notifySelectPhoto(String singlePath) {
        List<String> multiPath = new ArrayList<>();
        multiPath.add(singlePath);
        notifySelectPhoto(multiPath, singlePath);
    }

    /**
     * 提示已经选了多少图片
     */
    private void notifySelectPhoto(List<String> multiPath, String singlePath) {
        LoadingDialog.dismiss(mLoadingDialog);
        if (mSelectPhotoListener != null) {
            mSelectPhotoListener.onSelectedPhoto(multiPath, singlePath);
        }
    }

    /**
     * 设置选择图片的监听
     */
    public void setOnSelectPhotoListener(OnSelectPhotoListener listener) {
        mSelectPhotoListener = listener;
    }

    /**
     * 如果是Android6.0 以上就需要配置权限
     */

    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissionRationale(R.string.premission_storage_select_photo);
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }
            return false;
        } else if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissionRationale(R.string.premission_storage_select_photo);
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Request.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
            }
            return false;
        }
        return true;
    }


    private void requestPermissionRationale(@StringRes int message) {
        BaseDialog dialog = new BaseDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri url = Uri.parse("package:" + mActivity.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, url);
                        mActivity.startActivity(intent);
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 定义图片是单选还是多选
     */
    @IntDef({SelectMode.SINGLE, SelectMode.MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectMode {
        /**
         * 单选
         */
        int SINGLE = 1;
        /**
         * 多选
         */
        int MULTIPLE = 2;
    }


    private final static class ResultMode {
        /**
         * 压缩
         */
        private final static int COMPRESS = 1;
        /**
         * 剪切
         */
        private final static int CROP = 2;
    }


    /**
     * 公共类方便调用 request
     */
    public class Request {
        /**
         * 单选调用摄像头
         */
        static final int REQUEST_SINGLE_CAMERA = 1 << Shift.UTILS;
        /**
         * 多选调用摄像头
         */
        static final int REQUEST_MULTI_CAMERA = REQUEST_SINGLE_CAMERA + 1;
        /**
         * 单选图片
         */
        static final int REQUEST_SINGLE_PICK = REQUEST_SINGLE_CAMERA + 2;
        /**
         * 多选图片
         */
        static final int REQUEST_MULTI_PICK = REQUEST_SINGLE_CAMERA + 3;
        /**
         * 剪切图片
         */
        public static final int REQUEST_CROP_IMAGE = REQUEST_SINGLE_CAMERA + 4;
        /**
         * 请求处理 权限
         */
        public static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = REQUEST_SINGLE_CAMERA + 5;
        /**
         * 请求处理 权限
         */
        public static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = REQUEST_SINGLE_CAMERA + 6;
    }
}
