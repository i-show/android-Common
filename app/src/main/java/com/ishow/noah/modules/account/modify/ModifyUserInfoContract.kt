package com.ishow.noah.modules.account.modify

import android.content.Context

import com.ishow.common.app.mvp.IPresenter
import com.ishow.common.app.mvp.IView
import com.ishow.noah.entries.User

/**
 * Created by yuhaiyang on 2018/8/8.
 * 修改用户信息-Contract
 */
internal interface ModifyUserInfoContract {

    interface View : IView {

        /**
         * 更新成功
         */
        fun updateAvatar(avatar: String)
    }

    interface Presenter : IPresenter {
        /**
         * 修改头像
         */
        fun modifyAvatar(context: Context, avatar: String)

        /**
         * 更新用户信息
         */
        fun modifyUserInfo(context: Context, user: User)
    }
}