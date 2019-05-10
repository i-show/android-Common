package com.ishow.noah.modules.init.guide

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.noah.R

/**
 * Created by yuhaiyang on 2018/8/8.
 * Guide的Adapter
 */
internal class GuideAdapter( mContext: Context, private val mClickListener: View.OnClickListener) : PagerAdapter() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    companion object {
        private val GUIDE_IMAGES = intArrayOf(
                R.drawable.guide,
                R.drawable.guide_bg_2,
                R.drawable.guide_bg_3
        )
    }

    override fun getCount(): Int {
        return GUIDE_IMAGES.size
    }

    override fun isViewFromObject(view: View, original: Any): Boolean {
        return view === original
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = mLayoutInflater.inflate(R.layout.item_guide_item, container, false)
        item.setBackgroundResource(GUIDE_IMAGES[position])
        val comeIn = item.findViewById<View>(R.id.come_in)
        comeIn.setOnClickListener(mClickListener)
        comeIn.isEnabled = position == 2
        comeIn.visibility = if (position == 2) View.VISIBLE else View.INVISIBLE

        container.addView(item)
        return item
    }

    override fun destroyItem(container: ViewGroup, position: Int, child: Any) {
        val view = child as View
        view.setOnClickListener(null)
        container.removeView(view)
    }


}