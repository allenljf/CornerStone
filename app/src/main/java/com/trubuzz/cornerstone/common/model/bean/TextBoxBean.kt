package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class TextBoxData(
    val isHideable: String,
    val isShowTitle: String,
    val text: String,
    val title: String,
    val type: String
)