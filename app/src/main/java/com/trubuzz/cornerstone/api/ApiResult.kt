package com.trubuzz.cornerstone.api

import androidx.annotation.Keep

@Keep
class ApiResult<T>(
    val data: T?,
    val serverError: ServerError?,
    var networkError: Throwable?
)
