package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class PressureTestData(
    val controlGroup: String,
    val description: String,
    val events: List<Event>,
    val robot: String,
    val title: String,
    val type: String
)

@Keep
data class Event(
    val controlGroup: Double,
    val detail: String,
    val robot: Double,
    val title: String
)