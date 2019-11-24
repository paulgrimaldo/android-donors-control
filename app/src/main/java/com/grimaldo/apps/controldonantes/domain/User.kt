package com.grimaldo.apps.controldonantes.domain

import com.grimaldo.apps.controldonantes.dao.DBStructure
import java.io.Serializable

class User : Serializable {
    var id: Int? = null
    var username: String? = null
    var lastName: String? = null
    var age: Int? = null
    var weight: Float? = null
    var height: Float? = null
    var password: String? = null
    var bloodType: String? = null
    var bloodDesc: String? = null
    var type: Int = DBStructure.Types.NORMAL_USER.getTypeValue()

    override fun toString(): String {
        return "User(id=$id, username=$username, lastName=$lastName, age=$age, weight=$weight, height=$height, password=$password, bloodType=$bloodType, bloodDesc=$bloodDesc, type=$type)"
    }

    fun getFullName() = "$username $lastName"
    fun getFullBlood() = "$bloodType $bloodDesc"

    fun setAsNormalUser() {
        type = DBStructure.Types.NORMAL_USER.getTypeValue()
    }

    fun setAsDonor() {
        type = DBStructure.Types.DONOR.getTypeValue()
    }
}