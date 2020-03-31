package com.ishow.common.utils.download.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DownloadData::class], version = 1)
abstract class DownloadDB : RoomDatabase() {

    abstract fun getDownloadDao(): DownloadDao

    companion object {

        private var instance: DownloadDB? = null

        @Synchronized
        fun get(context: Context): DownloadDB {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, DownloadDB::class.java, "download.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}