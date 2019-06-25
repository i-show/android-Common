package com.ishow.noah.modules.main.mine

import com.ishow.noah.manager.UserManager

internal class MinePresenter(private val mView: MineContract.View) : MineContract.Presenter {

    override fun onResume() {
        val userManager = UserManager.instance
        val userContainer = userManager.getUserContainer(mView.getContext())
        mView.update(userContainer)
    }
}