package com.ishow.common.modules.image.select

import android.view.View
import android.widget.CheckBox
import com.ishow.common.entries.Photo

interface PhotoActionListener {
    /**
     * 点击图片勾选按钮
     */
    fun onClickPhotoStatus(view: CheckBox, photo: Photo)

    /**
     * 查看大图
     */
    fun viewPhoto(v: View, photo: Photo)
}