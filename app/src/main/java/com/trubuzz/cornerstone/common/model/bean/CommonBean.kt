package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class CommonBean(
    val code: String?,
    val errorCode: Int?,
    val subCode: String?,
    val msg: String?
)