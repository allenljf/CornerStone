package com.trubuzz.cornerstone.api

import android.content.Context
import android.view.View
import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.trubuzz.cornerstone.common.model.bean.*
import com.trubuzz.cornerstone.widget.*

object SectionConverter {
    fun convert(objectList: List<Any>): List<Any> {
        var resultList = mutableListOf<Any>()

        for (item in objectList) {
            var map = item as LinkedTreeMap<String, Any>
            var type = map.get("type").toString()
            var json = Gson().toJson(map)

            when (type) {
                "lineChart" -> {
                    val type = object : TypeToken<LineChartData>() {

                    }.type
                    val data: LineChartData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "pieChart" -> {
                    val type = object : TypeToken<PieChartData>() {

                    }.type
                    val data: PieChartData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "gisView" -> {
                    val type = object : TypeToken<GisViewData>() {

                    }.type
                    val data: GisViewData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "pressureTest" -> {
                    val type = object : TypeToken<PressureTestData>() {

                    }.type
                    val data: PressureTestData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "styleBox" -> {
                    val type = object : TypeToken<StyleBoxData>() {

                    }.type
                    val data: StyleBoxData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "fundBaseInfo" -> {
                    val type = object : TypeToken<FundBaseInfoData>() {

                    }.type
                    val data: FundBaseInfoData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "allotRule" -> {
                    val type = object : TypeToken<AllotRuleData>() {

                    }.type
                    val data: AllotRuleData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "textBox" -> {
                    val type = object : TypeToken<TextBoxData>() {

                    }.type
                    val data: TextBoxData = Gson().fromJson(json, type)
                    resultList.add(data)
                }
                "patternInformation" -> {
                    val type = object : TypeToken<LinkedTreeMap<*, *>>() {

                    }.type
                    val patternInformation: LinkedTreeMap<*, *> = Gson().fromJson(json, type)
                    resultList.add(convertPatternInformation(patternInformation))
                }
            }
        }

        return resultList
    }

    private fun convertPatternInformation(input: LinkedTreeMap<*, *>): List<Any> {
        val sectionsMap = input["sections"] as List<Any>
        return convert(sectionsMap)
    }

    @Keep
    data class PatternInformation(
        var sectionList: List<View>,
        var nameList: List<String>
    )

    fun initPatternInformationList(inputList: List<Any>, context: Context): PatternInformation {
        var sectionList = mutableListOf<View>()
        var nameList = mutableListOf<String>()

        for (item in inputList) {

            when (item.javaClass.simpleName) {
                "LineChartData" -> {
                    var lineChart = LineChartView(context, item as LineChartData)
                    sectionList.add(lineChart)
                    nameList.add(item.title)
                }
                "PieChartData" -> {
                    var pieChart = PieChartView(context, item as PieChartData)
                    sectionList.add(pieChart)
                    nameList.add(item.title)
                }
                "GisViewData" -> {
                    var gisView = GisView(context, item as GisViewData)
                    sectionList.add(gisView)
                    nameList.add(item.title)
                }
                "StyleBoxData" -> {
                    var styleBox = StyleBoxView(context, (item as StyleBoxData).getValueArray())
                    sectionList.add(styleBox)
                    nameList.add(item.title)
                }
                "PressureTestData" -> {
                    var pressureTest = PressureTestView(context, item as PressureTestData)
                    sectionList.add(pressureTest)
                    nameList.add(item.title)
                }
            }
        }

        return PatternInformation(sectionList, nameList)
    }
}