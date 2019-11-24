package com.grimaldo.apps.controldonantes.dao

class DBStructure {
    companion object {
        const val DB_NAME = "donors.sqlite"
        const val TABLE_NAME = "donors"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "username"
        const val LAST_NAME_COLUMN = "last_name"
        const val PASSWORD_COLUMN = "password"
        const val AGE_COLUMN = "age"
        const val WEIGHT_COLUMN = "weight"
        const val HEIGHT_COLUMN = "height"
        const val BLOOD_TYPE_COLUMN = "blood_type"
        const val BLOOD_DESC_COLUMN = "blood_description"
        const val TYPE_COLUMN = "type"
        const val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME (" +
                "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_COLUMN TEXT, " +
                "$LAST_NAME_COLUMN TEXT," +
                "$PASSWORD_COLUMN TEXT," +
                "$AGE_COLUMN INTEGER, " +
                "$WEIGHT_COLUMN REAL, " +
                "$HEIGHT_COLUMN REAL, " +
                "$BLOOD_TYPE_COLUMN TEXT," +
                "$BLOOD_DESC_COLUMN TEXT, " +
                "$TYPE_COLUMN INTEGER" +
                ")"
        const val DELETE_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    enum class Types(private val type: Int) {
        NORMAL_USER(1),
        DONOR(2);

        fun getTypeValue() = type
    }
}