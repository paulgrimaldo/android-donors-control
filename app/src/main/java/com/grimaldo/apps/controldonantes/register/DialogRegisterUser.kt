package com.grimaldo.apps.controldonantes.register

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.view.View
import com.grimaldo.apps.controldonantes.BaseDialog
import com.grimaldo.apps.controldonantes.R
import com.grimaldo.apps.controldonantes.domain.User
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class DialogRegisterUser : BaseDialog() {
    private lateinit var dialogView: View

    companion object {
        val TAG: String = DialogRegisterUser::class.java.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView = View.inflate(context, R.layout.dialog_register_user, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton(R.string.accept, null)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                onFinishListener?.onFinishUserEvent(null)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            handleAcceptClick()
        }
        return dialog
    }

    private fun handleAcceptClick() {
        val inputUsername = dialogView.findViewById<TextInputLayout>(R.id.inputUsername).editText!!
        val inputPassword = dialogView.findViewById<TextInputLayout>(R.id.inputPassword).editText!!
        val inputConfirmPassword = dialogView.findViewById<TextInputLayout>(R.id.inputConfirmPassword).editText!!

        var valid = nonEmptyList(inputUsername, inputPassword, inputConfirmPassword) { view, msg ->
            (view.parent.parent as TextInputLayout).error = msg
        }
        valid = valid && inputConfirmPassword.text.toString().validator().textEqualTo(inputPassword.text.toString())
            .addErrorCallback {
                (inputConfirmPassword.parent.parent as TextInputLayout).error = context?.getString(R.string.password_confirmation_error)
            }.check()

        if (valid) {
            this.dismiss()
            val user = User()
            user.username = inputUsername.text.toString()
            user.password = inputPassword.text.toString()
            user.setAsNormalUser()
            val event = UserEvent(getSource(), user)
            onFinishListener?.onFinishUserEvent(event)
        }
    }

    override fun getSource(): DialogFragment {
        return this
    }

}