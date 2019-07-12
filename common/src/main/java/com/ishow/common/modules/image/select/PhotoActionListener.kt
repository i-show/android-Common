package com.ishow.common.modules.image.select

import android.widget.CheckBox
import com.ishow.common.entries.Photo

interface PhotoActionListener {
    fun onClickPhotoStatus(view: CheckBox, photo: Photo)
}