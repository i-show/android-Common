package com.ishow.noah.modules.account.modify

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.ishow.common.utils.image.select.OnSelectImageListener
import com.ishow.common.utils.image.select.SelectImageUtils
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivityModifyUserInfoBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindActivity
import java.io.File

/**
 * Created by yuhaiyang on 2018/8/8.
 * 修改用户信息
 */
class ModifyUserActivity : AppBindActivity<ActivityModifyUserInfoBinding, ModifyUserViewModel>(),
    OnSelectImageListener {

    private lateinit var mSelectPhotoUtils: SelectImageUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_modify_user_info)

        mSelectPhotoUtils = SelectImageUtils(this, SelectImageUtils.SelectMode.SINGLE)
        mSelectPhotoUtils.setOnSelectPhotoListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSelectPhotoUtils.onActivityResult(requestCode, resultCode, data)
    }


    fun onViewClick(v: View?) {
        when (v?.id) {
            R.id.header -> selectPhoto()
        }
    }

    private fun selectPhoto() {
        mSelectPhotoUtils.setSelectMode(SelectImageUtils.SelectMode.SINGLE)
        mSelectPhotoUtils.select(1, 1, Bitmap.CompressFormat.JPEG)
    }

    override fun onSelectedPhoto(imageList: MutableList<File?>, image: File?) {
        image?.let {
            binding.vm?.uploadAvatar(it.absolutePath)
        }

    }
}