package com.example.tugashiace.model

import com.google.firebase.database.Exclude

data class Users(
    val id: String,
    val nama: String?,
    val email: String,
    val username: String,
    val nomorHp: String?
) {
    constructor():this("", "", "", "", "")
    @Exclude
    fun toMap():Map<String, Any?>{
        return mapOf(
            "id" to id,
            "nama" to nama,
            "email" to email,
            "username" to username,
            "nomorHp" to nomorHp,
        )
    }
}