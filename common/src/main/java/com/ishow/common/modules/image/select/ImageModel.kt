package com.ishow.common.modules.image.select

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.text.TextUtils
import com.ishow.common.R
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Image
import com.ishow.common.extensions.mainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ImageModel(private val context: Context) {

    private val photoList = ArrayList<Image>()
    private val folderList = ArrayList<Folder>()

    @SuppressLint("CheckResult")
    suspend fun getPhotos(listener: ((MutableList<Folder>, MutableList<Image>) -> Unit)?) = withContext(Dispatchers.IO) {

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION, null, null,
            MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        )

        cursor?.let {
            photoList.clear()
            folderList.clear()
            var position = 0
            while (cursor.moveToNext()) {
                val id = cursor.getLong(INDEX_ID)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                val name = cursor.getString(INDEX_NAME)
                val folderId = cursor.getString(INDEX_FOLDER_ID)
                val folderName = cursor.getString(INDEX_FOLDER_NAME)
                val size = cursor.getLong(INDEX_SIZE)
                val modifyDate = cursor.getLong(INDEX_MODIFY_DATE)

                if (size < MIN_PHOTO_SIZE || name.isNullOrEmpty() || folderName.isNullOrEmpty()) {
                    continue
                }
                val photo = Image(id, uri, name, modifyDate, folderName, position)
                photoList.add(photo)
                position++

                resolvePhotoFolder(photo, folderId, folderName)
            }
            addAllPhotoFolder(folderList, photoList)
            cursor.close()
        }

        mainThread { listener?.invoke(folderList, photoList) }
    }

    /**
     * 处理照片文件夹信息
     */
    private fun resolvePhotoFolder(photo: Image, folderId: String, folderName: String) {
        var contains = false

        for (folder in folderList) {
            if (TextUtils.equals(folderId, folder.id)) {
                folder.addPhoto(photo)
                contains = true
                break
            }
        }

        if (!contains) {
            val folder = Folder(folderId, folderName, photo)
            folderList.add(folder)
        }
    }

    /**
     * 增加所有照片选项
     */
    private fun addAllPhotoFolder(folderList: MutableList<Folder>, photoList: MutableList<Image>) {
        val folderName = context.getString(R.string.all_photos)
        val all = Folder("all", folderName, if (photoList.isEmpty()) null else photoList[0])
        all.addAll(photoList)
        all.isSelected = true
        folderList.add(0, all)
    }


    companion object {
        /**
         * 小于15K的照片不进行显示
         */
        private const val MIN_PHOTO_SIZE = 15 * 1024L

        /**
         * 搜索的列
         */
        private val IMAGE_PROJECTION = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        /**
         * ID
         */
        private const val INDEX_ID = 0

        /**
         * 名称的index
         */
        private const val INDEX_NAME = 1

        /**
         * 图片大小
         */
        private const val INDEX_SIZE = 2

        /**
         * 时间的index
         */
        private const val INDEX_MODIFY_DATE = 3

        /**
         * 文件夹 ID
         */
        private const val INDEX_FOLDER_ID = 4

        /**
         * 文件夹 名称
         */
        private const val INDEX_FOLDER_NAME = 5

    }
}