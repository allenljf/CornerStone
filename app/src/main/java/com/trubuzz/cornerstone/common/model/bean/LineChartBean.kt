package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class LineChartData(
    val lines: List<Line>,
    val title: String,
    val type: String
)

@Keep
data class Line(
    val color: String,
    val points: List<Point>,
    val title: String
)

@Keep
data class Point(
    val time: Long,
    val value: Double
)