package com.trubuzz.cornerstone.api

import androidx.annotation.Keep

@Keep
class BaseResponse<T>(
    val code: String,
    val errorCode: Int,
    val subCode: String?,
    val msg: String?,
    val data: T?
)

@Keep
class EmptyData {

}

@Keep
data class ServerError(
    val code: String,
    val errorCode: Int,
    val subCode: String?,
    val msg: String?
)