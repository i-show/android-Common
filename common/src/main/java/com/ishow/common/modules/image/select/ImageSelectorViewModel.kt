package com.ishow.common.modules.image.select

import android.app.Application
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.R
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Photo
import com.ishow.common.modules.image.show.ShowPhotoDialog
import com.ishow.common.utils.ToastUtils

class ImageSelectorViewModel(application: Application) : BaseViewModel(application) {

    private val _folderList = MutableLiveData<MutableList<Folder>>()
    val folderList: LiveData<MutableList<Folder>>
        get() = _folderList

    private val _currentFolder = MutableLiveData<Folder>()
    val currentFolder: LiveData<Folder>
        get() = _currentFolder

    private val _photoList = MutableLiveData<List<Photo>>()
    val photoList: LiveData<List<Photo>>
        get() = _photoList

    /**
     * 获取 选中照片
     */
    private val _selectedPhotos: MutableList<Photo> = java.util.ArrayList()
    val selectedPhotos: MutableList<Photo>
        get() = _selectedPhotos

    private val _topRightText = MutableLiveData<String>()
    val topRightText: LiveData<String>
        get() = _topRightText

    private val _previewText = MutableLiveData<String>()
    val previewText: LiveData<String>
        get() = _previewText

    private var mode: Int = Photo.Key.MODE_SINGLE
    private var maxCount: Int = 1

    fun init(mode: Int, maxCount: Int) {
        this.mode = mode
        this.maxCount = maxCount

        _folderList.value = ArrayList()
        _photoList.value = ArrayList()
        _topRightText.value = context.getString(R.string.complete)
        _previewText.value = context.getString(R.string.preview_image)

        val photoModel = ImageModel(context)
        photoModel.getPhotos { folders, photos ->
            _currentFolder.value = if (folders.isNotEmpty()) folders[0] else null
            _folderList.value = folders
            _photoList.value = photos
        }
    }

    private fun onSelectChanged(selectCount: Int) {
        @Suppress("CascadeIf")
        if (selectCount <= 0) {
            _topRightText.value = context.getString(R.string.complete)
            _previewText.value = context.getString(R.string.preview_image)
        } else if (mode == Photo.Key.MODE_SINGLE) {
            _topRightText.value = context.getString(R.string.complete)
            _previewText.value = context.getString(R.string.preview_image)
        } else {
            _topRightText.value = context.getString(R.string.link_complete, selectCount, maxCount)
            _previewText.value = context.getString(R.string.link_preview_image, selectCount)
        }
    }

    fun updateCurrentFolder(folder: Folder) {
        _currentFolder.value = folder
    }

    /**
     * 选择照片
     */
    fun selectPhoto(view: CheckBox, entry: Photo) {
        val alreadyCount = _selectedPhotos.size
        if (alreadyCount >= maxCount && !entry.isSelected) {
            val tip = context.getString(R.string.already_select_max, maxCount)
            ToastUtils.show(context, tip)
            return
        }

        entry.isSelected = !entry.isSelected
        if (entry.isSelected) {
            _selectedPhotos.add(entry)
        } else {
            _selectedPhotos.remove(entry)
        }
        view.isChecked = entry.isSelected

        onSelectChanged(_selectedPhotos.size)
    }

    fun viewPhoto(v: View, photo: Photo) {
        val dialog = ShowPhotoDialog(v.context)
        dialog.setData(photo.path)
        dialog.setBeforeView(v)
        dialog.show()
    }
}