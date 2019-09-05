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

package com.ishow.common.entries;

import com.ishow.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件夹
 */
public class Folder {
    /**
     * ID
     */
    public String id;
    /**
     * 名称
     */
    public String name;
    /**
     * 封面
     */
    public Image cover;
    /**
     * 包含的图片
     */
    public List<Image> photoList;

    public int count;
    public boolean isSelected;

    public Folder() {

    }

    public Folder(String id, String name, Image cover) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        addPhoto(cover);
    }

    public void addPhoto(Image image) {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }
        photoList.add(image);
    }

    public void addAll(List<Image> photos) {
        if (photos == null || photos.isEmpty()) {
            return;
        }
        if (photoList == null) {
            photoList = new ArrayList<>();
        } else {
            photoList.clear();
        }
        cover = photos.get(0);
        photoList.addAll(photos);
    }

    public List<Image> getPhotoList() {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }
        return photoList;
    }


    public String getName() {
        return name;
    }

    public String getCoverImage() {
        if (cover == null) {
            return StringUtils.EMPTY;
        } else {
            return cover.path;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Folder folder = (Folder) o;

        return Objects.equals(id, folder.id);

    }

}
