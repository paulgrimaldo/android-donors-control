package com.grimaldo.apps.controldonantes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.grimaldo.apps.controldonantes.adapters.DonorsListAdapter
import com.grimaldo.apps.controldonantes.dao.UserService
import com.grimaldo.apps.controldonantes.domain.User
import com.grimaldo.apps.controldonantes.login.LoginActivity
import com.grimaldo.apps.controldonantes.register.DialogEditDonor
import com.grimaldo.apps.controldonantes.register.DialogRegisterDonor
import com.grimaldo.apps.controldonantes.register.OnFinishUserEventListener
import com.grimaldo.apps.controldonantes.register.UserEvent
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.donor_content.*
import kotlinx.android.synthetic.main.donor_search_bar.*

class MainActivity : AppCompatActivity(), OnFinishUserEventListener, DonorsListAdapter.OnClickDonorListener {
    private val usersService = UserService(this)
    private val dialogRegisterDonor = DialogRegisterDonor()
    private val dialogEditDonor = DialogEditDonor()
    private val dialogUpdatePassword = DialogUpdatePassword()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        donorsList.setHasFixedSize(true)
        val logged = this.getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(AppConstants.PREFERENCES_LOGGED_PROPERTY, false)
        if (!logged) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        fabAddDonor.setOnClickListener {
            dialogRegisterDonor.show(supportFragmentManager, DialogRegisterDonor.TAG)
        }
        btnSearch.setOnClickListener {
            findById()
        }
        btnCancelSearch.setOnClickListener {
            inputDonorId.editText!!.setText("")
        }
        listDonors()
    }

    override fun onFinishUserEvent(event: UserEvent?) {
        event?.run {
            when (event.source) {
                dialogRegisterDonor -> {
                    try {
                        usersService.save(this.target)
                        Toast.makeText(this@MainActivity, R.string.new_donor_registered_successfully, Toast.LENGTH_LONG).show()
                        listDonors()
                    } catch (e: UserAlreadyExistsException) {
                        Toast.makeText(this@MainActivity, R.string.donor_already_exists, Toast.LENGTH_LONG).show()
                    }
                }
                dialogEditDonor -> {
                    usersService.update(this.target)
                    Toast.makeText(this@MainActivity, R.string.donor_updated_successfully, Toast.LENGTH_LONG).show()
                    listDonors()
                }
                dialogUpdatePassword -> {
                    usersService.updatePassword(this.target)
                    Toast.makeText(this@MainActivity, R.string.user_password_changed_successfully, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_close_session -> {
                logoutUser()
                refreshActivity()
            }
            R.id.menu_item_delete_account -> deleteAccount()
            R.id.menu_item_change_password -> dialogUpdatePassword.show(supportFragmentManager, DialogUpdatePassword.TAG)
        }
        return true
    }

    override fun onClickEditDonor(donor: User) {
        val args = Bundle()
        args.putSerializable(DialogEditDonor.DONOR_ARG, donor)
        dialogEditDonor.arguments = args
        dialogEditDonor.show(supportFragmentManager, DialogEditDonor.TAG)
    }

    override fun onClickDeleteDonor(donor: User) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.confirmation)
            .setMessage(R.string.delete_donor_confirmation_message)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.accept) { _, _ ->
                usersService.delete(donor)
                Toast.makeText(this, R.string.donor_deleted_successfully, Toast.LENGTH_LONG).show()
                listDonors()
            }.create()
        alertDialog.show()
    }

    private fun findById() {
        val valid = inputDonorId.editText!!.text.toString().validator()
            .nonEmpty()
            .onlyNumbers()
            .addErrorCallback {
                inputDonorId.error = it
            }
            .check()
        if (valid) {
            val user = usersService.findById(inputDonorId.editText!!.text.toString().toInt())
            user?.let {
                fillDonors(listOf(it))
            } ?: run {
                Toast.makeText(this, R.string.donor_not_found, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun refreshActivity() {
        startActivity(Intent(this, this::class.java))
        finish()
    }

    private fun deleteAccount() {
        val userId = this.getLoggedUserId()
        if (userId != -1) {
            val user = usersService.findById(userId)
            user?.let {
                logoutUser()
                usersService.delete(it)
                Toast.makeText(this, R.string.account_deleted, Toast.LENGTH_LONG).show()
                refreshActivity()
            }
        }
    }

    private fun listDonors() {
        val donors = usersService.findAll()
        fillDonors(donors)
    }

    private fun fillDonors(donors: List<User>) {
        donorsList.adapter = DonorsListAdapter(this, donors, this)
    }
}
