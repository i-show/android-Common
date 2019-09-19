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
import com.ishow.common.app.fragment.BaseFragment
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
import kotlinx.android.synthetic.main.fragment_image_list_common.*
import java.util.ArrayList

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
        dataBinding.vm = activity.viewModel
        dataBinding.fragment = this

        topBar.setOnTopBarListener(this)
        adapter = BindAdapter(activity)
        adapter.addLayout(BR.photo, R.layout.item_image_selector_list)
        adapter.addVariable(BR.vm, activity.viewModel)

        list.addItemDecoration(SpacingDecoration(activity, R.dimen.photo_selector_item_gap))
        list.adapter = adapter
        list.addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.removeOnScrollListener(scrollListener)
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
        val photoList = dataBinding.vm?.selectedImages?.value
        if (photoList.isNullOrEmpty()) {
            toast(R.string.please_select_image)
            return
        }

        when (dataBinding.vm?.mode) {
            Image.Key.MODE_SINGLE -> setSingleResult(photoList)
            Image.Key.MODE_MULTI -> setMultiResult(photoList)
        }
    }


    fun onViewClick(v: View) {
        when (v.id) {
            R.id.folderView -> selectPhotoFolder()
            R.id.preview -> preview()
        }
    }

    private fun setSingleResult(photoList: MutableList<Image>) {
        val photo = photoList[0]
        activity?.let {
            val intent = Intent()
            intent.putExtra(Image.Key.EXTRA_RESULT, photo.getPath())
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    private fun setMultiResult(photoList: MutableList<Image>) {
        val photoPaths: ArrayList<String> = photoList.map { it.path } as ArrayList<String>
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
        val adapter = BindAdapter<Folder>(context!!)
        adapter.addLayout(BR.folder, R.layout.item_image_selector_folder)
        adapter.data = dataBinding.vm?.folderList?.value!!

        BaseDialog.Builder(context, R.style.Theme_Dialog_Bottom2)
            .fromBottom(true)
            .setWidthProportion(1F)
            .setAdapter(adapter) { _, which -> updatePhotos(adapter.getItem(which)) }
            .create()
            .show()
    }


    private fun preview() {
        val activity = activity as ImageSelectorActivity
        activity.showImagePreview()
    }

    private fun updatePhotos(folder: Folder) {
        val currentFolder = dataBinding.vm?.currentFolder?.value
        if (folder == currentFolder) {
            return
        }

        folder.isSelected = true
        currentFolder?.isSelected = false
        dataBinding.vm?.updateCurrentFolder(folder)

        adapter.data = folder.getPhotoList()
        folderView.setText(folder.getName())
        list.scrollToPosition(0)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                timeLine.clearAnimation()
                AnimatorUtils.alpha(timeLine, timeLine.alpha, 0f, 800)
            } else {
                timeLine.clearAnimation()
                AnimatorUtils.alpha(timeLine, timeLine.alpha, 1.0f, 800)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            if (timeLine.alpha >= 0.15f) {
                val image = adapter.getItem(layoutManager.findFirstVisibleItemPosition())
                timeLine.text = DateUtils.formatFriendly(context, image.modifyDate * 1000)
            }
        }
    }
}