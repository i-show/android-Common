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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bright.common.R;
import com.bright.common.app.activity.BaseActivity;
import com.bright.common.entries.Folder;
import com.bright.common.entries.Photo;
import com.bright.common.utils.AnimatorUtils;
import com.bright.common.utils.DateUtils;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.recyclerview.itemdecoration.GridSpacingItemDecoration;

import java.util.List;

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

public class PhotoSelectorActivity extends BaseActivity implements
        PhotoSelectorContract.View {

    private PhotoSelectorAdapter mPhotoAdapter;
    private GridLayoutManager mLayoutManager;
    private int mMaxCount;
    private int mMode;

    private TextView mRightTextView;
    private TextView mTimeLine;

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

        mPhotoAdapter = new PhotoSelectorAdapter(this);
        mPhotoAdapter.setSelectedChangedListener(mSelectedChangedListener);
        mPhotoAdapter.setMaxCount(mMaxCount);

        mLayoutManager = new GridLayoutManager(this, 3);
        RecyclerView photoList = (RecyclerView) findViewById(R.id.list);
        photoList.setLayoutManager(mLayoutManager);
        photoList.addItemDecoration(new GridSpacingItemDecoration(this, R.dimen.photo_selector_item_gap));
        photoList.setAdapter(mPhotoAdapter);
        photoList.addOnScrollListener(mScrollListener);
    }


    @Override
    public void updateUI(List<Photo> photoList, List<Folder> folderList) {
        mPhotoAdapter.setData(photoList);
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
}
