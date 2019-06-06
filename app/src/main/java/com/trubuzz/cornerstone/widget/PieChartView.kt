package com.trubuzz.cornerstone.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.model.bean.PieChartData
import com.trubuzz.cornerstone.widget.chart.ChartUtil
import kotlinx.android.synthetic.main.view_pie_chart.view.*

import java.util.ArrayList


class PieChartView(context: Context, data: PieChartData) : LinearLayout(context) {
    private val pieChartData: PieChartData = data

    init {
        View.inflate(getContext(), R.layout.view_pie_chart, this)
        init()
    }

    fun init() {
        initUI()
    }

    val pieColors: List<Int>
        get() {
            val pieColors = context.resources.getStringArray(R.array.pie_colors)
            val colors = ArrayList<Int>()
            for (s in pieColors) {
                colors.add(Color.parseColor(s))
            }
            return colors
        }


    private fun initUI() {
        tv_title.text = pieChartData.title

        val entryList = ArrayList<PieEntry>()
        for (i in 0 until pieChartData.datas.size) {
            val data = pieChartData.datas[i]
            entryList.add(PieEntry(data.value.toFloat(), data.title))
        }

        setPieChart(pieChart, entryList)
        ChartUtil.setPieLegend(rv_legend, entryList, pieColors)
    }

    fun setPieChart(pieChart: PieChart, entryList: List<PieEntry>) {
        val dataSet = PieDataSet(entryList, "")
        dataSet.colors = pieColors
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(0f)

        pieChart.data = pieData
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setDrawMarkers(false)
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = false
        pieChart.transparentCircleRadius = 0f
        pieChart.invalidate()
    }

}
