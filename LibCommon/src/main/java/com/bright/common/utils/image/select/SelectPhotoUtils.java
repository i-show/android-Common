/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.bright.common.utils.image.select;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;

import com.bright.common.R;
import com.bright.common.app.activity.SelectorPicturesActivity;
import com.bright.common.constant.Shift;
import com.bright.common.entries.SelectorPicture;
import com.bright.common.utils.image.ImageUtils;
import com.bright.common.utils.log.L;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;
import com.bright.common.widget.loading.LoadingDialog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 基类
 */
public class SelectPhotoUtils implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private static final String TAG = "SelectPhotoUtils";

    /**
     * 多选模式下最多可以选多少张图片
     */
    private static final int MAX_SELECT_COUNT = 9;
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

    /**
     * 选择图片的Listener
     */
    private OnSelectPhotoListener mListener;

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
     * 选择图片的数量
     */
    private int mSelectCount;

    public SelectPhotoUtils(Activity context, @selectMode int selectMode) {
        mContext = context;
        mSelectMode = selectMode;
    }


    /**
     * 默认选择图片
     */
    public void select() {
        if (mSelectMode == SelectMode.SINGLE) {
            mResultMode = ResultMode.COMPRESS;
        } else {
            mResultMode = ResultMode.COMPRESS;
            mMaxSelectCount = MAX_SELECT_COUNT;
        }
    }

    /**
     * 选择图片
     */
    public void select(@IntRange(from = 1) int count) {
        if (mSelectMode == SelectMode.SINGLE) {
            throw new IllegalStateException("only mult select mode can select mulit count");
        }
        mSelectCount = count;
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
        if (mSelectMode == SelectMode.MULTIPLE) {
            throw new IllegalStateException("only single select mode can set scaleX and scaleY");
        }
        mResultMode = ResultMode.CROP;
        mScaleX = scaleX;
        mScaleY = scaleY;

        showSelectDialog();
    }

    /**
     * 显示选择框
     */
    protected void showSelectDialog() {
        if (mSelectDialog == null) {
            mSelectDialog = new BaseDialog.Builder(mContext, R.style.Dialog_Bottom_IOS)
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
        mCameraFileUri = Uri.fromFile(ImageUtils.generateRandomPhotoFile(mContext));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);
        switch (mSelectMode) {
            case SelectMode.SINGLE:
                mContext.startActivityForResult(intent, Request.REQUEST_SINGLE_CAMERA);
                break;
            case SelectMode.MULTIPLE:
                mContext.startActivityForResult(intent, Request.REQUEST_MULTI_CAMERA);
                break;
        }
    }

    /**
     * 通过相册来选择图片
     */
    private void selectPhotoByGallery() {
        Intent intent = new Intent(mContext, SelectorPicturesActivity.class);
        switch (mSelectMode) {
            case SelectMode.SINGLE:
                intent.putExtra(SelectorPicture.Key.EXTRA_SELECT_MODE, SelectorPicture.Key.MODE_SINGLE);
                mContext.startActivityForResult(intent, Request.REQUEST_SINGLE_PICK);
                break;
            case SelectMode.MULTIPLE:
                intent.putExtra(SelectorPicture.Key.EXTRA_SELECT_MODE, SelectorPicture.Key.MODE_MULTI);
                intent.putExtra(SelectorPicture.Key.EXTRA_SELECT_COUNT, mSelectCount);
                mContext.startActivityForResult(intent, Request.REQUEST_MULTI_PICK);
                break;
        }
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
            resolveResultCanceled(requestCode);
            return;
        }

        switch (requestCode) {
            case Request.REQUEST_SINGLE_PICK:
                break;
            case Request.REQUEST_SINGLE_CAMERA:
                break;
            case Request.REQUEST_MULTI_PICK:
                break;
            case Request.REQUEST_MULTI_CAMERA:
                break;
            case Request.REQUEST_CROP_IMAGE:
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
                YToast.show(mContext, R.string.cancle_photo);
                break;
        }
    }

    /**
     * 提示已经选了多少图片
     */
    private void notifySelectPhoto(List<String> pathList, int count) {
        if (mListener != null) {
            mListener.onSelectedPhoto(pathList, count);
        }
    }

    /**
     * 设置选择图片的监听
     */
    public void setOnSelectPhotoListener(OnSelectPhotoListener listener) {
        mListener = listener;
    }

    /**
     * 定义图片是单选还是多选
     */
    @IntDef({SelectMode.SINGLE, SelectMode.MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface selectMode {
    }

    @Keep
    public final static class SelectMode {
        /**
         * 单选
         */
        public final static int SINGLE = 1;
        /**
         * 多选
         */
        public final static int MULTIPLE = 2;
    }

    /**
     * 定义选择完图片后压缩还是剪切
     */
    @IntDef({ResultMode.COMPRESS, ResultMode.CROP})
    @Retention(RetentionPolicy.SOURCE)
    @interface resultMode {
    }

    public final static class ResultMode {
        /**
         * 压缩
         */
        public final static int COMPRESS = 1;
        /**
         * 剪切
         */
        public final static int CROP = 2;
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
    }
}
