package com.ishow.noah.modules.log

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by yuhaiyang on 2020/8/12.
 * 埋点的Dao
 */

@Dao
interface LogDao {
    /**
     * 插入Log
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(log: Log)

    @Query("SELECT * FROM log ")
    fun getAllLogs(): List<Log>

    @Query("DELETE FROM log where id=:id")
    fun deleteLog(id: String)
}