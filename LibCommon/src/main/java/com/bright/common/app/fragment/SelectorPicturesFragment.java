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

package com.bright.common.app.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.bright.common.R;
import com.bright.common.adapter.SelectorFolderAdapter;
import com.bright.common.adapter.SelectorPicturesAdapter;
import com.bright.common.entries.Folder;
import com.bright.common.entries.Photo;
import com.bright.common.utils.AnimatorUtils;
import com.bright.common.utils.DateUtils;
import com.bright.common.widget.dialog.BaseDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择Fragment
 */
public class SelectorPicturesFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "MultiImageSelector";

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    /**
     * 已经选择了的图片的路径
     */
    private ArrayList<String> mAlreadySelectedPath = new ArrayList<>();
    /**
     * 选中图片
     */
    private List<Photo> mAlreadySelectedImage = new ArrayList<>();

    private Folder mSelectedFolder;

    // 图片Grid
    private GridView mGridView;

    private SelectorPicturesAdapter mImageAdapter;
    private SelectorFolderAdapter mFolderAdapter;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;

    private Callback mCallback;

    private int mDesireImageCount;

    private File mTmpFile;
    private int mMode;

    public static SelectorPicturesFragment newInstance(Bundle args) {
        SelectorPicturesFragment fragment = new SelectorPicturesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement SelectorPicturesFragment.Callback interface...");
        }

        Bundle args = getArguments();

        // 选择图片数量
        mDesireImageCount = args.getInt(Photo.Key.EXTRA_SELECT_COUNT);
        // 图片选择模式
        mMode = args.getInt(Photo.Key.EXTRA_SELECT_MODE);
        // 默认选择
        if (mMode == Photo.Key.MODE_MULTI) {
            ArrayList<String> tmp = args.getStringArrayList(Photo.Key.EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                mAlreadySelectedPath = tmp;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi_choice_pictures, container, false);

        mTimeLineText = (TextView) root.findViewById(R.id.timeline);

        // 文件夹分类
        mCategoryText = (TextView) root.findViewById(R.id.category);
        mCategoryText.setText(R.string.all_photos);
        mCategoryText.setOnClickListener(this);

        mImageAdapter = new SelectorPicturesAdapter(getActivity());
        mImageAdapter.setMultiSelector(mMode == Photo.Key.MODE_MULTI);
        mImageAdapter.setMaxSelectedCount(mDesireImageCount);
        mImageAdapter.setCallBack(mImageAdapterCallBack);

        mGridView = (GridView) root.findViewById(R.id.grid);
        mGridView.setOnScrollListener(mScrollListener);
        mGridView.setAdapter(mImageAdapter);
        computeGridItemSize();

        mFolderAdapter = new SelectorFolderAdapter(getActivity());
        return root;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.category) {
            showImageFolderDialog();
        }
    }


    /**
     * 弹出选择Image文件夹
     */
    private void showImageFolderDialog() {
        BaseDialog dialog = new BaseDialog.Builder(getActivity(), R.style.Dialog_Bottom)
                .isShowFromBottom(true)
                .setAdapter(mFolderAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        selectImageFloder(which);
                    }
                }).create();
        dialog.show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        computeGridItemSize();
    }

    private void selectImageFloder(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Folder folder = mFolderAdapter.getItem(position);
                folder.isSelected = true;
                mFolderAdapter.notifyDataSetChanged();

                mImageAdapter.setData(folder.images);
                mCategoryText.setText(folder.name);

                if (mSelectedFolder != null) {
                    mSelectedFolder.isSelected = false;
                }
                mSelectedFolder = folder;

                // 滑动到最初始位置
                mGridView.smoothScrollToPosition(0);
            }
        }, 100);
    }

    /**
     * 通知图片的选中状态有改变
     */
    private void notifyImageSelectedStateChanged(Photo entry, boolean selected) {
        if (entry == null) {
            Log.i(TAG, "notifyImageSelectedStateChanged: entry is null");
            return;
        }

        if (mCallback == null) {
            Log.i(TAG, "notifyImageSelectedStateChanged:  call back is null , just do nothing");
            return;
        }

        switch (mMode) {
            case Photo.Key.MODE_SINGLE:
                mCallback.onSingleImageSelected(entry.path);
                break;
            case Photo.Key.MODE_MULTI:
                mCallback.onImageSelectedStateChanged(entry.path, selected);
                break;
        }
    }

    private SelectorPicturesAdapter.CallBack mImageAdapterCallBack = new SelectorPicturesAdapter.CallBack() {
        @Override
        public void onSelectImage(Photo entry, boolean selected) {
            notifyImageSelectedStateChanged(entry, selected);
        }
    };


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,

                MediaStore.Images.Media._ID};
        /**
         * 路径的index
         */
        public final static int INDEX_PATH = 0;
        /**
         * 名称的index
         */
        public final static int INDEX_NAME = 1;
        /**
         * 时间的index
         */
        public final static int INDEX_ADDED_DATE = 2;
        /**
         * 时间的index
         */
        public final static int INDEX_MODIFY_DATE = 3;
        /**
         * 文件夹 ID
         */
        public final static int INDEX_FLODER_ID = 4;
        /**
         * 文件夹 名称
         */
        public final static int INDEX_FLODER_NAME = 5;

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                return new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null, null,
                        IMAGE_PROJECTION[INDEX_MODIFY_DATE] + " DESC");
            } else if (id == LOADER_CATEGORY) {
                return new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
                        null,
                        IMAGE_PROJECTION[INDEX_MODIFY_DATE] + " DESC");
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor == null || cursor.isClosed()) {
                Log.i(TAG, "onLoadFinished:  data is null or have alread closed");
                return;
            }

            // 如果load重新加载那么重新制作当前已经保存的path
            checkSelectedPath();

            List<Photo> images = new ArrayList<>();
            List<Folder> folders = new ArrayList<>();
            Log.i(TAG, "onLoadFinished: cursor size = " + cursor.getCount());
            while (cursor.moveToNext()) {
                String path = cursor.getString(INDEX_PATH);
                String name = cursor.getString(INDEX_NAME);
                String folderId = cursor.getString(INDEX_FLODER_ID);
                String folderName = cursor.getString(INDEX_FLODER_NAME);
                long modifyDate = cursor.getLong(INDEX_MODIFY_DATE);

                Photo image = new Photo(path, name, modifyDate, folderName);
                image.isSelected = mAlreadySelectedPath.contains(path);
                images.add(image);
                if (image.isSelected) {
                    mAlreadySelectedImage.add(image);
                }

                // 获取文件夹名称
                Folder folder = new Folder();
                folder.id = folderId;
                folder.name = folderName;
                folder.cover = image;

                if (!folders.contains(folder)) {
                    folder.addImage(image);
                    folders.add(folder);
                } else {
                    Folder f = folders.get(folders.indexOf(folder));
                    f.addImage(image);
                }
            }
            cursor.close();

            mImageAdapter.setSelectedImages(mAlreadySelectedImage);
            mImageAdapter.setData(images);

            Folder all = new Folder();
            all.id = "all";
            all.name = getString(R.string.all_photos);
            all.cover = images.isEmpty() ? null : images.get(0);
            all.images = images;
            all.isSelected = true;
            folders.add(0, all);
            // 这个地方loader 可能会自动刷新 虽然概率很小
            if (mSelectedFolder == null) {
                mSelectedFolder = all;
            } else {
                all.isSelected = false;
                for (Folder folder : folders) {
                    if (folder.equals(mSelectedFolder)) {
                        mSelectedFolder = folder;
                        folder.isSelected = true;
                    }
                }
            }

            mFolderAdapter.setData(folders);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    /**
     * 检测是否需要更新路径
     * 目的：当loader 自动刷新的时候需要重新生成
     */
    private void checkSelectedPath() {
        if (mAlreadySelectedImage == null || mAlreadySelectedImage.isEmpty()) {
            Log.i(TAG, "checkSelectedPath:  not need to check alread is empty");
            return;
        }

        mAlreadySelectedPath.clear();
        for (Photo image : mAlreadySelectedImage) {
            mAlreadySelectedPath.add(image.path);
        }

        mAlreadySelectedImage.clear();
    }

    private void computeGridItemSize() {
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();
                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.photo_selector_item_gap);
                final int numCount = width / desireSize;
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {
            if (state == SCROLL_STATE_IDLE) {
                AnimatorUtils.alpha(mTimeLineText, mTimeLineText.getAlpha(), 0, 800);
            } else if (state == SCROLL_STATE_FLING && mGridView.getFirstVisiblePosition() != 0) {
                AnimatorUtils.alpha(mTimeLineText, 0, 1.0f, 800);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mTimeLineText.getAlpha() >= 0.15f) {
                Photo image = mImageAdapter.getItem(firstVisibleItem);
                mTimeLineText.setText(DateUtils.formatFriendly(getActivity(), image.modifyDate * 1000));
            }
        }
    };

    /**
     * 回调接口
     */
    public interface Callback {
        /**
         * 单选图片
         */
        void onSingleImageSelected(String path);

        /**
         * 图片的选中状态改变
         */
        void onImageSelectedStateChanged(String path, boolean selected);

        /**
         * 点击了Camera
         */
        void onCameraShot(File imageFile);
    }
}
