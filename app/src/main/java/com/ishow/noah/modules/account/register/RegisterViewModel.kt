package com.ishow.noah.modules.account.register

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.noah.R
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.params.request.RegisterParams
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AppModel
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class RegisterViewModel(application: Application) : AppBaseViewModel(application) {

    private val _verifyCodeStatus = MutableLiveData<Event<Boolean>>()
    val verifyCodeStatus: LiveData<Event<Boolean>>
        get() = _verifyCodeStatus

    @Suppress("UNUSED_PARAMETER")
    fun sendVerifyCode(phone: String) = viewModelScope.launch(Dispatchers.Main) {
        delay(2000)
        val result = Random().nextInt() % 2 == 0
        _verifyCodeStatus.value = Event(result)
        if (!result) toast("发送验证码失败")
    }

    fun register(phone: String, verifyCode: String, password: String, ensurePassword: String) {
        if (!TextUtils.equals(password, ensurePassword)) {
            toast(R.string.please_input_right_ensure_password)
            return
        }
        val params = RegisterParams()
        params.phone = phone
        params.verifyCode = verifyCode
        params.password = password

        viewModelScope.launch {
            val accountModel = AppModel.instance
            val result: AppHttpResponse<UserContainer> = requestResponse { accountModel.register(params) }
            if (result.isSuccess) {
                UserManager.instance.setUserContainer(result.data)
                showSuccess()
            } else {
                toast(result.message)
            }
        }

    }
}