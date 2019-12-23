package com.ishow.common.modules.image.select

import android.app.Application
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.R
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Image
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.databinding.bus.Event

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
    private val _imageList = MutableLiveData<MutableList<Image>>()
    val imageList: LiveData<MutableList<Image>>
        get() = _imageList

    /**
     * 图片列表
     */
    private val _imageListDataChanged = MutableLiveData<Event<Any>>()
    val imageListDataChanged: LiveData<Event<Any>>
        get() = _imageListDataChanged

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
    private val _previewImage = MutableLiveData<Image>()
    val previewCurrent: LiveData<Image>
        get() = _previewImage

    /**
     * 当前预览照片是否被选中
     */
    private val _previewImageStatus = MutableLiveData<Boolean>()
    val previewImageStatus: LiveData<Boolean>
        get() = _previewImageStatus

    /**
     * 当前预览的总数
     */
    private val _previewTotal = MutableLiveData<Int>()
    val previewTotal: LiveData<Int>
        get() = _previewTotal

    /**
     * 当前预览的是第几张
     */
    private val _previewPosition = MutableLiveData<Int>()
    val previewPosition: LiveData<Int>
        get() = _previewPosition

    /**
     * 是否是全局预览
     * 当点击单个图片的时候为全局预览
     */
    private val _previewGlobal = MutableLiveData<Boolean>()
    val previewGlobal: LiveData<Boolean>
        get() = _previewGlobal

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
        } else {
            _topRightText.value = context.getString(R.string.link_complete, selectCount, maxCount)
            _previewText.value = context.getString(R.string.link_preview_image, selectCount)
        }
        // 只有在非全局模式下才会动态改变
        if (_previewGlobal.value != true) {
            _previewTotal.value = selectCount
        }
    }

    fun updateCurrentFolder(folder: Folder) {
        _currentFolder.value = folder
        folder.photoList.forEachIndexed { index, image -> image.position = index }
        _imageList.value = folder.photoList
    }

    /**
     * 选择照片
     */
    @JvmOverloads
    fun selectImage(entry: Image, view: CheckBox? = null, mask: View? = null) {
        val photoList = _selectedImages.value!!
        val alreadyCount = photoList.size
        if (alreadyCount >= maxCount && !entry.selected) {
            val tip = context.getString(R.string.already_select_max, maxCount)
            ToastUtils.show(context, tip)
            return
        }

        entry.selected = !entry.selected
        if (entry.selected) {
            photoList.add(entry)
        } else {
            photoList.remove(entry)
        }
        view?.isChecked = entry.selected
        mask?.visibility = if (entry.selected) View.VISIBLE else View.INVISIBLE
        onSelectChanged(photoList.size)
        _selectedImages.value = photoList
    }

    /**
     * 取消选中状态
     */
    fun cancelSelectPhoto(entry: Image, view: CheckBox? = null) {
        entry.cancelSelected = !entry.cancelSelected
        view?.isChecked = entry.cancelSelected
        _previewImageStatus.value = entry.selected && !entry.cancelSelected
    }

    fun removeCancelSelectImage() {
        val list = _selectedImages.value!!
        for (i in list.size - 1 downTo 0) {
            val item = list[i]
            if (item.cancelSelected) {
                list.removeAt(i)
                item.selected = false
                item.cancelSelected = false
            }
        }
        onSelectChanged(list.size)
        _imageListDataChanged.value = Event(true)
    }


    fun changePreviewGlobalStatus(status: Boolean) {
        _previewGlobal.value = status

        if (status) {
            _previewTotal.value = _imageList.value?.size ?: 0
        } else {
            _previewTotal.value = _selectedImages.value?.size ?: 0
        }
    }

    /**
     * 设置当前预览图片
     */
    internal fun setPreviewImage(image: Image, position: Int) {
        _previewPosition.value = position
        _previewImage.value = image
        _previewImageStatus.value = image.selected && !image.cancelSelected
    }
}