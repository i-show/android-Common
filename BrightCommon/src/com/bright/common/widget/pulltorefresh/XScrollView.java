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

package com.bright.common.widget.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.bright.common.R;

/**
 * XScrollView, modified from {@link XListView}
 */
public class XScrollView extends ScrollView implements IPullToRefresh, OnScrollListener {
    private static final String TAG = "XScrollView";
    /**
     * 重置头部高度
     */
    private static final int HANDLER_RESET_HEADER_HEIGHT = 1;
    /**
     * 重置Footer高度
     */
    private static final int HANDLER_RESET_FOOTER_HEIGHT = 2;

    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;

    // when pull up >= 50px
    private final static int PULL_LOAD_MORE_DELTA = 50;

    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;

    // used for scroll back
    private Scroller mScroller;
    // user's scroll listener
    private OnScrollListener mScrollListener;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    // the interface to trigger refresh and load more.
    private OnPullToRefreshListener mListener;

    private LinearLayout mLayout;
    private LinearLayout mContentLayout;

    // header view content, use it to calculate the Header's height. And hide it when disable pull refresh.
    private RelativeLayout mHeaderContent;
    private int mHeaderHeight;

    private XHeaderView mHeaderView;
    private XFooterView mFooterView;

    private boolean mEnablePullRefresh = true;
    private boolean mEnablePullLoad = true;
    private boolean mEnableAutoLoad = false;

    private boolean mPullRefreshing = false;
    private boolean mPullLoading = false;

    private Handler mHandler;

    public XScrollView(Context context) {
        this(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context, attrs);
    }


    private void initWithContext(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XScrollView);
        int headerLineVisibility = a.getInt(R.styleable.XScrollView_headerLineVisibility, View.VISIBLE);
        int footerLineVisibility = a.getInt(R.styleable.XScrollView_footerLineVisibility, View.VISIBLE);
        a.recycle();

        mLayout = (LinearLayout) View.inflate(context, R.layout.widget_pulltorefresh_scrollview, null);
        mContentLayout = (LinearLayout) mLayout.findViewById(R.id.content_layout);

        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XScrollView need the scroll event, and it will dispatch the event to user's listener (as a proxy).
        this.setOnScrollListener(this);

        // init header view
        mHeaderView = new XHeaderView(context);
        mHeaderView.setLineVisibility(headerLineVisibility);
        mHeaderContent = (RelativeLayout) mHeaderView.findViewById(R.id.header_content);

        LinearLayout headerLayout = (LinearLayout) mLayout.findViewById(R.id.header_layout);
        headerLayout.addView(mHeaderView);

        // init footer view
        mFooterView = new XFooterView(context);
        mFooterView.setLineVisibility(footerLineVisibility);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        LinearLayout footLayout = (LinearLayout) mLayout.findViewById(R.id.footer_layout);
        footLayout.addView(mFooterView, params);

