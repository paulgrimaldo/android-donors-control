package com.grimaldo.apps.controldonantes

import android.content.Context
import com.grimaldo.apps.controldonantes.domain.User

fun Context.saveLoggedUser(user: User) {
    this.getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(AppConstants.PREFERENCES_LOGGED_PROPERTY, true)
        .putString(AppConstants.PREFERENCES_USERNAME_PROPERTY, user.username)
        .putInt(AppConstants.PREFERENCES_ID_PROPERTY, user.id!!)
        .apply()
}

fun Context.logoutUser() {
    this.getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(AppConstants.PREFERENCES_LOGGED_PROPERTY, false)
        .putString(AppConstants.PREFERENCES_USERNAME_PROPERTY, null)
        .putInt(AppConstants.PREFERENCES_ID_PROPERTY, -1)
        .apply()
}

fun Context.getLoggedUserId(): Int {
    return this.getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE)
        .getInt(AppConstants.PREFERENCES_ID_PROPERTY, -1)
}