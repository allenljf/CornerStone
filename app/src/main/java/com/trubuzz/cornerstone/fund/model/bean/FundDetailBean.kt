package com.trubuzz.cornerstone.fund.model.bean

import androidx.annotation.Keep

@Keep
data class FundDetailData(
    val `class`: String,
    val during: String,
    val fundId: String,
    val introduce: String,
    val minimumAmount: String,
    val name: String,
    val pnl: String,
    val riskLevel: String,
    val stars: String,
    val sections: List<Any>,
    val tag: String,
    val incomeType:String,
    val currency:String
)
