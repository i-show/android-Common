package com.ishow.common.widget.pulltorefresh

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R
import com.ishow.common.widget.pulltorefresh.footer.IPullToRefreshFooter
import com.ishow.common.widget.pulltorefresh.headers.IPullToRefreshHeader
import com.ishow.common.widget.pulltorefresh.utils.ViewHelper

/**
 * Created by Bright.Yu on 2017/3/20.
 * PullToRefresh
 */
class PullToRefreshView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ViewGroup(context, attrs, defStyle) {
    /**
     * HeaderView
     */
    private var mHeader: IPullToRefreshHeader? = null
    /**
     * TargetView
     */
    private var mTargetView: View? = null
    /**
     * FooterView
     */
    private var mFooter: IPullToRefreshFooter? = null

    /**
     * 上次的位置
     */
    private var mLastY: Float = 0.toFloat()
    private var mInitialDownY: Float = 0.toFloat()
    /**
     * 滑动距离的判断
     */
    private var mTouchSlop: Int = 0

    private var mIsBeingDragged: Boolean = false
    var isRefreshEnable: Boolean = false
    var isLoadMoreEnable: Boolean = false
    /**
     * 监听
     */
    private var pullToRefreshListener: OnPullToRefreshListener? = null
    private var pullToRefreshStatusChangedListener: OnPullToRefreshStatusChangedListener? = null

    private lateinit var mHandler: Handler

    /**
     * 在调用onLayout的时候使用
     */
    private var mHeaderMovingDistance: Int = 0
    private var mTargetOffsetTop: Int = 0
    private val mScrollViewId: Int
    /**
     * RecyclerView默认的Footer或者HeaderCount
     */
    var customFooterOrHeaderCount: Int = 0

    private var mRefreshingListener: AbsAnimatorListener? = null
    private var mRefreshingHeaderListener: AbsAnimatorListener? = null
    private var mSetRefreshNormalListener: AbsAnimatorListener? = null

    /**
     * 是否已经加载完毕
     */
    val isLoadEnd: Boolean
        get() = mFooter?.status == IPullToRefreshFooter.STATUS_END

    /**
     * 状态已经可以进行下拉刷新
     */
    private val isAlreadyStatus: Boolean
        get() = isEnabled && isHeaderAlreadyForRefresh && isFooterAlreadyForRefresh

    private val isHeaderAlreadyForRefresh: Boolean
        get() {
            if (!isRefreshEnable || mHeader == null) {
                return false
            }
            return when (mHeader?.status) {
                IPullToRefreshHeader.STATUS_NORMAL,
                IPullToRefreshHeader.STATUS_READY -> true
                else -> false
            }
        }

    private val isFooterAlreadyForRefresh: Boolean
        get() {
            if (!isLoadMoreEnable || mFooter == null) {
                return true
            }
            return when (mFooter?.status) {
                IPullToRefreshFooter.STATUS_NORMAL,
                IPullToRefreshFooter.STATUS_END,
                IPullToRefreshFooter.STATUS_FAILED -> true
                else -> false
            }
        }

    private// 当Header不存在或者使用的时候那么也可以认为可以进行加载更多
    val isHeaderAlreadyForLoadMore: Boolean
        get() {
            if (!isRefreshEnable || mHeader == null) {
                return true
            }
            return when (mHeader?.status) {
                IPullToRefreshHeader.STATUS_NORMAL,
                IPullToRefreshHeader.STATUS_READY -> true
                else -> false
            }
        }

    /**
     * 是否可以加载更多
     */
    private val isCanLoadMore: Boolean
        get() {
            if (!isLoadMoreEnable || mFooter == null) {
                return false
            }

            return when (mFooter?.status) {
                IPullToRefreshFooter.STATUS_NORMAL,
                IPullToRefreshFooter.STATUS_FAILED -> true
                else -> false
            }
        }


