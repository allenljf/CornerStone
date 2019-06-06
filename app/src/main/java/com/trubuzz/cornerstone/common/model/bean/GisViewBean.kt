package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class GisViewData(
    val datas: List<GisData>,
    val title: String,
    val type: String
)

@Keep
data class GisData(
    val title: String,
    val value: Double
)