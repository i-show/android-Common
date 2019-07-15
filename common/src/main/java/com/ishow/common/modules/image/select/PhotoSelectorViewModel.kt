package com.ishow.common.modules.image.select

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishow.common.R
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Photo

class PhotoSelectorViewModel : ViewModel() {

    private val _folderList = MutableLiveData<MutableList<Folder>>()
    val folderList: LiveData<MutableList<Folder>>
        get() = _folderList

    private val _currentFolder = MutableLiveData<Folder>()
    val currentFolder: LiveData<Folder>
        get() = _currentFolder

    private val _photoList = MutableLiveData<List<Photo>>()
    val photoList: LiveData<List<Photo>>
        get() = _photoList

    private val _topRightEnable = MutableLiveData<Boolean>()
    val topRightEnable: LiveData<Boolean>
        get() = _topRightEnable

    private val _topRightText = MutableLiveData<String>()
    val topRightText: LiveData<String>
        get() = _topRightText

    private var mMode: Int = Photo.Key.MODE_SINGLE
    private var mMaxCount: Int = 1

    fun init(context: Activity, mode: Int, maxCount: Int) {
        mMode = mode
        mMaxCount = maxCount

        _folderList.value = ArrayList()
        _photoList.value = ArrayList()
        _topRightEnable.value = false
        _topRightText.value = context.getString(R.string.complete)

        val photoModel = PhotoModel(context)
        photoModel.getPhotos { folders, photos ->
            _currentFolder.value = if (folders.isNotEmpty()) folders[0] else null
            _folderList.value = folders
            _photoList.value = photos
        }
    }

    fun onPhotoSelectStatusChanged(context: Context, selectCount: Int) {
        @Suppress("CascadeIf")
        if (selectCount <= 0) {
            _topRightEnable.value = false
            _topRightText.value = context.getString(R.string.complete)
        } else if (mMode == Photo.Key.MODE_SINGLE) {
            _topRightEnable.value = true
            _topRightText.value = context.getString(R.string.complete)
        } else {
            _topRightEnable.value = true
            _topRightText.value = context.getString(R.string.link_complete, selectCount, mMaxCount)
        }
    }


    fun updateCurrentFolder(folder: Folder) {
        _currentFolder.value = folder
    }

    fun onClickPhoto() {

    }
}