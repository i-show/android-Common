package com.ishow.noah.modules.account.modify

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.ishow.common.utils.image.select.OnSelectPhotoListener
import com.ishow.common.utils.image.select.SelectPhotoUtils
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivityModifyUserInfoBinding
import com.ishow.noah.modules.base.mvvm.AppBindActivity

/**
 * Created by yuhaiyang on 2018/8/8.
 * 修改用户信息
 */
class ModifyUserActivity : AppBindActivity<ActivityModifyUserInfoBinding>(), OnSelectPhotoListener {

    private lateinit var mSelectPhotoUtils: SelectPhotoUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_modify_user_info)
        getViewModel(ModifyUserViewModel::class.java).also {
            dataBinding.vm = it
            it.init()
        }

        mSelectPhotoUtils = SelectPhotoUtils(this, SelectPhotoUtils.SelectMode.SINGLE)
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
        mSelectPhotoUtils.setSelectMode(SelectPhotoUtils.SelectMode.SINGLE)
        mSelectPhotoUtils.select(1, 1, Bitmap.CompressFormat.JPEG)
    }

    override fun onSelectedPhoto(multiPath: List<String>, singlePath: String) {
        dataBinding.vm?.uploadAvatar(singlePath)
    }
}