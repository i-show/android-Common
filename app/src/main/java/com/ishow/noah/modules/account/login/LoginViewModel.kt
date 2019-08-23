package com.ishow.noah.modules.account.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.extensions.getInteger
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.StringUtils
import com.ishow.noah.R
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.base.mvvm.AppBaseViewModel
import com.ishow.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginViewModel(application: Application) : AppBaseViewModel(application) {
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    private val _passwordHint = MutableLiveData<String>()
    val passwordHint: LiveData<String>
        get() = _passwordHint

    private lateinit var mAccountModel: AccountModel

    override fun init() {
        mAccountModel = AccountModel()

        val account = StorageUtils.with(context)
            .key(UserContainer.Key.ACCOUNT)
            .get(StringUtils.EMPTY)
        _phoneNumber.value = account

        val min = context.getInteger(R.integer.min_password)
        val max = context.getInteger(R.integer.max_password)
        _passwordHint.value = context.getString(R.string.login_hint_password, min, max)

        clear()
    }

    /**
     * 登录
     */
    fun login(phone: String, password: String) = GlobalScope.launch {
        val result: AppHttpResponse<UserContainer> = withLoading { mAccountModel.login(phone, password) }
        if (result.isSuccess()) {
            saveUserInfo(phone)
            UserManager.instance.setUserContainer(context, result.data)
            showSuccess()
        } else {
            toast(result.message)
        }
    }

    /**
     * 清除用户缓存
     */
    private fun clear() {
        AppHttpInterceptor.token = null

        StorageUtils.with(context)
            .key(UserContainer.Key.CACHE)
            .remove()
    }


    /**
     * 保存用户信息
     */
    private fun saveUserInfo(account: String) {
        StorageUtils.with(context)
            .param(UserContainer.Key.ACCOUNT, account)
            .save()
    }
}