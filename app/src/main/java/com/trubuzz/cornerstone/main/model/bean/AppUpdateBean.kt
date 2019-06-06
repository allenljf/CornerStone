package com.trubuzz.cornerstone.main.model.bean

import androidx.annotation.Keep

@Keep
data class AppUpdate(
    val message: String,
    val result: String,
    val title: String
)

@Keep
data class AppBadge(
    val list: List<AppBadgeItem>
)

@Keep
data class AppBadgeItem(
    val badge: String,
    val title: String,
    val type: String
)