package com.ishow.noah.modules.log

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by yuhaiyang on 2020/8/12.
 *  params["action"] = action
 */

@Keep
@Entity(tableName = "log")
data class Log(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val action: String,
    val remark: String? = null,
    val content: String? = null,
    val dateTime: String,
    val isPost: Boolean = false
)