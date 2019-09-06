package com.ishow.common.modules.image.select

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import com.ishow.common.R
import com.ishow.common.app.mvvm.view.BindFragment
import com.ishow.common.databinding.FragmentImagePreviewCommonBinding
import androidx.viewpager2.widget.ViewPager2
import com.ishow.common.BR
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.entries.Image
import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration
import kotlinx.android.synthetic.main.fragment_image_preview_common.*


/**
 * Created by yuhaiyang on 2019-09-04.
 * 照片预览
 */
class ImagePreviewFragment : BindFragment<FragmentImagePreviewCommonBinding>() {

    private lateinit var adapter: BindAdapter<Image>
    private lateinit var previewAdapter: BindAdapter<Image>
    private var currentPosition = 0

    override fun getLayout(): Int = R.layout.fragment_image_preview_common

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as ImageSelectorActivity
        dataBinding.vm = activity.viewModel
        dataBinding.fragment = this

        adapter = BindAdapter(activity)
        adapter.addLayout(BR.item, R.layout.item_image_preview_big)
        list.adapter = adapter
        list.registerOnPageChangeCallback(pagerCallBack)

        previewAdapter = BindAdapter(activity)
        previewAdapter.addLayout(BR.item, R.layout.item_image_preview_small)
        previewAdapter.addVariable(BR.vm, activity.viewModel)
        previewAdapter.addVariable(BR.fragment, this@ImagePreviewFragment)
        val itemDecoration = SpacingDecoration(10)
        previewList.addItemDecoration(itemDecoration)
        previewList.adapter = previewAdapter
    }

    /**
     * item_image_preview_small 点击过来的时间
     */
    fun setCurrentItem(position: Int) {
        val selectedImageList = dataBinding.vm?.selectedImages?.value
        selectedImageList?.let {
            list.setCurrentItem(position, false)
            dataBinding.vm?.setPreviewImage(selectedImageList[position], position)
            previewAdapter.notifyDataSetChanged()
        }
    }

    /**
     * fragment_image_preview_common 中调用
     */
    fun setUnSelectPhoto(entry: Image, view: CheckBox? = null) {
        dataBinding.vm?.setUnSelectPhoto(entry, view)
        previewAdapter.notifyDataSetChanged()
    }

    fun onBack() {
        fragmentManager?.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding.vm?.removeUnSelectPhoto()
    }

    private val pagerCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position >= adapter.itemCount) {
                return
            }
            currentPosition = position
            val current = adapter.getItem(position)
            dataBinding.vm?.setPreviewImage(current, position)
            previewAdapter.notifyDataSetChanged()
            previewList.scrollToPosition(position)
        }
    }
}