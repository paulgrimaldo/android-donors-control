package com.grimaldo.apps.controldonantes.dao

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import com.grimaldo.apps.controldonantes.UserAlreadyExistsException
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.AGE_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.BLOOD_DESC_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.BLOOD_TYPE_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.HEIGHT_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.ID_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.LAST_NAME_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.NAME_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.PASSWORD_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.TABLE_NAME
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.TYPE_COLUMN
import com.grimaldo.apps.controldonantes.dao.DBStructure.Companion.WEIGHT_COLUMN
import com.grimaldo.apps.controldonantes.domain.User


class UserService(context: Context) {
    private val db: DB = DB(context)

    fun save(user: User) {
        try {
            val query = "INSERT INTO $TABLE_NAME VALUES (" +
                    "${user.id}," +
                    "'${user.username}'," +
                    "'${user.lastName}'," +
                    "'${user.password}'," +
                    "${user.age}," +
                    "${user.weight}," +
                    "${user.height}," +
                    "'${user.bloodType}'," +
                    "'${user.bloodDesc}'," +
                    "${user.type}" +
                    ")"
            val sqLiteDatabase = db.writableDatabase
            sqLiteDatabase.execSQL(query)
            db.close()
        } catch (e: SQLiteConstraintException) {
            throw UserAlreadyExistsException()
        }
    }

    fun findByCredentials(username: String, password: String): User? {
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $TABLE_NAME.$NAME_COLUMN LIKE '%$username%' " +
                "AND $TABLE_NAME.$PASSWORD_COLUMN='$password' " +
                "AND $TABLE_NAME.$TYPE_COLUMN=${DBStructure.Types.NORMAL_USER.getTypeValue()}"
        val sqLiteDatabase = db.readableDatabase
        val cursor = sqLiteDatabase.rawQuery(query, null)
        if (!cursor.moveToFirst()) {
            return null
        }
        val user = loadUserFromCursor(cursor)
        cursor.close()
        db.close()
        return user
    }

    fun findAll(): List<User> {
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $TABLE_NAME.$TYPE_COLUMN =${DBStructure.Types.DONOR.getTypeValue()}"
        val sqLiteDatabase = db.readableDatabase
        val cursor = sqLiteDatabase.rawQuery(query, null)
        val users = ArrayList<User>()
        while (cursor.moveToNext()) {
            val user = loadUserFromCursor(cursor)
            users.add(user)
        }
        cursor.close()
        db.close()
        return users
    }

    fun delete(user: User) {
        val query = "DELETE FROM $TABLE_NAME WHERE $TABLE_NAME.$ID_COLUMN = ${user.id}"
        val sqLiteDatabase = db.writableDatabase
        sqLiteDatabase.execSQL(query)
    }

    fun update(user: User) {
        val query = "UPDATE $TABLE_NAME SET " +
                "$NAME_COLUMN= '${user.username}', " +
                "$LAST_NAME_COLUMN = '${user.lastName}', " +
                "$AGE_COLUMN = ${user.age}, " +
                "$WEIGHT_COLUMN = ${user.weight}, " +
                "$HEIGHT_COLUMN = ${user.height}, " +
                "$BLOOD_TYPE_COLUMN = '${user.bloodType}', " +
                "$BLOOD_DESC_COLUMN = '${user.bloodDesc}' " +
                "WHERE $TABLE_NAME.$ID_COLUMN = ${user.id}"
        val sqLiteDatabase = db.writableDatabase
        sqLiteDatabase.execSQL(query)
        db.close()
    }

    fun updatePassword(user: User) {
        val query = "UPDATE $TABLE_NAME SET " +
                "$PASSWORD_COLUMN = '${user.password}' " +
                "WHERE $TABLE_NAME.$ID_COLUMN = ${user.id}"
        val sqLiteDatabase = db.writableDatabase
        sqLiteDatabase.execSQL(query)
        db.close()
    }

    fun findById(id: Int): User? {
        val query = "SELECT * FROM $TABLE_NAME WHERE $TABLE_NAME.$ID_COLUMN = $id"
        val sqLiteDatabase = db.readableDatabase
        val cursor = sqLiteDatabase.rawQuery(query, null)
        if (!cursor.moveToFirst()) {
            return null
        }
        val user = loadUserFromCursor(cursor)
        cursor.close()
        db.close()
        return user
    }

    private fun loadUserFromCursor(cursor: Cursor): User {
        return User().apply {
            this.id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN))
            this.username = cursor.getString(cursor.getColumnIndex(NAME_COLUMN))
            this.password = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN))
            this.lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME_COLUMN))
            this.age = cursor.getInt(cursor.getColumnIndex(AGE_COLUMN))
            this.weight = cursor.getFloat(cursor.getColumnIndex(WEIGHT_COLUMN))
            this.height = cursor.getFloat(cursor.getColumnIndex(HEIGHT_COLUMN))
            this.bloodType = cursor.getString(cursor.getColumnIndex(BLOOD_TYPE_COLUMN))
            this.bloodDesc = cursor.getString(cursor.getColumnIndex(BLOOD_DESC_COLUMN))
            this.type = cursor.getInt(cursor.getColumnIndex(TYPE_COLUMN))
        }
    }
}