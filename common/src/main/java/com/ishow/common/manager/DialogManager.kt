package com.ishow.common.manager

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

class DialogManager private constructor() {
    private val map = HashMap<LifecycleOwner, LinkedList<Dismissible>>()

    fun addDialog(owner: LifecycleOwner, dialog: Dismissible) {
        var dialogs = map[owner]
        if (dialogs == null) {
            dialogs = LinkedList()
            map[owner] = dialogs

            owner.lifecycle.addObserver(Ob())
        }
        dialogs.offer(dialog)


    }


    private fun showDialog(_dialog: Dismissible) {
        val dialog = _dialog as Dialog
        _dialog.addDismissListener(object : IDismissListener {
            override fun onDismiss() {
                dialog.ownerActivity
            }
        })

        dialog.show()

    }


    private fun showDialogFragment(_dialog: Dismissible) {
        val dialog = _dialog as DialogFragment
        val currentState = dialog.activity?.lifecycle?.currentState
        if (currentState != Lifecycle.State.CREATED) {
            return
        }

    }


    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DialogManager() }
    }


    inner class Ob() : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop(owner: LifecycleOwner) {

        }


        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(owner: LifecycleOwner) {
            val dialogQueue = map[owner]
            dialogQueue?.clear()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {

        }
    }

    interface Dismissible {
        fun addDismissListener(listener: IDismissListener)
    }

    interface IDismissListener {
        fun onDismiss()
    }


}