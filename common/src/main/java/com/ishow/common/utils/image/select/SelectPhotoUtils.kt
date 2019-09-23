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

package com.ishow.common.utils.image.select

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.ishow.common.R
import com.ishow.common.entries.Image
import com.ishow.common.modules.image.cutter.PhotoCutterActivity
import com.ishow.common.modules.image.select.ImageSelectorActivity
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.image.ImageUtils
import com.ishow.common.utils.log.LogUtils
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.common.utils.permission.PermissionManager.hasPermission
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.common.widget.loading.LoadingDialog
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 基类
 */
class SelectPhotoUtils(private val activity: Activity, @param:SelectMode private var mSelectMode: Int) :
    DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

    /**
     * 保存图片的地址的
     */
    private val photoList = Collections.synchronizedList(ArrayList<String>())

    /**
     * Camera拍照输出的地址
     */
    private var cameraFileUri: Uri? = null
    /**
     * 选择方式的Dialog
     */
    private var selectDialog: BaseDialog? = null

    private var loadingDialog: LoadingDialog? = null

    /**
     * 选择图片的Listener
     */
    private var selectPhotoListener: OnSelectPhotoListener? = null
    /**
     * 图片选择后是 压缩还是剪切
     */
    private var resultMode: Int = 0
    /**
     * 剪切模式-X轴比例
     */
    private var scaleX: Int = 0
    /**
     * 剪切模式-Y轴比例
     */
    private var scaleY: Int = 0
    /**
     * 可以选择的最大数量
     */
    private var maxSelectCount: Int = 0

    /**
     * 数据是否已经完成
     */
    private var isAlreadyOk: Boolean = false
    /**
     * 压缩图片的线程池
     */
    private var executorService: ExecutorService? = null
    /**
     * Format
     */
    private lateinit var compressFormat: Bitmap.CompressFormat

    var fragment: Fragment? = null

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (selectPhotoListener == null) {
                LoadingDialog.dismiss(loadingDialog)
                return
            }

            if (executorService?.isTerminated!! && !isAlreadyOk) {
                isAlreadyOk = true
                notifySelectPhoto(photoList, photoList[0])
            }
        }
    }

    /**
     * 设置选择模式
     */
    fun setSelectMode(@SelectMode selectMode: Int) {
        mSelectMode = selectMode
    }

    @JvmOverloads
    fun select(format: Bitmap.CompressFormat = Bitmap.CompressFormat.WEBP) {
        compressFormat = format

        if (!checkPermission()) {
            Log.i(TAG, "select: no permission")
            return
        }

        if (mSelectMode == SelectMode.SINGLE) {
            resultMode = ResultMode.COMPRESS
        } else {
            resultMode = ResultMode.COMPRESS
            maxSelectCount = MAX_SELECT_COUNT
        }

        showSelectDialog()
    }

    /**
     * 选择图片
     */
    @JvmOverloads
    fun select(@IntRange(from = 1) maxCount: Int, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG) {
        compressFormat = format

        if (!checkPermission()) {
            Log.i(TAG, "select: no permission")
            return
        }

        if (mSelectMode == SelectMode.SINGLE) {
            throw IllegalStateException("only multiple selection use")
        }
        maxSelectCount = maxCount
        resultMode = ResultMode.COMPRESS

        showSelectDialog()
    }

    /**
     * 选择图片模式为剪切模式
     *
     * @param scaleX 单选图片-X轴的比例
     * @param scaleY 单选图片-Y轴的比例
     */
    @JvmOverloads
    fun select(@IntRange(from = 1) scaleX: Int, @IntRange(from = 1) scaleY: Int, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG) {
        compressFormat = format

        if (!checkPermission()) {
            Log.i(TAG, "select: no permission")
            return
        }

        if (mSelectMode == SelectMode.MULTIPLE) {
            throw IllegalStateException("only single select SelectMode can set scaleX and scaleY")
        }
        resultMode = ResultMode.CROP
        this.scaleX = scaleX
        this.scaleY = scaleY

        showSelectDialog()
    }

    @JvmOverloads
    fun selectByCamera(file: File? = null) {
        mSelectMode = SelectMode.SINGLE
        selectPhotoByCamera(file)
    }

    /**
     * 显示选择框
     */
    private fun showSelectDialog() {
        if (selectDialog == null) {
            selectDialog = BaseDialog.Builder(activity, R.style.Theme_Dialog_Bottom)
                .setNegativeButton(R.string.cancel, null)
                .fromBottom(true)
                .setItems(R.array.select_photos) { dialog, which -> onClick(dialog, which) }
                .setOnDismissListener(this)
                .create()
        }

        if (!selectDialog!!.isShowing) {
            selectDialog?.show()
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            SELECT_PHOTO_CAMERA -> selectPhotoByCamera()
            SELECT_PHOTO_GALLERY -> selectPhotoByGallery()
        }
    }

    /**
     * 通过相机来选择图片
     */
    @Suppress("SpellCheckingInspection")
    private fun selectPhotoByCamera(file: File? = null) {
        var resultFile = file
        val authority = activity.packageName + ".fileprovider"
        if (resultFile == null) resultFile = ImageUtils.genImageFile(activity)
        cameraFileUri = Uri.fromFile(resultFile)
        val uri = FileProvider.getUriForFile(activity, authority, resultFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        when (mSelectMode) {
            SelectMode.SINGLE -> {
                if (fragment == null) {
                    activity.startActivityForResult(intent, Request.REQUEST_SINGLE_CAMERA)
                } else {
                    fragment?.startActivityForResult(intent, Request.REQUEST_SINGLE_CAMERA)
                }
            }
            SelectMode.MULTIPLE -> {
                if (fragment == null) {
                    activity.startActivityForResult(intent, Request.REQUEST_MULTI_CAMERA)
                } else {
                    fragment?.startActivityForResult(intent, Request.REQUEST_MULTI_CAMERA)
                }
            }
        }
    }

    /**
     * 通过相册来选择图片
     */
    private fun selectPhotoByGallery() {
        val intent = Intent(activity, ImageSelectorActivity::class.java)
        when (mSelectMode) {
            SelectMode.SINGLE -> {
                intent.putExtra(Image.Key.EXTRA_SELECT_MODE, Image.Key.MODE_SINGLE)
                if (fragment == null) {
                    activity.startActivityForResult(intent, Request.REQUEST_SINGLE_PICK)
                } else {
                    fragment?.startActivityForResult(intent, Request.REQUEST_SINGLE_PICK)
                }
            }
            SelectMode.MULTIPLE -> {
                intent.putExtra(Image.Key.EXTRA_SELECT_MODE, Image.Key.MODE_MULTI)
                intent.putExtra(Image.Key.EXTRA_SELECT_COUNT, maxSelectCount)
                if (fragment == null) {
                    activity.startActivityForResult(intent, Request.REQUEST_MULTI_PICK)
                } else {
                    fragment?.startActivityForResult(intent, Request.REQUEST_MULTI_PICK)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        selectDialog = null
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 用来接管activity的result
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            LoadingDialog.dismiss(loadingDialog)
            resolveResultCanceled(requestCode)
            return
        }

        when (requestCode) {
            // 不管多选还是单选 相机处理是一样的
            Request.REQUEST_SINGLE_CAMERA,
            Request.REQUEST_MULTI_CAMERA -> resolveSingleResult(cameraFileUri!!.path)
            Request.REQUEST_SINGLE_PICK -> {
                if (intentData == null) {
                    LoadingDialog.dismiss(loadingDialog)
                    return
                }
                val path = intentData.getStringExtra(Image.Key.EXTRA_RESULT)
                resolveSingleResult(path)
            }
            Request.REQUEST_MULTI_PICK -> {
                if (intentData == null) {
                    LoadingDialog.dismiss(loadingDialog)
                    return
                }
                val pathList = intentData.getStringArrayListExtra(Image.Key.EXTRA_RESULT)
                resolveMultiResult(pathList)
            }
            Request.REQUEST_CROP_IMAGE -> {
                if (intentData == null) {
                    LoadingDialog.dismiss(loadingDialog)
                    return
                }
                val picPath = intentData.getStringExtra(PhotoCutterActivity.KEY_RESULT_PATH)
                notifySelectPhoto(picPath)
            }
            else -> LogUtils.i(TAG, "requestCode = $requestCode")
        }
    }


    /**
     * onActivityResult 裁掉canceled的事件
     */
    private fun resolveResultCanceled(requestCode: Int) {
        when (requestCode) {
            Request.REQUEST_SINGLE_PICK,
            Request.REQUEST_SINGLE_CAMERA,
            Request.REQUEST_MULTI_PICK,
            Request.REQUEST_MULTI_CAMERA,
            Request.REQUEST_CROP_IMAGE -> ToastUtils.show(activity, R.string.cancel_photo)
        }
    }

    /**
     * onActivityResult 处理 单选
     */
    private fun resolveSingleResult(picPath: String?) {
        if (TextUtils.isEmpty(picPath)) {
            Log.i(TAG, "resolveSingleResult: picPath is empty")
            return
        }

        loadingDialog = LoadingDialog.show(activity, loadingDialog)

        val photos = ArrayList<String>()
        photos.add(picPath!!)

        photoList.clear()
        photoList.add("single")

        when (resultMode) {
            ResultMode.COMPRESS -> {
                loadingDialog = LoadingDialog.show(activity, loadingDialog)
                resolveResultPhotosForCompress(photos)
            }
            ResultMode.CROP -> gotoCrop(picPath)
        }
    }

    /**
     * onActivityResult 处理 单选
     */
    private fun resolveMultiResult(pathList: List<String>?) {
        if (pathList == null || pathList.isEmpty()) {
            Log.i(TAG, "resolveMultiResult: pathList is empty")
            return
        }
        // 把没有压缩前添加进入当占位符
        photoList.clear()
        photoList.addAll(pathList)

        loadingDialog = LoadingDialog.show(activity, loadingDialog)
        resolveResultPhotosForCompress(pathList)
    }

    /**
     * 处理返回的图片-通过压缩图片的方式
     */
    private fun resolveResultPhotosForCompress(photos: List<String>) {
        isAlreadyOk = false
        // 设置最多线程同时上传图片
        executorService = Executors.newFixedThreadPool(MAX_THREAD)
        for (i in photos.indices) {
            val photo = photos[i]
            executorService!!.execute(CompressRunnable(photo, i))
        }
        // 关闭线程池
        executorService!!.shutdown()
    }

    /**
     * 跳转剪切
     */
    private fun gotoCrop(path: String?) {
        val intent = Intent(activity, PhotoCutterActivity::class.java)
        intent.putExtra(PhotoCutterActivity.KEY_PATH, path)
        intent.putExtra(PhotoCutterActivity.KEY_RATIO_X, scaleX)
        intent.putExtra(PhotoCutterActivity.KEY_RATIO_Y, scaleY)
        intent.putExtra(PhotoCutterActivity.KEY_FORMAT, compressFormat)

        if (fragment == null) {
            activity.startActivityForResult(intent, Request.REQUEST_CROP_IMAGE)
        } else {
            fragment?.startActivityForResult(intent, Request.REQUEST_CROP_IMAGE)
        }
    }

    private inner class CompressRunnable internal constructor(
        internal var path: String,
        internal var key: Int
    ) : Runnable {
        override fun run() {
            val resultPath = ImageUtils.compressImage(activity, path, compressFormat)
            photoList[key] = resultPath
            mHandler.sendEmptyMessageDelayed(0, 100)
        }
    }


    /**
     * 提示已经选了多少图片
     */
    private fun notifySelectPhoto(singlePath: String) {
        val multiPath = ArrayList<String>()
        multiPath.add(singlePath)
        notifySelectPhoto(multiPath, singlePath)
    }

    /**
     * 提示已经选了多少图片
     */
    private fun notifySelectPhoto(multiPath: MutableList<String>, singlePath: String) {
        LoadingDialog.dismiss(loadingDialog)
        selectPhotoListener?.onSelectedPhoto(multiPath, singlePath)
    }

    /**
     * 设置选择图片的监听
     */
    fun setOnSelectPhotoListener(listener: OnSelectPhotoListener) {
        selectPhotoListener = listener
    }


    private fun checkPermission(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (!hasPermission(activity, *permissions)) {
            PermissionManager.with(activity)
                .permission(*permissions)
                .send()
            return false
        }
        return true
    }


    /**
     * 定义图片是单选还是多选
     */
    @IntDef(SelectMode.SINGLE, SelectMode.MULTIPLE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class SelectMode {
        companion object {
            /**
             * 单选
             */
            const val SINGLE = 1
            /**
             * 多选
             */
            const val MULTIPLE = 2
        }
    }


    object ResultMode {
        /**
         * 压缩
         */
        const val COMPRESS = 1
        /**
         * 剪切
         */
        const val CROP = 2
    }


    /**
     * 公共类方便调用 request
     */
    object Request {
        /**
         * 单选调用摄像头
         */
        internal const val REQUEST_SINGLE_CAMERA = 1 shl 8
        /**
         * 多选调用摄像头
         */
        internal const val REQUEST_MULTI_CAMERA = REQUEST_SINGLE_CAMERA + 1
        /**
         * 单选图片
         */
        internal const val REQUEST_SINGLE_PICK = REQUEST_SINGLE_CAMERA + 2
        /**
         * 多选图片
         */
        internal const val REQUEST_MULTI_PICK = REQUEST_SINGLE_CAMERA + 3
        /**
         * 剪切图片
         */
        const val REQUEST_CROP_IMAGE = REQUEST_SINGLE_CAMERA + 4

    }

    companion object {

        private const val TAG = "SelectPhotoUtils"

        /**
         * 多选模式下最多可以选多少张图片
         */
        private const val MAX_SELECT_COUNT = 9
        /**
         * 最多几个线程进行同时压缩
         */
        private const val MAX_THREAD = 3
        /**
         * 通过拍照 来选择
         */
        private const val SELECT_PHOTO_CAMERA = 0
        /**
         * 通过画廊来选择
         */
        private const val SELECT_PHOTO_GALLERY = 1
    }
}

