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

package com.bright.common.utils.photo.select;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.app.activity.MultiImageSelectorActivity;
import com.bright.common.model.MultiSelectorImage;
import com.bright.common.utils.photo.CompressImageUtils;
import com.bright.common.widget.YToast;
import com.bright.common.widget.loading.LoadingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 上传图片以及添加图片的封装类
 */
public class MultipleSelectPhotoUtils extends SelectPhotoUtils {
    private static final String TAG = "MultipleSelectPhoto";
    /**
     * 最多选择多少张图片
     */
    public static final int MAX_PHOTO_COUNT = 9;
    /**
     * 最多几个线程进行同时压缩
     */
    public static final int MAX_THREAD = 3;

    private CallBack mCallBack;
    /**
     * 选择图片的张数
     */
    private int mCount;
    /**
     * 图片是否压缩完毕
     */
    private boolean isAreadyOk = false;
    // 线程池 多线程处理压缩图片
    private ExecutorService mExecutorService;
    /**
     * 保存图片的地址的
     */
    private List<String> mPhotos = Collections.synchronizedList(new ArrayList<String>());

    public MultipleSelectPhotoUtils(Activity context) {
        super(context);
    }

    public void select() {
        select(MAX_PHOTO_COUNT);
    }

    public void select(int count) {
        mCount = count;
        showSelectDialog();
    }

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
                intent = new Intent(mContext, MultiImageSelectorActivity.class);
                intent.putExtra(MultiSelectorImage.Key.EXTRA_SELECT_COUNT, mCount);
                mContext.startActivityForResult(intent, Request.REQUEST_PICK);
                break;
        }
    }

    // 用来接管activity的result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED) {
            YToast.makeText(mContext, R.string.cancle_photo, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case Request.REQUEST_CAMERA:
                mLoadingDialog = LoadingDialog.show(mContext);
                mPhotos.clear();
                mPhotos.add("camera");
                isAreadyOk = false;
                // 设置最多线程同时上传图片
                mExecutorService = Executors.newFixedThreadPool(MAX_THREAD);
                mExecutorService.execute(new CompressRunnable(mCameraFileUri.getPath(), 0));
                // 关闭线程池
                mExecutorService.shutdown();
                break;
            case Request.REQUEST_PICK:
                List<String> photots = data.getStringArrayListExtra(MultiSelectorImage.Key.EXTRA_RESULT);
                mLoadingDialog = LoadingDialog.show(mContext);
                mPhotos.clear();
                // 把没有压缩前添加进入当占位符
                mPhotos.addAll(photots);
                isAreadyOk = false;
                // 设置最多线程同时上传图片
                mExecutorService = Executors.newFixedThreadPool(MAX_THREAD);
                for (int i = 0; i < photots.size(); i++) {
                    String photo = photots.get(i);
                    mExecutorService.execute(new CompressRunnable(photo, i));
                }

                // 关闭线程池
                mExecutorService.shutdown();
                break;
        }
    }


    private class CompressRunnable implements Runnable {
        String path;
        int key;

        public CompressRunnable(String path, int key) {
            this.path = path;
            this.key = key;
        }

        @Override
        public void run() {
            String compressPath = generateUri().getPath();
            CompressImageUtils.compreeImage(path, compressPath);
            mPhotos.set(key, compressPath);
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mCallBack == null) {
                LoadingDialog.dismiss(mLoadingDialog);
                return;
            }

            if (mExecutorService.isTerminated() && !isAreadyOk) {
                isAreadyOk = true;
                mCallBack.onResult(mPhotos);
                Log.i(TAG, "mCallBack  = =" + mPhotos.size());
                LoadingDialog.dismiss(mLoadingDialog);
            }

        }
    };


    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onResult(List<String> urls);
    }
}
