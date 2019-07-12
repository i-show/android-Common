package com.ishow.common.modules.image.select

import com.ishow.common.entries.Photo

interface PhotoActionListener {
    fun onClickPhotoStatus(position:Int, photo: Photo)
}