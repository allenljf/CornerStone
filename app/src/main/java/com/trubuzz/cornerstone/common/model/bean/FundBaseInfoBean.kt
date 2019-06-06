package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class FundBaseInfoData(
    var manager: Manager,
    val datas: List<TitleValueData>,
    val isHideable: String,
    val isShowtitle: String,
    val title: String,
    val type: String
)

@Keep
data class Manager(
    val detail: String,
    val manager: String,
    val title: String
)