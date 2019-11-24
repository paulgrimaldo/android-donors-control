package com.grimaldo.apps.controldonantes.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB(context: Context) : SQLiteOpenHelper(context, DBStructure.DB_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DBStructure.CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DBStructure.DELETE_TABLE_QUERY)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 3
    }
}