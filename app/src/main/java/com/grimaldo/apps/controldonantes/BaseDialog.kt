package com.grimaldo.apps.controldonantes

import android.content.Context
import android.support.v4.app.DialogFragment
import com.grimaldo.apps.controldonantes.register.OnFinishUserEventListener

abstract class BaseDialog : DialogFragment() {
    protected var onFinishListener: OnFinishUserEventListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            onFinishListener = context as OnFinishUserEventListener
        } catch (ex: ClassCastException) {
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFinishListener = null
    }

    abstract fun getSource(): DialogFragment
}