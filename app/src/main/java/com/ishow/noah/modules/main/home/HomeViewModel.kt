package com.ishow.noah.modules.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.ishow.noah.entries.User
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeViewModel(app: Application) : AppBaseViewModel(app) {

    private val _test = MutableLiveData<String>()
    val test: LiveData<String>
        get() = _test

    private fun getUser(id: String): LiveData<User> {
        val user = User()
        user.id = id
        return MutableLiveData(user)
    }

    val userId: LiveData<String> = liveData {
        emit("2")
    }
    val user = Transformations.switchMap(userId) { id -> getUser(id) }

    private var time = 0L

    val test2 = liveData {
        emit(time)
    }


    fun changedTime() {
        time = System.currentTimeMillis()
    }
}