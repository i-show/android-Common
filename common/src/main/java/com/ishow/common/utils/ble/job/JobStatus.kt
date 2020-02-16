package com.ishow.common.utils.ble.job


enum class JobStatus(status: Int) {
    Run(0),
    Success(1),
    Failed(2)
}