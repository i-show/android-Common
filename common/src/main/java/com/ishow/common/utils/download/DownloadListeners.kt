package com.ishow.common.utils.download

/**
 * 进度监听
 */
typealias OnDownloadProgressBlock = (current: Long, total: Long) -> Unit

/**
 * 状态改变
 */
typealias OnDownloadStatusChangedBlock = (info: DownloadStatusInfo) -> Unit

