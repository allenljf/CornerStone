package com.trubuzz.cornerstone.common.model.bean

import androidx.annotation.Keep

@Keep
data class StyleBoxData(
    val largeAvg: Int,
    val largeGrow: Int,
    val largeValue: Int,
    val mediumAvg: Int,
    val mediumGrow: Int,
    val mediumValue: Int,
    val smallAvg: Int,
    val smallGrow: Int,
    val smallValue: Int,
    val title: String,
    val type: String
){
    fun getValueArray(): FloatArray {
        val values = FloatArray(9)
        values[0] = largeValue.toFloat()
        values[1] = largeAvg.toFloat()
        values[2] = largeGrow.toFloat()

        values[3] = mediumValue.toFloat()
        values[4] = mediumAvg.toFloat()
        values[5] = mediumGrow.toFloat()

        values[6] = smallValue.toFloat()
        values[7] = smallAvg.toFloat()
        values[8] = smallGrow.toFloat()

        return values
    }
}