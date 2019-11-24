package com.grimaldo.apps.controldonantes.register

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.grimaldo.apps.controldonantes.R
import com.grimaldo.apps.controldonantes.domain.User

@Suppress("UNCHECKED_CAST")
class DialogEditDonor : DialogRegisterDonor() {
    companion object {
        val TAG: String = DialogEditDonor::class.java.simpleName
        const val DONOR_ARG = "DONOR_ARG"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val donor = arguments?.getSerializable(DONOR_ARG)
        if (donor != null && donor is User) {
            val inputUsername = dialogView.findViewById<TextInputLayout>(R.id.inputUsername).editText!!
            val inputLastName = dialogView.findViewById<TextInputLayout>(R.id.inputLastName).editText!!
            val inputAge = dialogView.findViewById<TextInputLayout>(R.id.inputAge).editText!!
            val inputId = dialogView.findViewById<TextInputLayout>(R.id.inputId).editText!!
            val inputWeight = dialogView.findViewById<TextInputLayout>(R.id.inputWeight).editText!!
            val inputHeight = dialogView.findViewById<TextInputLayout>(R.id.inputHeight).editText!!
            val inputBloodDesc = dialogView.findViewById<Spinner>(R.id.inputBloodDesc)
            val inputBloodType = dialogView.findViewById<Spinner>(R.id.inputBloodType)

            inputId.setText(donor.id.toString())
            inputId.isEnabled = false
            inputUsername.setText(donor.username)
            inputLastName.setText(donor.lastName)
            inputAge.setText(donor.age.toString())
            inputWeight.setText(donor.weight.toString())
            inputHeight.setText(donor.height.toString())
            with(inputBloodType) {
                val position = (this.adapter as ArrayAdapter<String>).getPosition(donor.bloodType)
                setSelection(position)
            }
            with(inputBloodDesc) {
                val position = (this.adapter as ArrayAdapter<String>).getPosition(donor.bloodDesc)
                setSelection(position)
            }
        }
    }

    override fun getSource(): DialogFragment {
        return this
    }
}