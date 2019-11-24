package com.grimaldo.apps.controldonantes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import com.grimaldo.apps.controldonantes.dao.UserService
import com.grimaldo.apps.controldonantes.register.UserEvent
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class DialogUpdatePassword : BaseDialog() {

    companion object {
        val TAG: String = DialogUpdatePassword::class.java.simpleName

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_passwprd, null)
        val dialog = AlertDialog.Builder(context)
            .setTitle(R.string.change_password)
            .setView(dialogView)
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.accept, null)
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setOnClickListener {
                handleAcceptClick(dialogView)
            }
        return dialog
    }

    private fun handleAcceptClick(dialogView: View) {
        val inputCurrentPassword = dialogView.findViewById<TextInputLayout>(R.id.inputCurrentPassword).editText!!
        val inputPassword = dialogView.findViewById<TextInputLayout>(R.id.inputPassword).editText!!
        val inputConfirmPassword = dialogView.findViewById<TextInputLayout>(R.id.inputConfirmPassword).editText!!
        var valid = nonEmptyList(inputCurrentPassword, inputPassword, inputConfirmPassword) { view, msg ->
            (view.parent.parent as TextInputLayout).error = msg
        }
        valid = valid && inputConfirmPassword.text.toString().validator()
            .textEqualTo(inputPassword.text.toString())
            .addErrorCallback { (inputConfirmPassword.parent.parent as TextInputLayout).error = context?.getString(R.string.password_confirmation_error) }
            .check()
        val userId = context?.getLoggedUserId()!!
        val user = UserService(context!!).findById(userId)
        if (inputCurrentPassword.text.toString() != user?.password) {
            (inputCurrentPassword.parent.parent as TextInputLayout).error = context?.getString(R.string.bad_current_password)
            valid = false
        }
        if (valid) {
            dismiss()
            val userEvent = user?.run {
                id = userId
                password = inputPassword.text.toString()
                UserEvent(getSource(), this)
            }
            onFinishListener?.onFinishUserEvent(userEvent)
        }
    }

    override fun getSource(): DialogFragment {
        return this
    }
}