        // init header height
        ViewTreeObserver observer = mHeaderView.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    mHeaderHeight = mHeaderContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();
                    if (null != observer) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }

        this.addView(mLayout);
    }

    /**
     * Set the content ViewGroup for XScrollView.
     *
     * @param content
     */
    public void setContentView(ViewGroup content) {
        if (mLayout == null) {
            return;
        }

        if (mContentLayout == null) {
            mContentLayout = (LinearLayout) mLayout.findViewById(R.id.content_layout);
        }

        if (mContentLayout.getChildCount() > 0) {
            mContentLayout.removeAllViews();
        }
        mContentLayout.addView(content);
    }

    /**
     * Set the content View for XScrollView.
     *
     * @param content
     */
    public void setView(View content) {
        if (mLayout == null) {
            return;
        }

        if (mContentLayout == null) {
            mContentLayout = (LinearLayout) mLayout.findViewById(R.id.content_layout);
        }
        mContentLayout.addView(content);
    }

    /**
     * Enable or disable pull down refresh feature.
     */
    @Override
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;

        // disable, hide the content
        mHeaderContent.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);

        if (!mEnablePullRefresh) {
            setHeaderDividersEnabled(true);
        } else {
            setHeaderDividersEnabled(false);
        }
    }

    /**
     * Enable or disable pull up load more feature.
     */
    @Override
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;

        if (!mEnablePullLoad) {
            mFooterView.setBottomMargin(0);
            mFooterView.hide();
            mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
            mFooterView.setOnClickListener(null);
            setFooterDividersEnabled(true);
        } else {
            mPullLoading = false;
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterView.show();
            mFooterView.setState(XFooterView.STATE_NORMAL);
            setFooterDividersEnabled(false);
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * Enable or disable auto load more feature when scroll to bottom.
     */
    @Override
    public void setAutoLoadEnable(boolean enable) {
        mEnableAutoLoad = enable;
    }

    @Override
    public void setLoadingState() {
        //TODO
    }

    @Override
    public void setEmptyState() {
        //TODO
    }

    /**
     * Stop refresh, reset header view.
     */
    @Override
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            mHandler.sendEmptyMessage(HANDLER_RESET_HEADER_HEIGHT);
        }
    }

    /**
     * 刷新成功
     */
    @Override
    public void setRefreshSuccess() {
        if (!mPullRefreshing) {
            Log.i(TAG, "setRefreshSuccess: Status is incorrect");
            return;
        }
        mPullRefreshing = false;
        mHeaderView.setState(XHeaderView.STATE_REFRESH_SUCCESS);
        mHandler.sendEmptyMessageDelayed(HANDLER_RESET_HEADER_HEIGHT, 500);

    }

    /**
     * 刷新失败
     */
    @Override
    public void setRefreshFail() {
        if (!mPullRefreshing) {
            Log.i(TAG, "setRefreshFail: Status is incorrect");
            return;
        }
        mPullRefreshing = false;
        mHeaderView.setState(XHeaderView.STATE_REFRESH_SUCCESS);
        mHandler.sendEmptyMessageDelayed(HANDLER_RESET_HEADER_HEIGHT, 500);
    }

    /**
     * 停止加载更多
     */
    @Override
    public void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(XFooterView.STATE_NORMAL);
        }
    }

    /**
     * 已经全部加载完毕
     */
    @Override
    public void setLoadEnd() {
        mPullLoading = false;
        mEnablePullLoad = false;
        mFooterView.show();
        mFooterView.setState(XFooterView.STATE_END);
        mFooterView.setOnClickListener(null);
        setFooterDividersEnabled(false);
    }


    /**
     * 自动刷新
     */
    @Override
    public void autoRefresh() {
        mHeaderView.setVisibleHeight(mHeaderHeight);

        if (mEnablePullRefresh && !mPullRefreshing) {
            // update the arrow image not refreshing
            if (mHeaderView.getVisibleHeight() > mHeaderHeight) {
                mHeaderView.setState(XHeaderView.STATE_READY);
            } else {
                mHeaderView.setState(XHeaderView.STATE_NORMAL);
            }
        }

        mPullRefreshing = true;
        mHeaderView.setState(XHeaderView.STATE_REFRESHING);
        refresh();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler = new RecycleHandler();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener listener = (OnXScrollListener) mScrollListener;
            listener.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());

        if (mEnablePullRefresh && !mPullRefreshing) {
            // update the arrow image unrefreshing
            if (mHeaderView.getVisibleHeight() > mHeaderHeight) {
                mHeaderView.setState(XHeaderView.STATE_READY);
            } else {
                mHeaderView.setState(XHeaderView.STATE_NORMAL);
            }
        }

        // scroll to top each time
        post(new Runnable() {
            @Override
            public void run() {
                XScrollView.this.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void resetHeaderHeight() {
        int height = mHeaderView.getVisibleHeight();
        if (height == 0) return;

        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderHeight) return;

        // default: scroll back to dismiss header.
        int finalHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderHeight) {
            finalHeight = mHeaderHeight;
        }

        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;

        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) {
                // height enough to invoke load  more.
                mFooterView.setState(XFooterView.STATE_READY);
            } else {
                mFooterView.setState(XFooterView.STATE_NORMAL);
            }
        }

        mFooterView.setBottomMargin(height);

        // scroll to bottom
        post(new Runnable() {
            @Override
            public void run() {
                XScrollView.this.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();

        if (bottomMargin > 0) {
            mScrollBack = SCROLL_BACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        if (!mPullLoading) {
            mPullLoading = true;
            mFooterView.setState(XFooterView.STATE_LOADING);
            loadMore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                if (isTop() && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();

                } else if (isBottom() && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);

                }
                break;

            default:
                // reset
                mLastY = -1;

                resetHeaderOrBottom();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    private void resetHeaderOrBottom() {
        if (isTop()) {
            // invoke refresh
            if (mEnablePullRefresh && mHeaderView.getVisibleHeight() > mHeaderHeight) {
                mPullRefreshing = true;
                mHeaderView.setState(XHeaderView.STATE_REFRESHING);
                refresh();
            }
            resetHeaderHeight();

        } else if (isBottom()) {
            // invoke load more.
            if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                startLoadMore();
            }
            resetFooterHeight();
        }
    }

    private boolean isTop() {
        return getScrollY() <= 0 || mHeaderView.getVisibleHeight() > mHeaderHeight || mContentLayout.getTop() > 0;
    }

    private boolean isBottom() {
        return Math.abs(getScrollY() + getHeight() - computeVerticalScrollRange()) <= 5 ||
                (getScrollY() > 0 && null != mFooterView && mFooterView.getBottomMargin() > 0);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLL_BACK_HEADER) {
                mHeaderView.setVisibleHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }

            postInvalidate();
            invokeOnScrolling();
        }

        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    /**
     * 设置监听
     */
    public void setOnPullToRefreshListener(OnPullToRefreshListener listener) {
        mListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // Grab the last child placed in the ScrollView, we need it to determinate the bottom position.
        View view = getChildAt(getChildCount() - 1);

        if (null != view) {
            // Calculate the scroll diff
            int diff = (view.getBottom() - (view.getHeight() + view.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0 && mEnableAutoLoad) {
                // notify that we have reached the bottom
                startLoadMore();
            }
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // send to user's listener
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void refresh() {
        if (mEnablePullRefresh && null != mListener) {
            mListener.onRefresh();
        }
    }

    private void loadMore() {
        if (mEnablePullLoad && null != mListener) {
            mListener.onLoadMore();
        }
    }


    private class RecycleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: msg.what == " + msg.what);
            switch (msg.what) {
                case HANDLER_RESET_HEADER_HEIGHT:
                    resetHeaderHeight();
                    break;
                case HANDLER_RESET_FOOTER_HEIGHT:
                    resetFooterHeight();
                    break;
            }
        }
    }

    /**
     * Header 的线是否显示
     */
    public void setHeaderDividersEnabled(boolean visibility) {
        mHeaderView.setLineVisibility(visibility ? VISIBLE : GONE);
    }

    /**
     * Footer的线是否显示
     */
    public void setFooterDividersEnabled(boolean visibility) {
        mFooterView.setLineVisibility(visibility ? VISIBLE : GONE);
    }
}
