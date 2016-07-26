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

package com.bright.common.app.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.adapter.MultiSelectorFolderAdapter;
import com.bright.common.adapter.MultiSelectorImageAdapter;
import com.bright.common.app.BaseFragment;
import com.bright.common.model.MultiSelectorFolder;
import com.bright.common.model.MultiSelectorImage;
import com.bright.common.utils.DateUtils;
import com.bright.common.utils.ImageUtils;
import com.bright.common.widget.YToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择Fragment
 */
public class MultiImageSelectorFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "MultiImageSelector";

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    // 结果数据
    private ArrayList<String> mResultList = new ArrayList<>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private MultiSelectorImageAdapter mImageAdapter;
    private MultiSelectorFolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 底部View
    private View mFootContent;

    private int mDesireImageCount;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    private int mGridWidth, mGridHeight;

    private File mTmpFile;
    private int mMode;

    public static MultiImageSelectorFragment newInstance(Bundle args) {
        MultiImageSelectorFragment fragment = new MultiImageSelectorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }

        Bundle args = getArguments();

        // 选择图片数量
        mDesireImageCount = args.getInt(MultiSelectorImage.Key.EXTRA_SELECT_COUNT);
        // 是否显示照相机
        mIsShowCamera = args.getBoolean(MultiSelectorImage.Key.EXTRA_SHOW_CAMERA, false);
        // 图片选择模式
        mMode = args.getInt(MultiSelectorImage.Key.EXTRA_SELECT_MODE);
        // 默认选择
        if (mMode == MultiSelectorImage.Key.MODE_MULTI) {
            ArrayList<String> tmp = args.getStringArrayList(MultiSelectorImage.Key.EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                mResultList = tmp;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_multi_select_image, container, false);

        mFootContent = root.findViewById(R.id.footer);

        mTimeLineText = (TextView) root.findViewById(R.id.timeline);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        // 文件夹分类
        mCategoryText = (TextView) root.findViewById(R.id.category);
        mCategoryText.setText(R.string.folder_all);
        mCategoryText.setOnClickListener(this);

        mImageAdapter = new MultiSelectorImageAdapter(getActivity(), mIsShowCamera);
        mImageAdapter.setMultiSelector(mMode == MultiSelectorImage.Key.MODE_MULTI);
        mImageAdapter.setMaxSelectedCount(mDesireImageCount);
        mImageAdapter.setCallBack(mImageAdapterCallBack);

        mGridView = (GridView) root.findViewById(R.id.grid);
        mGridView.setOnScrollListener(mScrollListener);
        mGridView.setAdapter(mImageAdapter);
        compute();

        mFolderAdapter = new MultiSelectorFolderAdapter(getActivity());
        return root;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.category) {
            if (mFolderPopupWindow == null) {
                createPopupFolderList(mGridWidth, mGridHeight);
            }

            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.show();
                int index = mFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.getListView().setSelection(index);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mFootContent);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText(R.string.folder_all);
                            if (mIsShowCamera) {
                                mImageAdapter.setShowCamera(true);
                            } else {
                                mImageAdapter.setShowCamera(false);
                            }
                        } else {
                            MultiSelectorFolder folder = mFolderAdapter.getRealItem(index);
                            mImageAdapter.setData(folder.images);
                            mCategoryText.setText(folder.name);
                            // 设定默认选择
                            if (mResultList != null && mResultList.size() > 0) {
                                mImageAdapter.setDefaultSelected(mResultList);
                            }
                            mImageAdapter.setShowCamera(false);
                        }

                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
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

        if (mFolderPopupWindow != null && mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        }
        compute();
        super.onConfigurationChanged(newConfig);

    }

    /**
     * 选择相机
     */
    private void goToCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = ImageUtils.generatePhotoName(getActivity());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            YToast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片操作
     */
    private void selectImageFromGrid(MultiSelectorImage entry) {
        if (entry == null) {
            Log.i(TAG, "selectImageFromGrid: entry is null");
            return;
        }


        // 多选模式
        if (mMode == MultiSelectorImage.Key.MODE_MULTI) {
            if (mResultList.contains(entry.path)) {
                mResultList.remove(entry.path);

                if (mCallback != null) {
                    mCallback.onImageUnselected(entry.path);
                }
            } else {

                mResultList.add(entry.path);
                if (mCallback != null) {
                    mCallback.onImageSelected(entry.path);
                }
            }
        } else if (mMode == MultiSelectorImage.Key.MODE_SINGLE) {
            // 单选模式
            if (mCallback != null) {
                mCallback.onSingleImageSelected(entry.path);
            }
        }
    }

    private MultiSelectorImageAdapter.CallBack mImageAdapterCallBack = new MultiSelectorImageAdapter.CallBack() {
        @Override
        public void onClickCamera() {
            goToCamera();
        }

        @Override
        public void onSelectImage(MultiSelectorImage entry) {
            // 正常操作
            selectImageFromGrid(entry);
        }
    };


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                return new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null, null,
                        IMAGE_PROJECTION[2] + " DESC");
            } else if (id == LOADER_CATEGORY) {
                return new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
                        null,
                        IMAGE_PROJECTION[2] + " DESC");
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor == null || cursor.isClosed()) {
                Log.i(TAG, "onLoadFinished:  data is null or have alread closed");
                return;
            }
            List<MultiSelectorImage> images = new ArrayList<>();
            List<MultiSelectorFolder> folders = new ArrayList<>();

            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                MultiSelectorImage image = new MultiSelectorImage(path, name, dateTime);
                images.add(image);
                if (!hasFolderGened) {
                    // 获取文件夹名称
                    File imageFile = new File(path);
                    File folderFile = imageFile.getParentFile();
                    MultiSelectorFolder folder = new MultiSelectorFolder();
                    folder.name = folderFile.getName();
                    folder.path = folderFile.getAbsolutePath();
                    folder.cover = image;
                    if (!folders.contains(folder)) {
                        List<MultiSelectorImage> imageList = new ArrayList<>();
                        imageList.add(image);
                        folder.images = imageList;
                        folders.add(folder);
                    } else {
                        // 更新
                        MultiSelectorFolder f = folders.get(folders.indexOf(folder));
                        f.images.add(image);
                    }
                }
            }
            cursor.close();

            mImageAdapter.setData(images);

            // 设定默认选择
            if (mResultList != null && mResultList.size() > 0) {
                mImageAdapter.setDefaultSelected(mResultList);
            }

            mFolderAdapter.setData(folders);
            hasFolderGened = true;

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    private void compute() {
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();
                final int height = mGridView.getHeight();

                mGridWidth = width;
                mGridHeight = height;

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                final int numCount = width / desireSize;
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (mFolderPopupWindow != null) {
                    mFolderPopupWindow.setHeight(height * 5 / 8);
                }

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
                mTimeLineText.setVisibility(View.GONE);
            } else if (state == SCROLL_STATE_FLING) {
                mTimeLineText.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mTimeLineText.getVisibility() == View.VISIBLE) {
                int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                MultiSelectorImage image = (MultiSelectorImage) view.getAdapter().getItem(index);
                if (image != null) {
                    mTimeLineText.setText(DateUtils.getLastModifiedTime(image.path));
                }
            }
        }
    };

    /**
     * 回调接口
     */
    public interface Callback {
        void onSingleImageSelected(String path);

        void onImageSelected(String path);

        void onImageUnselected(String path);

        void onCameraShot(File imageFile);
    }
}
