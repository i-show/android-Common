package com.ishow.common.modules.image.select

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.app.mvvm.view.BindFragment
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.databinding.FragmentImagePreviewCommonBinding
import com.ishow.common.entries.Image
import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration

/**
 * Created by yuhaiyang on 2019-09-04.
 * 照片预览
 */
class ImagePreviewFragment : BindFragment<FragmentImagePreviewCommonBinding, BaseViewModel>() {

    private lateinit var adapter: BindAdapter<Image>
    private lateinit var previewAdapter: BindAdapter<Image>

    private var previewPosition: Int? = null

    override fun getLayout(): Int = R.layout.fragment_image_preview_common

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val position = it.getInt(Image.Key.POSITION, -1)
            if (position != -1) {
                previewPosition = position
                arguments = null
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as ImageSelectorActivity
        adapter = BindAdapter()
        adapter.addLayout(BR.item, R.layout.item_image_preview_big)
        binding.list.adapter = adapter

        previewAdapter = BindAdapter()
        previewAdapter.addLayout(BR.item, R.layout.item_image_preview_small)
        previewAdapter.addVariable(BR.vm, activity.viewModel)
        previewAdapter.addVariable(BR.fragment, this@ImagePreviewFragment)
        val itemDecoration = SpacingDecoration(10)
        binding.previewList.addItemDecoration(itemDecoration)
        binding.previewList.adapter = previewAdapter

        binding.vm = activity.viewModel
        binding.vm?.previewGlobal?.observe(activity, Observer { onPreviewGlobalStatusChanged() })
        binding.fragment = this

        initPreviewData()
        setCurrentItem(previewPosition, false)
        binding.list.registerOnPageChangeCallback(pagerCallBack)
    }


    /**
     * item_image_preview_small 点击过来的时间
     */
    fun setCurrentItem(position: Int?, notify: Boolean = true) {
        if (position == null) {
            return
        }
        if (!isAdded) {
            return
        }

        adapter.data?.let {
            binding.list.setCurrentItem(position, false)
            binding.vm?.setPreviewImage(it[position], position)
            if (notify) previewAdapter.notifyDataSetChanged()
        }
    }

    /**
     * fragment_image_preview_common 中调用
     */
    fun changSelectStatus(image: Image, view: CheckBox? = null) {
        binding.vm?.let {
            if (it.previewGlobal.value == true) {
                it.selectImage(image, view)
            } else {
                it.cancelSelectPhoto(image, view)
                previewAdapter.notifyDataSetChanged()
            }
        }
    }

    fun onBack() = activity?.onBackPressed()


    override fun onDestroyView() {
        super.onDestroyView()
        binding.list.unregisterOnPageChangeCallback(pagerCallBack)
    }

    private val pagerCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position >= adapter.itemCount) {
                return
            }
            val current = adapter.getItem(position)
            binding.vm?.setPreviewImage(current, position)
            previewAdapter.notifyDataSetChanged()
            binding.previewList.scrollToPosition(position)
        }
    }

    private fun initPreviewData() {
        binding.vm?.let {
            if (it.previewGlobal.value == true) {
                adapter.data = it.imageList.value
            } else {
                adapter.data = it.selectedImages.value
            }
        }
    }

    private fun onPreviewGlobalStatusChanged() {
        initPreviewData()

        if (isAdded) previewAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(position: Int): ImagePreviewFragment {
            val fragment = ImagePreviewFragment()
            if (position >= 0) {
                val bundle = Bundle()
                bundle.putInt(Image.Key.POSITION, position)
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}