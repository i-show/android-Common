package com.ishow.noah.modules.main.mine

import android.content.Context

import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.UserManager

internal class MinePresenter(private val mView: MineContract.View) : MineContract.Presenter {

    override fun onResume(context: Context?) {
        val userManager = UserManager.instance
        val userContainer = userManager.getUserContainer(context)
        mView.update(userContainer)
    }
}