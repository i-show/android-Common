package com.ishow.noah.modules.main.home

import android.animation.ValueAnimator
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.ishow.common.extensions.isMainThread
import com.ishow.noah.entries.User
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.Dispatchers

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeViewModel(app: Application) : AppBaseViewModel(app) {

    private val _test = MutableLiveData<String>()
    val test: LiveData<String>
        get() = _test

    override fun init() {
        super.init()
    }

    private fun getUser(id: String): LiveData<User> {
        Log.i("yhy", "getUser------")
        val user = User()
        user.nickName = "Name"
        user.id = id
        return MutableLiveData(user)
    }

    val userId: LiveData<String> = liveData(Dispatchers.IO) {
        Log.i("yhy", "gen userId isMainThread = " + isMainThread())
        emit("2")
    }

    val user = Transformations.switchMap(userId) { id -> getUser(id) }

    val userName = Transformations.map(user) { it.nickName }

}