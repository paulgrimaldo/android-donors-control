package com.grimaldo.apps.controldonantes.register

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.grimaldo.apps.controldonantes.BaseDialog
import com.grimaldo.apps.controldonantes.R
import com.grimaldo.apps.controldonantes.domain.User
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList

open class DialogRegisterDonor : BaseDialog() {
    lateinit var dialogView: View

    companion object {
        val TAG: String = DialogRegisterDonor::class.java.simpleName
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogView = View.inflate(context, R.layout.dialog_create_donor, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton(R.string.accept, null)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                onFinishListener?.onFinishUserEvent(null)
            }
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            handleAcceptClick(dialogView)
        }
        return dialog
    }

    private fun handleAcceptClick(dialogView: View) {
        val inputUsername = dialogView.findViewById<TextInputLayout>(R.id.inputUsername).editText!!
        val inputLastName = dialogView.findViewById<TextInputLayout>(R.id.inputLastName).editText!!
        val inputAge = dialogView.findViewById<TextInputLayout>(R.id.inputAge).editText!!
        val inputId = dialogView.findViewById<TextInputLayout>(R.id.inputId).editText!!
        val inputWeight = dialogView.findViewById<TextInputLayout>(R.id.inputWeight).editText!!
        val inputHeight = dialogView.findViewById<TextInputLayout>(R.id.inputHeight).editText!!
        val inputBloodDesc = dialogView.findViewById<Spinner>(R.id.inputBloodDesc)
        val inputBloodType = dialogView.findViewById<Spinner>(R.id.inputBloodType)
        var valid = nonEmptyList(inputUsername, inputLastName, inputAge, inputId, inputWeight, inputHeight) { view, msg ->
            (view.parent.parent as TextInputLayout).error = msg
        }
        valid = valid && nonEmptyList(inputBloodDesc, inputBloodType) { view, msg ->
            (view.selectedView as TextView).error = msg
        }
        if (valid) {
            dismiss()
            val user = User()
            val createUserEvent = user.run {
                this.id = inputId.text.toString().toInt()
                this.username = inputUsername.text.toString()
                this.lastName = inputLastName.text.toString()
                this.age = inputAge.text.toString().toInt()
                this.weight = inputWeight.text.toString().toFloat()
                this.height = inputHeight.text.toString().toFloat()
                this.bloodType = inputBloodType.selectedItem as String
                this.bloodDesc = inputBloodDesc.selectedItem as String
                setAsDonor()
                UserEvent(getSource(), this)
            }
            onFinishListener?.onFinishUserEvent(createUserEvent)
        }
    }

    override fun getSource(): DialogFragment {
        return this
    }
}