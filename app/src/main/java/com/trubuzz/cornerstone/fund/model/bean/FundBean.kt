package com.trubuzz.cornerstone.fund.model.bean

import androidx.annotation.Keep

@Keep
data class FundData(
    val funds: List<Fund>,
    val specialFund: List<SpecialFund>
)

@Keep
data class Fund (
    val introduce: String,
    val list: List<FundItem>,
    val tag: String
)

@Keep
data class FundItem(
    val fundId: String,
    val name: String,
    val fundType: String,
    val pnl: String,
    val during: String,
    val `class`: String,
    val stars: String,
    val t_cur: String,
    val incomeType: String
)

@Keep
data class SpecialFund(
    val `class`: String,
    val during: String,
    val fundId: String,
    val fundType: String,
    val t_cur: String,
    val incomeType: String,
    val name: String,
    val pnl: String,
    val stars: String,
    val tag: String
)
