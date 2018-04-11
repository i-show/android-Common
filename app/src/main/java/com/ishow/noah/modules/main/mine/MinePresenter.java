package com.ishow.noah.modules.main.mine;

import android.content.Context;

import com.ishow.noah.entries.UserContainer;
import com.ishow.noah.manager.UserManager;

class MinePresenter implements MineContract.Presenter {

    private MineContract.View mView;

    MinePresenter(MineContract.View view) {
        mView = view;
    }

    @Override
    public void onResume(Context context) {
        UserManager userManager = UserManager.getInstance();
        UserContainer userContainer = userManager.getUserContainer(context);
        mView.update(userContainer);
    }
}