package com.ishow.common.extensions

import com.google.gson.Gson


fun Any.toJson(): String? = Gson().toJson(this)
