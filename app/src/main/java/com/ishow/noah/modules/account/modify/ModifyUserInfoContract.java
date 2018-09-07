package com.ishow.noah.modules.account.modify;

import android.content.Context;

import com.ishow.common.mvp.base.IPresenter;
import com.ishow.common.mvp.base.IView;
import com.ishow.common.mvp.base.IViewStatus;
import com.ishow.noah.entries.User;

interface ModifyUserInfoContract{

    interface View extends IView, IViewStatus {

        /**
         * 更新成功
         */
        void updateAvatar(String avatar);
    }

    interface Presenter extends IPresenter {
        /**
         * 修改头像
         */
        void modifyAvatar(Context context, String avatar);

        /**
         * 更新用户信息
         */
        void modifyUserInfo(Context context, User user);
    }
}