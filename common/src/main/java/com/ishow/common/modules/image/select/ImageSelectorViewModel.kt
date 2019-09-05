package com.ishow.common.modules.image.select

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.R
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Image
import com.ishow.common.modules.image.show.ShowPhotoDialog
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.ToastUtils

class ImageSelectorViewModel(application: Application) : BaseViewModel(application) {
    /**
     * 文件夹列表
     */
    private val _folderList = MutableLiveData<MutableList<Folder>>()
    val folderList: LiveData<MutableList<Folder>>
        get() = _folderList

    /**
     * 当前 文件夹
     */
    private val _currentFolder = MutableLiveData<Folder>()
    val currentFolder: LiveData<Folder>
        get() = _currentFolder
    /**
     * 图片列表
     */
    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>>
        get() = _imageList

    /**
     * 获取 选中照片
     */
    private val _selectedImages = MutableLiveData<MutableList<Image>>()
    val selectedImages: LiveData<MutableList<Image>>
        get() = _selectedImages
    /**
     * 列表页TopBar右侧计数信息
     */
    private val _topRightText = MutableLiveData<String>()
    val topRightText: LiveData<String>
        get() = _topRightText

    /**
     * 列表页-预览计数信息
     */
    private val _previewText = MutableLiveData<String>()
    val previewText: LiveData<String>
        get() = _previewText

    /**
     * 当前预览的照片
     */
    private val _previewCurrent = MutableLiveData<Image>()
    val previewCurrent: LiveData<Image>
        get() = _previewCurrent

    private val _currentPreviewImageStatus = MutableLiveData<Boolean>()
    val currentPreviewImageStatus: LiveData<Boolean>
        get() = _currentPreviewImageStatus


    /**
     * 预览页-左上角Text
     */
    private val _previewTopText = MutableLiveData<String>()
    val previewTopText: LiveData<String>
        get() = _previewTopText

    internal var mode: Int = Image.Key.MODE_SINGLE
    private var maxCount: Int = 1

    fun init(mode: Int, maxCount: Int) {
        this.mode = mode
        this.maxCount = maxCount

        _folderList.value = ArrayList()
        _imageList.value = ArrayList()
        _selectedImages.value = ArrayList()

        _topRightText.value = context.getString(R.string.complete)
        _previewText.value = context.getString(R.string.preview_image)

        val photoModel = ImageModel(context)
        photoModel.getPhotos { folders, photos ->
            _currentFolder.value = if (folders.isNotEmpty()) folders[0] else null
            _folderList.value = folders
            _imageList.value = photos
        }
    }

    /**
     *  选中图片个数出现变化
     */
    private fun onSelectChanged(selectCount: Int) {
        @Suppress("CascadeIf")
        if (selectCount <= 0) {
            _topRightText.value = context.getString(R.string.complete)
            _previewText.value = context.getString(R.string.preview_image)
        } else if (mode == Image.Key.MODE_SINGLE) {
            _topRightText.value = context.getString(R.string.complete)
            _previewText.value = context.getString(R.string.preview_image)
            _previewTopText.value = "1/1"
        } else {
            _topRightText.value = context.getString(R.string.link_complete, selectCount, maxCount)
            _previewText.value = context.getString(R.string.link_preview_image, selectCount)
            _previewTopText.value = "1/$selectCount"
        }
    }

    fun updateCurrentFolder(folder: Folder) {
        _currentFolder.value = folder
    }

    /**
     * 选择照片
     */
    @JvmOverloads
    fun selectPhoto(entry: Image, view: CheckBox? = null) {
        val photoList = _selectedImages.value!!
        val alreadyCount = photoList.size
        if (alreadyCount >= maxCount && !entry.isSelected) {
            val tip = context.getString(R.string.already_select_max, maxCount)
            ToastUtils.show(context, tip)
            return
        }

        entry.isSelected = !entry.isSelected
        if (entry.isSelected) {
            photoList.add(entry)
        } else {
            photoList.remove(entry)
        }
        view?.isChecked = entry.isSelected

        onSelectChanged(photoList.size)
        _selectedImages.value = photoList
    }


    fun setUnSelectPhoto(entry: Image, view: CheckBox? = null) {
        entry.isUnSelected = !entry.isUnSelected
        view?.isChecked = entry.isUnSelected
        _currentPreviewImageStatus.value = entry.isSelected && !entry.isUnSelected
    }

    fun viewPhoto(v: View, photo: Image) {
        val dialog = ShowPhotoDialog(v.context)
        dialog.setData(photo.path)
        dialog.setBeforeView(v)
        dialog.show()
    }

    internal fun setPreviewCurrent(image: Image) {
        _previewCurrent.value = image
        _currentPreviewImageStatus.value = image.isSelected && !image.isUnSelected
    }
}