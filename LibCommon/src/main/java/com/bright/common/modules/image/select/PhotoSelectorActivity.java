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

package com.bright.common.modules.image.select;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bright.common.R;
import com.bright.common.app.activity.BaseActivity;
import com.bright.common.entries.Folder;
import com.bright.common.entries.Photo;
import com.bright.common.utils.AnimatorUtils;
import com.bright.common.utils.DateUtils;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.dialog.BaseDialog;
import com.bright.common.widget.recyclerview.itemdecoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

public class PhotoSelectorActivity extends BaseActivity implements
        View.OnClickListener,
        PhotoSelectorContract.View {
    private static final String TAG = "PhotoSelectorActivity";
    private PhotoSelectorAdapter mPhotoAdapter;
    private FolderSelectorAdapter mFolderAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private TextView mRightTextView;
    private TextView mTimeLine;
    private TextView mFolderTextView;

    private int mMaxCount;
    private int mMode;
    private Folder mSelectedFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhotoSelector);
        setContentView(R.layout.activity_photo_selector);

        PhotoSelectorContract.Presenter presenter = new PhotoSelectorPresenter(this, this);
        presenter.start(this);
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        Intent intent = getIntent();
        mMaxCount = intent.getIntExtra(Photo.Key.EXTRA_SELECT_COUNT, Photo.Key.DEAFULT_MAX_COUNT);
        mMode = intent.getIntExtra(Photo.Key.EXTRA_SELECT_MODE, Photo.Key.MODE_MULTI);
    }

    @Override
    protected void initViews() {
        super.initViews();

        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        final int padding = getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
        mRightTextView = topBar.getRightTextView();
        mRightTextView.setEnabled(false);
        mRightTextView.setPadding(padding, mRightTextView.getPaddingTop(), padding, mRightTextView.getPaddingBottom());

        mTimeLine = (TextView) findViewById(R.id.time_line);

        mFolderAdapter = new FolderSelectorAdapter(this);

        mPhotoAdapter = new PhotoSelectorAdapter(this);
        mPhotoAdapter.setSelectedChangedListener(mSelectedChangedListener);
        mPhotoAdapter.setMaxCount(mMode == Photo.Key.MODE_MULTI ? mMaxCount : 1);

        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(this, R.dimen.photo_selector_item_gap));
        mRecyclerView.setAdapter(mPhotoAdapter);
        mRecyclerView.addOnScrollListener(mScrollListener);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_photo_selector_floder);
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.color_accent));
        mFolderTextView = (TextView) findViewById(R.id.folder);
        mFolderTextView.setOnClickListener(this);
        mFolderTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
        mFolderTextView.setText(R.string.all_photos);
    }


    @Override
    public void updateUI(List<Photo> photoList, List<Folder> folderList) {
        mPhotoAdapter.setData(photoList);
        mFolderAdapter.setData(folderList);
        mSelectedFolder = folderList.isEmpty() ? null : folderList.get(0);
    }

    @Override
    public void showLoading(String message, boolean dialog) {

    }

    @Override
    public void dismissLoading(boolean dialog) {

    }

    @Override
    public void showError(String message, boolean dialog, int errorType) {

    }

    @Override
    public void showSuccess(String message) {

    }

    @Override
    public void showEmpty(String message) {

    }

    private PhotoSelectorAdapter.OnSelectedChangedListener mSelectedChangedListener = new PhotoSelectorAdapter.OnSelectedChangedListener() {
        @Override
        public void onSelectedChanged(int selectCount) {
            if (selectCount <= 0) {
                mRightTextView.setEnabled(false);
                mRightTextView.setText(R.string.complete);
            } else if (mMode == Photo.Key.MODE_SINGLE) {
                mRightTextView.setEnabled(true);
                mRightTextView.setText(R.string.complete);
            } else {
                mRightTextView.setEnabled(true);
                String count = getString(R.string.link_complete, selectCount, mMaxCount);
                mRightTextView.setText(count);
            }
        }
    };

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mTimeLine.clearAnimation();
                AnimatorUtils.alpha(mTimeLine, mTimeLine.getAlpha(), 0, 800);
            } else {
                mTimeLine.clearAnimation();
                AnimatorUtils.alpha(mTimeLine, mTimeLine.getAlpha(), 1.0f, 800);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (mTimeLine.getAlpha() >= 0.15f) {
                Photo image = mPhotoAdapter.getItem(mLayoutManager.findFirstVisibleItemPosition());
                mTimeLine.setText(DateUtils.formatFriendly(PhotoSelectorActivity.this, image.modifyDate * 1000));
            }
        }
    };

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.folder) {
            selectPhotoFolder();
        }
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        setResult();
    }

    /**
     * 选择图片文件夹
     */
    private void selectPhotoFolder() {
        BaseDialog dialog = new BaseDialog.Builder(this, R.style.Dialog_Bottom)
                .isShowFromBottom(true)
                .setAdapter(mFolderAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        Folder folder = mFolderAdapter.getItem(which);
                        updatePhotos(folder);
                    }
                }).create();
        dialog.show();
    }

    private void updatePhotos(Folder folder) {

        if (folder.equals(mSelectedFolder)) {
            Log.i(TAG, "updatePhotos: is same folder");
            return;
        }

        folder.isSelected = true;
        if (mSelectedFolder != null) {
            mSelectedFolder.isSelected = false;
        }
        mSelectedFolder = folder;

        mFolderTextView.setText(folder.getName());
        mPhotoAdapter.setData(folder.getPhotoList());
        mFolderAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }


    private void setResult() {
        List<Photo> photoList = mPhotoAdapter.getSelectedPhotos();
        switch (mMode) {
            case Photo.Key.MODE_SINGLE:
                if (photoList.isEmpty()) {
                    Log.i(TAG, "setResult: photoList is null or empty");
                    finish();
                    return;
                }
                Photo photo = photoList.get(0);
                // 返回已选择的图片数据
                Intent singleData = new Intent();
                singleData.putExtra(Photo.Key.EXTRA_RESULT, photo.getPath());
                setResult(RESULT_OK, singleData);
                finish();
                break;
            case Photo.Key.MODE_MULTI:
                if (photoList.isEmpty()) {
                    Log.i(TAG, "setResult: photoList is null or empty");
                    finish();
                    return;
                }

                ArrayList<String> photoPaths = new ArrayList<>();
                for (Photo _photo : photoList) {
                    photoPaths.add(_photo.getPath());
                }

                // 返回已选择的图片数据
                Intent multiData = new Intent();
                multiData.putStringArrayListExtra(Photo.Key.EXTRA_RESULT, photoPaths);
                setResult(RESULT_OK, multiData);
                finish();

                break;
        }
    }

}
