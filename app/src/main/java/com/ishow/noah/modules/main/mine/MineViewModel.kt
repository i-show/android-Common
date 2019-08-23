package com.ishow.noah.modules.main.mine

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.noah.R
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.base.mvvm.AppBaseViewModel

class MineViewModel(application: Application) : AppBaseViewModel(application) {

    private val _avatarUrl = MutableLiveData<String>()
    val avatarUrl: LiveData<String>
        get() = _avatarUrl

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    fun resume() {
        val userContainer = UserManager.instance.getUserContainer(context)

        if (userContainer == null) {
            _avatarUrl.value = null
            _userName.value = context.getString(R.string.click_login)
        } else {
            _avatarUrl.value = userContainer.user?.avatar
            _userName.value = userContainer.user?.nickName
        }

    }


}