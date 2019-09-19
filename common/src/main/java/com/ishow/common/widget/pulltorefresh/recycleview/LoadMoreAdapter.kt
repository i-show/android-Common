package com.ishow.common.widget.pulltorefresh.recycleview

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ishow.common.R
import com.ishow.common.extensions.inflate
import com.ishow.common.widget.pulltorefresh.footer.IPullToRefreshFooter


class LoadMoreAdapter(
    private val innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IPullToRefreshFooter {
    /**
     * LoadMore de Views
     */
    private var loadMoreView: View? = null
    private var loadMoreTextView: TextView? = null
    private var loadMoreLoadingView: ImageView? = null
    /**
     * 旋转动画
     */
    private val rotateLoading: RotateAnimation
    /**
     * 当前的Load More状态
     */
    private var loadMoreStatus: Int = 0

    private var isEnabled: Boolean = false

    override var status: Int
        get() = loadMoreStatus
        set(@IPullToRefreshFooter.FooterStatus status) {
            loadMoreStatus = status
            setFooterStatus(status)
        }

    /**
     * 注册监听
     */
    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            notifyItemMoved(fromPosition, toPosition)
        }

    }

    init {
        isEnabled = true
        innerAdapter.registerAdapterDataObserver(dataObserver)

        rotateLoading = RotateAnimation(0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateLoading.duration = (ROTATE_ANIM_DURATION * 2).toLong()
        rotateLoading.repeatCount = Animation.INFINITE
        rotateLoading.fillAfter = false
    }


    override fun getItemCount(): Int {
        return if (isEnabled) {
            innerAdapter.itemCount + 1
        } else {
            innerAdapter.itemCount
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!isEnabled) {
            return innerAdapter.getItemViewType(position)
        }

        val realCount = innerAdapter.itemCount
        return if (position >= realCount) {
            TYPE_LOAD_MORE
        } else {
            innerAdapter.getItemViewType(position)
        }
    }

    /**
     * 是否是 LoadMore状态
     */
    private fun isLoadMoreItem(position: Int): Boolean {
        val realCount = innerAdapter.itemCount
        return position >= realCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LOAD_MORE) {
            loadMoreView = parent.inflate(R.layout.layout_pull2refresh_footer)
            loadMoreTextView = loadMoreView!!.findViewById(R.id.pull_to_refresh_footer_text)
            loadMoreLoadingView = loadMoreView!!.findViewById(R.id.pull_to_refresh_footer_loading)
            ViewHolder(loadMoreView!!)
        } else {
            innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = getItemViewType(position)
        if (itemType == TYPE_LOAD_MORE) {
            setFooterStatus(loadMoreStatus)
        } else {
            innerAdapter.onBindViewHolder(holder, position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager

        if (layoutManager !is GridLayoutManager) {
            // 如果不是GridLayoutManager 那么不进行任何操作
            return
        }

        val gridLayoutManager = layoutManager as GridLayoutManager?
        val spanSizeLookup = gridLayoutManager!!.spanSizeLookup
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (isLoadMoreItem(position)) {
                    gridLayoutManager.spanCount
                } else {
                    spanSizeLookup?.getSpanSize(position) ?: 1
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        innerAdapter.unregisterAdapterDataObserver(dataObserver)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)
        if (isLoadMoreItem(holder.layoutPosition)) {
            setFullSpan(holder)
            setFooterStatus(loadMoreStatus)
        }
    }

    private fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true
        }
    }

    override fun init(parent: ViewGroup) {
        loadMoreStatus = IPullToRefreshFooter.STATUS_NORMAL
    }

    fun setEnabled(enable: Boolean) {
        isEnabled = enable
        loadMoreView?.visibility = if (enable) View.VISIBLE else View.GONE
    }

    private fun setFooterStatus(status: Int) {
        if (loadMoreView == null) {
            return
        }
        when (status) {
            IPullToRefreshFooter.STATUS_NORMAL -> {
                loadMoreLoadingView?.clearAnimation()
                loadMoreLoadingView?.visibility = View.GONE
                loadMoreTextView?.setText(R.string.pull2refresh_footer_normal)
            }
            IPullToRefreshFooter.STATUS_LOADING -> {
                loadMoreLoadingView?.clearAnimation()
                loadMoreLoadingView?.startAnimation(rotateLoading)
                loadMoreLoadingView?.visibility = View.VISIBLE
                loadMoreTextView?.setText(R.string.pull2refresh_footer_loading)
            }
            IPullToRefreshFooter.STATUS_FAILED -> {
                loadMoreLoadingView?.clearAnimation()
                loadMoreLoadingView?.visibility = View.GONE
                loadMoreTextView?.setText(R.string.pull2refresh_footer_fail)
            }
            IPullToRefreshFooter.STATUS_END -> {
                loadMoreLoadingView?.clearAnimation()
                loadMoreLoadingView?.visibility = View.GONE
                loadMoreTextView?.setText(R.string.pull2refresh_footer_end)
            }
        }
    }

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        /**
         * 是否是加载更多的Item
         */
        private const val TYPE_LOAD_MORE = Integer.MAX_VALUE - 2
        /**
         * 加载动画的时间
         */
        private const val ROTATE_ANIM_DURATION = 380
    }
}
