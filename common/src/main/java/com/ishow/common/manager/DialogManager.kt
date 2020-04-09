package com.ishow.common.manager

import android.app.Dialog
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.ishow.common.extensions.delay
import java.util.*
import kotlin.collections.HashMap

class DialogManager private constructor() {
    private val dialogMap = HashMap<LifecycleOwner, LinkedList<Dismissible>>()

    private val currentDialogMap = HashMap<LifecycleOwner, Dismissible?>()

    fun addDialog(owner: FragmentActivity?, dialog: Dismissible) {
        if (owner == null) return

        var dialogs = dialogMap[owner]
        if (dialogs == null) {
            dialogs = LinkedList()
            dialogMap[owner] = dialogs

            owner.lifecycle.addObserver(Observer())
        }
        dialogs.offer(dialog)
        show(owner)
    }

    private fun show(owner: FragmentActivity) {
        val currentDialog = currentDialogMap[owner]
        if (currentDialog == null) {
            val dialogList = dialogMap[owner]
            val dialog = dialogList?.poll() ?: return

            if (checkOwnerStatus(owner)) {
                Log.i(TAG, "show: currentStatus = " + owner.lifecycle.currentState)
                return
            }

            currentDialogMap[owner] = dialog

            if (dialog is Dialog) {
                showDialog(owner, dialog)
            } else if (dialog is DialogFragment) {
                showDialogFragment(owner, dialog)
            }
        }
    }

    private fun showDialog(owner: FragmentActivity, _dialog: Dismissible) = delay(200) {
        _dialog.addDismissListener(object : IDismissListener {
            override fun onDismiss() {
                onDismissDialog(owner)
            }
        })

        val dialog = _dialog as Dialog
        dialog.show()
    }


    private fun showDialogFragment(owner: FragmentActivity, _dialog: Dismissible) = delay(200) {
        _dialog.addDismissListener(object : IDismissListener {
            override fun onDismiss() {
                onDismissDialog(owner)
            }
        })

        val dialog = _dialog as DialogFragment
        dialog.show(owner.supportFragmentManager, "dialog")
    }

    private fun onDismissDialog(owner: FragmentActivity) {
        currentDialogMap[owner] = null
        show(owner)
    }

    private fun checkOwnerStatus(owner: LifecycleOwner): Boolean {
        return owner.lifecycle.currentState != Lifecycle.State.RESUMED
    }

    companion object {
        private const val TAG = "DialogManager"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DialogManager() }
    }

    internal inner class Observer : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            val dialogQueue = dialogMap[owner]
            dialogQueue?.clear()
        }
    }

    interface Dismissible {
        fun addDismissListener(listener: IDismissListener)
    }

    interface IDismissListener {
        fun onDismiss()
    }

}