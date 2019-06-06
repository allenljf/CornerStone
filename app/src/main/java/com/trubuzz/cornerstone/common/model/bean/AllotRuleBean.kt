package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class AllotRuleData(
    val datas: List<TitleValueData>,
    val isHideable: String,
    val isShowtitle: String,
    val title: String,
    val type: String
)

@Keep
data class TitleValueData(
    val action: Action,
    val style: Style,
    val title: String,
    val value: String
)

@Keep
data class Action(
    val type: String,
    val url: String,
    val title: String
)

@Keep
data class Style(
    val color: String,
    val formats: List<String>
)