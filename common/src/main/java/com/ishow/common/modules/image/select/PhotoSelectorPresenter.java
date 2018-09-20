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

package com.ishow.common.modules.image.select;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import com.ishow.common.R;
import com.ishow.common.entries.Folder;
import com.ishow.common.entries.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bright.Yu on 2017/1/24.
 * 照片选择器
 */

class PhotoSelectorPresenter implements PhotoSelectorContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "PhotoSelectorPresenter";

    /**
     * 小于15K的照片不进行显示
     */
    private static final long MIN_PHOTO_SIZE = 15 * 1024;
    /**
     * 搜索的列
     */
    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,

            MediaStore.Images.Media._ID};

    /**
     * 路径的index
     */
    private final static int INDEX_PATH = 0;
    /**
     * 名称的index
     */
    private final static int INDEX_NAME = 1;
    /**
     * 时间的index
     */
    private final static int INDEX_SIZE = 2;
    /**
     * 时间的index
     */
    private final static int INDEX_MODIFY_DATE = 3;
    /**
     * 文件夹 ID
     */
    private final static int INDEX_FLODER_ID = 4;
    /**
     * 文件夹 名称
     */
    private final static int INDEX_FLODER_NAME = 5;

    private PhotoSelectorContract.View mView;

    private PhotoSelectorActivity mActivity;

    private LoaderManager mLoaderManager;

    PhotoSelectorPresenter(PhotoSelectorActivity activity, PhotoSelectorContract.View view) {
        mActivity = activity;
        mView = view;
        mLoaderManager = activity.getSupportLoaderManager();
    }

    @Override
    public void start(Context context) {
        mLoaderManager.initLoader(0, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mActivity,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION,
                null, null,
                IMAGE_PROJECTION[INDEX_MODIFY_DATE] + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.isClosed()) {
            Log.i(TAG, "onLoadFinished:  data is null or have alread closed");
            mView.showEmpty(null);
            return;
        }

        List<Photo> photoList = new ArrayList<>();
        List<Folder> folderList = new ArrayList<>();
        while (data.moveToNext()) {
            String path = data.getString(INDEX_PATH);
            String name = data.getString(INDEX_NAME);
            String folderId = data.getString(INDEX_FLODER_ID);
            String folderName = data.getString(INDEX_FLODER_NAME);
            long size = data.getLong(INDEX_SIZE);
            long modifyDate = data.getLong(INDEX_MODIFY_DATE);

            if (size < MIN_PHOTO_SIZE) {
                continue;
            }

            Photo photo = new Photo(path, name, modifyDate, folderName);
            photoList.add(photo);

            resolvePhotoFolder(folderList, photo, folderId, folderName);
        }

        addAllPhotoFolder(folderList, photoList);
        data.close();

        mView.updateUI(photoList, folderList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    /**
     * 处理照片文件夹信息
     */
    private void resolvePhotoFolder(List<Folder> folderList, Photo photo, String folderId, String folderName) {
        boolean contains = false;
        for (Folder folder : folderList) {
            if (TextUtils.equals(folderId, folder.id)) {
                folder.addPhoto(photo);
                contains = true;
                break;
            }
        }

        if (!contains) {
            Folder folder = new Folder(folderId, folderName, photo);
            folderList.add(folder);
        }
    }

    /**
     * 增加所有照片选项
     */
    private void addAllPhotoFolder(List<Folder> folderList, List<Photo> photoList) {
        String floderName = mActivity.getString(R.string.all_photos);
        Folder all = new Folder("all", floderName, photoList.isEmpty() ? null : photoList.get(0));
        all.addAll(photoList);
        all.isSelected = true;
        folderList.add(0, all);
    }
}
