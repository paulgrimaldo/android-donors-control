package com.grimaldo.apps.controldonantes.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.grimaldo.apps.controldonantes.MainActivity
import com.grimaldo.apps.controldonantes.R
import com.grimaldo.apps.controldonantes.dao.UserService
import com.grimaldo.apps.controldonantes.register.DialogRegisterUser
import com.grimaldo.apps.controldonantes.register.OnFinishUserEventListener
import com.grimaldo.apps.controldonantes.register.UserEvent
import com.grimaldo.apps.controldonantes.saveLoggedUser
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), OnFinishUserEventListener {
    private val userService = UserService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnNewUser.setOnClickListener {
            val dialogRegisterUser = DialogRegisterUser()
            dialogRegisterUser.show(supportFragmentManager, DialogRegisterUser.TAG)
        }
        btnLogin.setOnClickListener {
            makeLoginAttempt()
        }
    }

    override fun onFinishUserEvent(event: UserEvent?) {
        event?.let {
            userService.save(it.target)
            Toast.makeText(this, R.string.user_registered_successfully, Toast.LENGTH_LONG).show()
        }
    }

    private fun makeLoginAttempt() {
        val valid = nonEmptyList(inputUsername.editText!!, inputPassword.editText!!) { view, msg ->
            (view.parent.parent as TextInputLayout).error = msg
        }
        if (valid) {
            val username = inputUsername.editText!!.text.toString()
            val password = inputPassword.editText!!.text.toString()
            val user = userService.findByCredentials(username, password)
            user?.let {
                this@LoginActivity.saveLoggedUser(it)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } ?: run {
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_LONG).show()
            }
        }
    }
}
