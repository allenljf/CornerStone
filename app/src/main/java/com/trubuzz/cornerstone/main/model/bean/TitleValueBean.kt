package com.trubuzz.cornerstone.main.model.bean

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class TitleValueBean(val country: String, val code: String) : Serializable
