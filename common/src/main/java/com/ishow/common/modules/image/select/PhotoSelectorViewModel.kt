package com.ishow.common.modules.image.select

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Photo
import com.ishow.common.utils.ToastUtils
import io.reactivex.Observable
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class PhotoSelectorViewModel : ViewModel() {

    private val _folderList = MutableLiveData<List<Folder>>().apply { value = emptyList() }
    val folderList: LiveData<List<Folder>>
        get() = _folderList

    private val _photoList = MutableLiveData<List<Photo>>().apply { value = emptyList() }
    val photoList: LiveData<List<Photo>>
        get() = _photoList


    fun init(context: Activity) {
        val photoModel = PhotoModel(context)
        photoModel.getPhotos { folders, photos ->
            _folderList.value = folders
            _photoList.value = photos
        }
    }

    fun onClickPhotoStatus(view: View) {
        Log.i("yhy", "onClickPhotoStatus")
        ToastUtils.show(view.context, "11111")
    }

    fun onClickPhoto() {

    }
}