package com.trubuzz.cornerstone.login.model.bean

import androidx.annotation.Keep

@Keep
data class LoginData(
    val session: String,
    val token: String,
    val userInfo: UserInfo
)

@Keep
data class UserInfo(
    val avatar: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val nick: String,
    val phone: String,
    val userExtInfo: String
)