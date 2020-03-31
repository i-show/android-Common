package com.ishow.common.utils.download.db

import androidx.room.*

@Dao
interface DownloadDao {

    @Query("select * from file_download")
    fun getData(): MutableList<DownloadData>?

    @Query("select * from file_download where url = :url")
    fun getData(url: String): MutableList<DownloadData>?

    @Query("select * from file_download where url = :url and id = :id")
    fun getData(id: Int, url: String): DownloadData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: DownloadData)

    @Update
    fun update(data: DownloadData)

    @Query("delete from file_download where url = :url")
    fun delete(url: String)
}