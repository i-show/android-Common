package com.ishow.noah.modules.account.modify

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.entries.status.Error
import com.ishow.common.modules.binding.Event
import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.constant.Url
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.base.mvvm.AppBaseViewModel
import com.ishow.noah.utils.http.AppHttpCallBack
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ModifyUserViewModel(application: Application) : AppBaseViewModel(application) {
    private val _avatarPath = MutableLiveData<String>()
    val avatarPath: LiveData<String>
        get() = _avatarPath


    fun init() {
        _avatarPath.value = UserManager.instance.getAvatar(context)
    }

    fun uploadAvatar(path: String) = GlobalScope.launch {
        val accountModel = AccountModel()
        val result = request { accountModel.uploadAvatar(path) }
        result?.let { mainThread { _avatarPath.value = it } }
    }
}