package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class PieChartData(
    val datas: List<PieData>,
    val title: String,
    val type: String
)

@Keep
data class PieData(
    val title: String,
    val value: Double
)