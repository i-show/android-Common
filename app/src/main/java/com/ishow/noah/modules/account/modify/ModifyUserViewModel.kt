package com.ishow.noah.modules.account.modify

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.extensions.mainThread
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AppModel
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ModifyUserViewModel(application: Application) : AppBaseViewModel(application) {
    private val _avatarPath = MutableLiveData<String>()
    val avatarPath: LiveData<String>
        get() = _avatarPath


    override fun init() {
        _avatarPath.value = UserManager.instance.getAvatar()
    }

    fun uploadAvatar(path: String) = GlobalScope.launch {
        val accountModel = AppModel.instance
        val result = request { accountModel.uploadAvatar(path) }
        result?.let {
            UserManager.setAvatar(it)
            mainThread { _avatarPath.value = it }
        }
    }
}