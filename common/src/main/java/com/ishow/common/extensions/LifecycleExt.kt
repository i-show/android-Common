package com.ishow.common.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 监听 Destroyed 的状态
 */
fun Lifecycle.observerDestroyed(destroyed: () -> Unit) {
    addObserver(LifecycleObserver(this, destroyed))
}

/**
 * Destroy 状态回调
 */
private class LifecycleObserver(var lifecycle: Lifecycle?, val destroyed: () -> Unit) : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val lifecycleState = source.lifecycle.currentState
        if (lifecycleState == Lifecycle.State.DESTROYED) {
            destroyed()
            lifecycle?.apply {
                removeObserver(this@LifecycleObserver)
                lifecycle = null
            }
        }
    }
}
