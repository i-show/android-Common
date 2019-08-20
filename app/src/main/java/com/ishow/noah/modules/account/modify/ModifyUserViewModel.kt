package com.ishow.noah.modules.account.modify

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.base.mvvm.AppBaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ModifyUserViewModel(application: Application) : AppBaseViewModel(application) {
    private val _avatarPath = MutableLiveData<String>()
    val avatarPath: LiveData<String>
        get() = _avatarPath


    override fun init() {
        _avatarPath.value = UserManager.instance.getAvatar(context)
    }

    fun uploadAvatar(path: String) = GlobalScope.launch {
        val accountModel = AccountModel()
        val result = request { accountModel.uploadAvatar(path) }
        result?.let { mainThread { _avatarPath.value = it } }
    }
}