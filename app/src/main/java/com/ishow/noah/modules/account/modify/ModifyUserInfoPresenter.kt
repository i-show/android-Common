package com.ishow.noah.modules.account.modify

import android.content.Context

import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.constant.Url
import com.ishow.noah.entries.User
import com.ishow.noah.manager.UserManager
import com.ishow.noah.utils.http.AppHttpCallBack

import java.io.File

/**
 * Created by yuhaiyang on 2018/8/8.
 * 修改用户信息的Presenter
 */
internal class ModifyUserInfoPresenter(private val mView: ModifyUserInfoContract.View) : ModifyUserInfoContract.Presenter {

    override fun modifyAvatar(context: Context, avatar: String) {
        val userManager = UserManager.instance
        mView.showLoading(null, true)
        Http.post()
                .url(Url.uploadAvatar())
                .addHeader("token", userManager.getAccessToken(context))
                .addParams("file", File(avatar))
                .execute(object : AppHttpCallBack<String>(context) {
                    override fun onFailed(error: HttpError) {
                        mView.dismissLoading(true)
                        mView.showError(error.message, true, 0)
                    }

                    override fun onSuccess(result: String) {
                        mView.dismissLoading(true)
                        mView.updateAvatar(avatar)
                    }
                })
    }

    override fun modifyUserInfo(context: Context, user: User) {

    }
}