package com.ishow.common.modules.image.select

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.app.mvvm.view.BindFragment
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.databinding.FragmentImageListCommonBinding
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Image
import com.ishow.common.extensions.toast
import com.ishow.common.utils.AnimatorUtils
import com.ishow.common.utils.DateUtils
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration
import java.util.*

/**
 * Created by yuhaiyang on 2019-09-04.
 * 图片列表
 */

class ImageListFragment : BindFragment<FragmentImageListCommonBinding, BaseViewModel>() {
    private lateinit var adapter: BindAdapter<Image>

    override fun getLayout(): Int = R.layout.fragment_image_list_common

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as ImageSelectorActivity
        binding.vm = activity.viewModel
        binding.fragment = this

        binding.topBar.setOnTopBarListener(this)
        adapter = BindAdapter()
        adapter.addLayout(BR.photo, R.layout.item_image_selector_list)
        adapter.addVariable(BR.vm, activity.viewModel)
        adapter.addVariable(BR.fragment, this)

        binding.list.addItemDecoration(SpacingDecoration(activity, R.dimen.photo_selector_item_gap))
        binding.list.adapter = adapter
        binding.list.addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.list.removeOnScrollListener(scrollListener)
    }

    override fun onLeftClick(v: View) {
        super.onLeftClick(v)
        activity?.onBackPressed()
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        setResult()
    }

    private fun setResult() {
        val photoList = binding.vm?.selectedImages?.value
        if (photoList.isNullOrEmpty()) {
            toast(R.string.please_select_image)
            return
        }

        when (binding.vm?.mode) {
            Image.Key.MODE_SINGLE -> setSingleResult(photoList)
            Image.Key.MODE_MULTI -> setMultiResult(photoList)
        }
    }


    fun onViewClick(v: View) {
        when (v.id) {
            R.id.folderView -> selectPhotoFolder()
            R.id.preview -> previewSelected()
        }
    }

    private fun setSingleResult(photoList: MutableList<Image>) {
        val photo = photoList[0]
        activity?.let {
            val intent = Intent()
            intent.putExtra(Image.Key.EXTRA_RESULT, photo.uri.toString())
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    private fun setMultiResult(photoList: MutableList<Image>) {
        val photoPaths: ArrayList<String> = photoList.map { it.uri.toString() } as ArrayList<String>
        activity?.let {
            val intent = Intent()
            intent.putStringArrayListExtra(Image.Key.EXTRA_RESULT, photoPaths)
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    /**
     * 选择图片文件夹
     */
    private fun selectPhotoFolder() {
        val adapter = BindAdapter<Folder>()
        adapter.addLayout(BR.folder, R.layout.item_image_selector_folder)
        adapter.data = binding.vm?.folderList?.value

        BaseDialog.Builder(context, R.style.Theme_Dialog_Bottom2)
            .fromBottom(true)
            .setWidthProportion(1F)
            .setAdapter(adapter) { _, which -> updatePhotos(adapter.getItem(which)) }
            .create()
            .show()
    }

    /**
     * 预览选中图片
     */
    private fun previewSelected() {
        binding.vm?.changePreviewGlobalStatus(false)
        val activity = activity as ImageSelectorActivity
        activity.previewSelected()
    }

    /**
     * 预览大图
     * 点击单个图片
     */
    fun previewPhoto(position: Int) {
        binding.vm?.changePreviewGlobalStatus(true)
        val activity = activity as ImageSelectorActivity
        activity.preview(position)
    }

    private fun updatePhotos(folder: Folder) {
        val currentFolder = binding.vm?.currentFolder?.value
        if (folder == currentFolder) {
            return
        }

        folder.isSelected = true
        currentFolder?.isSelected = false
        binding.vm?.updateCurrentFolder(folder)

        binding.folderView.setText(folder.name)
        binding.list.scrollToPosition(0)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                binding.timeLine.clearAnimation()
                AnimatorUtils.alpha(binding.timeLine, binding.timeLine.alpha, 0f, 800)
            } else {
                binding.timeLine.clearAnimation()
                AnimatorUtils.alpha(binding.timeLine, binding.timeLine.alpha, 1.0f, 800)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            if (binding.timeLine.alpha >= 0.15f) {
                val image = adapter.getItem(layoutManager.findFirstVisibleItemPosition())
                binding.timeLine.text = DateUtils.formatFriendly(image.modifyDate * 1000)
            }
        }
    }
}