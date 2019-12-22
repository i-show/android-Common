/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ishow.common.entries

import android.net.Uri

/**
 * 文件夹
 */
class Folder(
    /**
     * ID
     */
    var id: String?,
    /**
     * 名称
     */
    var name: String?,
    /**
     * 封面
     */
    var cover: Image?
) {
    /**
     * 包含的图片
     */
    var photoList = mutableListOf<Image>()
    var count = 0
    @JvmField
    var isSelected = false
    val coverImage: Uri? = cover?.uri
    
    init {
        addPhoto(cover)
    }

    fun addPhoto(image: Image?) {
        image?.let { photoList.add(it) }
    }

    fun addAll(photos: MutableList<Image>?) {
        if (photos.isNullOrEmpty()) {
            return
        }

        if (photoList.isNotEmpty()) {
            photoList.clear()
        }
        cover = photos[0]
        photoList.addAll(photos)
    }


}