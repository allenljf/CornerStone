package com.trubuzz.cornerstone.assets.model.bean

import androidx.annotation.Keep

@Keep
data class AssetsData(
    val accountId: String,
    val accountStatus: String,
    val fundAmount: String,
    val fundPosition: List<FundPosition>,
    val riskLevel: String,
    val totalAmount: String,
    val totalPNL: String,
    val totalPNLRate: String
)

@Keep
data class FundPosition(
    val allotDate: String,
    val allotValue: String,
    val annualPNL: String,
    val fundId: String,
    val lockDuring: String,
    val name: String,
    val totalAmount: String,
    val totalPNL: String
)