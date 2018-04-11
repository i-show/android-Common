package com.ishow.noah.modules.account.modify;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.noah.constant.Url;
import com.ishow.noah.entries.User;
import com.ishow.noah.manager.UserManager;
import com.ishow.noah.utils.http.AppHttpCallBack;

import java.io.File;

class ModifyUserInfoPresenter implements ModifyUserInfoContract.Presenter {
   
    private ModifyUserInfoContract.View mView;

    ModifyUserInfoPresenter(ModifyUserInfoContract.View view) {
        mView = view;
    }

    @Override
    public void modifyAvatar(Context context, final String avatar) {
        UserManager userManager = UserManager.getInstance();
        mView.showLoading(null, true);
        Http.post()
                .url(Url.uploadAvatar())
                .addHeader("token", userManager.getAccessToken(context))
                .addParams("file", new File(avatar))
                .execute(new AppHttpCallBack<String>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        mView.dismissLoading(true);
                        mView.showError(error.getMessage(), true, 0);
                    }

                    @Override
                    protected void onSuccess(String result) {
                        mView.dismissLoading(true);
                        mView.updateAvatar(avatar);
                    }
                });
    }

    @Override
    public void modifyUserInfo(Context context, User user) {

    }
}