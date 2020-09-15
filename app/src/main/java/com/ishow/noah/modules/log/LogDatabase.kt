package com.ishow.noah.modules.log

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ishow.noah.App

/**
 * Created by yuhaiyang on 2020/8/12.
 * 埋点的数据库
 */

@Database(entities = [Log::class], version = 1, exportSchema = false)
abstract class LogDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { buildDatabase() }

        private fun buildDatabase() = Room.databaseBuilder(App.app, LogDatabase::class.java, "ad_ko.db")
            .allowMainThreadQueries()
            .build()
    }

}