    private val recycleScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!isLoadMoreEnable || !isCanLoadMore || !isHeaderAlreadyForLoadMore) {
                return
            }

            val layoutManager = recyclerView.layoutManager
            if (layoutManager == null) {
                Log.i(TAG, "onScrollStateChanged:  layoutManager is null")
                return
            }
            // 如果item == 1 不进行任何操作 默认是有一个footer的
            if (layoutManager.itemCount <= 1 + customFooterOrHeaderCount) {
                return
            }

            if (layoutManager is GridLayoutManager) {
                val gridLayoutManager = layoutManager as GridLayoutManager?
                //获取最后一个可见view的位置
                val lastPosition = gridLayoutManager!!.findLastVisibleItemPosition()
                val itemCount = gridLayoutManager.itemCount
                val spanCount = gridLayoutManager.spanCount
                // 提前3个进行预加载
                if (lastPosition + spanCount * 3 >= itemCount - 1) {
                    // 通知状态改变要在setStatus之前
                    notifyLoadMoreStatusChanged(IPullToRefreshFooter.STATUS_LOADING)
                    mFooter!!.status = IPullToRefreshFooter.STATUS_LOADING
                    setLoading()
                }
            } else if (layoutManager is LinearLayoutManager) {
                val linearManager = layoutManager as LinearLayoutManager?
                //获取最后一个可见view的位置
                val lastPosition = linearManager!!.findLastVisibleItemPosition()
                val itemCount = linearManager.itemCount
                // 提前3个进行预加载
                if (lastPosition >= itemCount - 4) {
                    // 通知状态改变要在setStatus之前
                    notifyLoadMoreStatusChanged(IPullToRefreshFooter.STATUS_LOADING)
                    mFooter!!.status = IPullToRefreshFooter.STATUS_LOADING
                    setLoading()
                }
            }
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshView)
        isLoadMoreEnable = a.getBoolean(R.styleable.PullToRefreshView_loadMoreEnable, true)
        isRefreshEnable = a.getBoolean(R.styleable.PullToRefreshView_refreshEnable, true)
        mScrollViewId = a.getResourceId(R.styleable.PullToRefreshView_scrollViewId, View.NO_ID)
        a.recycle()
        init()
    }

    private fun init() {
        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mHandler = Handler()
        customFooterOrHeaderCount = 0

        mRefreshingListener = PullToRefreshAnimatorListener(AnimatorType.Refreshing)
        mRefreshingHeaderListener = PullToRefreshAnimatorListener(AnimatorType.HeaderRefreshing)
        mSetRefreshNormalListener = PullToRefreshAnimatorListener(AnimatorType.RefreshNormal)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val childCount = childCount
        check(childCount == 1) { "need only one a child" }
        mTargetView = getChildAt(0)
        var scrollView: View? = findViewById(mScrollViewId)
        if (scrollView == null) {
            scrollView = mTargetView
        }
        if (scrollView == null) {
            return
        }

        if (scrollView is RecyclerView) {
            val recyclerView = scrollView as RecyclerView?
            recyclerView!!.addOnScrollListener(recycleScrollListener)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight

        if (mHeader != null) {
            val view = mHeader!!.view
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
        }

        if (mTargetView != null) {
            val widthM = MeasureSpec.makeMeasureSpec(width - paddingLeft - paddingRight, MeasureSpec.EXACTLY)
            val heightM = MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, MeasureSpec.EXACTLY)
            mTargetView!!.measure(widthM, heightM)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = measuredWidth
        val height = measuredHeight
        if (mHeader != null) {
            val view = mHeader!!.view
            view.layout(0, -view.measuredHeight + mHeaderMovingDistance, view.measuredWidth, mHeaderMovingDistance)
        }

        if (mTargetView != null) {
            val child = mTargetView
            val childLeft = paddingLeft
            val childTop = paddingTop
            val childWidth = width - paddingLeft - paddingRight
            val childHeight = height - paddingTop - paddingBottom
            child!!.layout(
                childLeft,
                childTop + mTargetOffsetTop,
                childLeft + childWidth,
                childTop + childHeight + mTargetOffsetTop
            )
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 只有当Header和Footer 都ok的时候才能进行 下拉或者上拉
        if (!isAlreadyStatus || canScrollUp()) {
            return false
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsBeingDragged = false
                mInitialDownY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val y = ev.y
                startDragging(y)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mIsBeingDragged = false
        }

        return mIsBeingDragged
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isAlreadyStatus || canScrollUp()) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> mIsBeingDragged = false
            MotionEvent.ACTION_MOVE -> {
                val y = event.y.toInt()
                startDragging(y.toFloat())
                if (mIsBeingDragged) {
                    val offset = mLastY - y
                    mLastY = y.toFloat()
                    movingHeader(offset.toInt())
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                updateHeaderWhenUpOrCancel()
                mIsBeingDragged = false
            }
        }
        return true
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isLoadMoreEnable = enabled
        isRefreshEnable = enabled
    }


    fun setHeader(header: IPullToRefreshHeader) {
        if (mHeader != null) {
            removeView(mHeader!!.view)
        }

        mHeader = header
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(header.view, lp)
        requestLayout()
    }

    /**
     * Header 移动
     */
    @Synchronized
    private fun movingHeader(offset: Int) {
        if (mHeader == null) {
            return
        }

        val offsetResult = mHeader!!.moving(this, offset)
        mHeaderMovingDistance = mHeader!!.movingDistance

        ViewCompat.offsetTopAndBottom(mTargetView!!, offsetResult)
        mTargetOffsetTop = mTargetView!!.top

        if (mHeader!!.isEffectiveDistance) {
            notifyRefreshStatusChanged(IPullToRefreshHeader.STATUS_READY)
        } else {
            notifyRefreshStatusChanged(IPullToRefreshHeader.STATUS_NORMAL)
        }
    }

    @Synchronized
    private fun updateHeaderWhenUpOrCancel() {
        if (mHeader == null) {
            Log.i(TAG, "updateHeaderWhenUpOrCancel: header is null")
            return
        }

        if (mHeader!!.isEffectiveDistance) {
            notifyRefreshStatusChanged(IPullToRefreshHeader.STATUS_REFRESHING)
            val offset = mHeader!!.refreshing(this, mRefreshingHeaderListener)
            mHeaderMovingDistance = mHeader!!.movingDistance
            ViewHelper.movingY(mTargetView!!, offset, mRefreshingListener)
        } else {
            val offset = mHeader!!.cancelRefresh(this)
            ViewHelper.movingY(mTargetView!!, offset, mSetRefreshNormalListener)
        }
    }

    /**
     * 在这里Footer比较特殊不能直接Add到 这里面，需要在Target里面实现
     */
    fun setFooter(footer: IPullToRefreshFooter) {
        mFooter = footer
    }

    /**
     * 判断是否可以进行拖拽
     */
    private fun startDragging(y: Float) {
        val yDiff = y - mInitialDownY
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mLastY = y
            mIsBeingDragged = true
        }
    }


    /**
     * 是否可以上滑
     * 下拉刷新的时候使用
     */
    private fun canScrollUp(): Boolean {
        return mTargetView != null && mTargetView!!.canScrollVertically(-1)
    }

    /**
     * 设置成普通状态
     */
    fun setRefreshNormal() {
        if (mHeader == null) {
            return
        }
        mHandler.postDelayed({
            val offset = -mHeader!!.movingDistance
            ViewHelper.movingY(mTargetView!!, offset, mSetRefreshNormalListener)
        }, ANI_INTERVAL.toLong())
    }

    /**
     * 刷新成功
     */
    fun setRefreshSuccess() {
        if (mHeader == null || mHeader!!.status != IPullToRefreshHeader.STATUS_REFRESHING) {
            return
        }
        // 通知状态改变要在setStatus之前
        notifyRefreshStatusChanged(IPullToRefreshHeader.STATUS_SUCCESS)
        mHandler.postDelayed({
            val offset = mHeader!!.refreshSuccess(this@PullToRefreshView)
            ViewHelper.movingY(mTargetView!!, offset, mSetRefreshNormalListener)
        }, ANI_INTERVAL.toLong())
    }

    /**
     * 刷新失败
     */
    fun setRefreshFailed() {
        if (mHeader == null || mHeader!!.status != IPullToRefreshHeader.STATUS_REFRESHING) {
            Log.e(TAG, "setRefreshFailed: FooterStatus error ")
            return
        }
        // 通知状态改变要在setStatus之前
        notifyRefreshStatusChanged(IPullToRefreshHeader.STATUS_FAILED)
        mHandler.postDelayed({
            val offset = mHeader!!.refreshFailed(this@PullToRefreshView)
            ViewHelper.movingY(mTargetView!!, offset, mSetRefreshNormalListener)
        }, ANI_INTERVAL.toLong())
    }

    /**
     * 下拉加载失败
     */
    fun setLoadMoreFailed() {
        if (mFooter == null) {
            Log.i(TAG, "setLoadMoreFailed: mFooter is null")
            return
        }
        if (mFooter!!.status != IPullToRefreshFooter.STATUS_LOADING) {
            Log.i(TAG, "setLoadMoreFailed: FooterStatus error")
            return
        }
        // 通知状态改变要在setStatus之前
        notifyLoadMoreStatusChanged(IPullToRefreshFooter.STATUS_FAILED)
        requestLayout()
    }

    /**
     * 下拉加载成功
     */
    fun setLoadMoreSuccess() {
        if (mFooter == null) {
            Log.i(TAG, "setLoadMoreSuccess: mFooter is null")
            return
        }
        if (mFooter!!.status != IPullToRefreshFooter.STATUS_LOADING) {
            return
        }
        // 通知状态改变要在setStatus之前
        notifyLoadMoreStatusChanged(IPullToRefreshFooter.STATUS_NORMAL)
        requestLayout()
    }

    /**
     * 加载完成
     */
    fun setLoadMoreEnd() {
        if (mTargetView == null) {
            return
        }
        notifyLoadMoreStatusChanged(IPullToRefreshFooter.STATUS_END)
        requestLayout()
    }

    /**
     * 正常加载状态
     */
    fun setLoadMoreNormal() {
        if (mTargetView == null) {
            return
        }
        notifyLoadMoreStatusChanged(IPullToRefreshFooter.STATUS_NORMAL)
        requestLayout()
    }
    private fun setRefreshing() {
        computeStatus()
        notifyRefresh()
    }

    private fun setLoading() {
        computeStatus()
        notifyLoadMore()
    }

    private fun computeStatus() {
        if (mHeader != null) {
            mHeaderMovingDistance = mHeader!!.movingDistance
        }

        if (mTargetView != null) {
            mTargetOffsetTop = mTargetView!!.top
        }
    }

    fun setOnPullToRefreshListener(listener: OnPullToRefreshListener) {
        pullToRefreshListener = listener
    }

    private fun notifyRefresh() {
        pullToRefreshListener?.onRefresh(this)
    }

    private fun notifyLoadMore() {
        pullToRefreshListener?.onLoadMore(this)
    }

    /**
     * 设置状态改变
     */
    fun setOnPullToRefreshStatusChangedListener(listener: OnPullToRefreshStatusChangedListener) {
        pullToRefreshStatusChangedListener = listener
    }

    /**
     * 通知刷新的状态有改变
     */
    private fun notifyRefreshStatusChanged(status: Int) {
        if (mHeader == null || mHeader!!.status == status) {
            return
        }
        mHeader?.status = status
        pullToRefreshStatusChangedListener?.onRefreshStatusChanged(status)
    }

    /**
     * 通知加载更多的状态有改变
     */
    private fun notifyLoadMoreStatusChanged(status: Int) {
        if (mFooter == null || mFooter!!.status == status) {
            return
        }
        mFooter?.status = status
        pullToRefreshStatusChangedListener?.onLoadMoreStatusChanged(status)
    }

    private inner class PullToRefreshAnimatorListener constructor(private val type: AnimatorType) :
        AbsAnimatorListener() {

        override fun onAnimationEnd(animation: Animator) {
            when (type) {
                AnimatorType.RefreshNormal -> setRefreshNormal()
                AnimatorType.Refreshing -> setRefreshing()
                AnimatorType.HeaderRefreshing -> computeStatus()
            }
        }

        private fun setRefreshNormal() {
            // 通知状态改变要在setStatus之前
            notifyRefreshStatusChanged(IPullToRefreshHeader.STATUS_NORMAL)

            if (mHeader != null) {
                mHeaderMovingDistance = mHeader!!.movingDistance
            }

            if (mTargetView != null) {
                mTargetOffsetTop = mTargetView!!.top
            }
        }

        override fun onAnimationUpdate(animation: ValueAnimator) {
            computeStatus()
        }
    }

    enum class AnimatorType {
        RefreshNormal,
        Refreshing,
        HeaderRefreshing
    }

    companion object {
        private const val TAG = "PullToRefreshView"
        /**
         * 设置了时间的时候进行配置一个时间间隔
         */
        private const val ANI_INTERVAL = 800
    }
}
