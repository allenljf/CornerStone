package com.trubuzz.cornerstone.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.model.bean.PressureTestData
import kotlinx.android.synthetic.main.view_pressure_test.view.*
import java.text.DecimalFormat
import java.util.*


class PressureTestView(internal var mContext: Context, internal var data: PressureTestData) : LinearLayout(mContext) {

    private val ZERO_LIMIT = 0.005f
    private val PERCENT_SPACE = 5f
    private val PRESSURE_LABEL_COUNT = 5

    init {
        View.inflate(context, R.layout.view_pressure_test, this)
        initUI()
    }

    private fun initUI() {
        tv_title.text = data.title
        tv_description.text = data.description

        val yRobot = ArrayList<BarEntry>()
        val yGroup = ArrayList<BarEntry>()

        val xLabels = ArrayList<String>()
        for (i in 0 until data.events.size) {
            yRobot.add(
                BarEntry(
                    i.toFloat(),
                    data.events[i].robot.toFloat(),
                    data.events[i].detail
                )
            )
            yGroup.add(
                BarEntry(
                    i.toFloat(),
                    data.events[i].controlGroup.toFloat(),
                    data.events[i].detail
                )
            )
            xLabels.add(data.events[i].title)
        }

        setPressureChart(barChart, yRobot, yGroup, xLabels)
    }

    fun setPressureChart(
        barChart: BarChart,
        yRobot: ArrayList<BarEntry>,
        yGroup: ArrayList<BarEntry>,
        xLabels: List<String>?
    ) {
        val xAxis = barChart.xAxis
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = 0f
        xAxis.setCenterAxisLabels(true)
        xAxis.textColor = ContextCompat.getColor(context, R.color.gray_B4B4B4)
        xAxis.setDrawGridLines(false)
        xAxis.xOffset = 0f
        xAxis.textSize = 10f

        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            getXLabel(xLabels, value)
        }

        val left = barChart.axisLeft
        left.textColor = ContextCompat.getColor(context, R.color.gray_B4B4B4)

        left.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        left.setDrawAxisLine(false)
        left.gridLineWidth = 0f
        left.spaceTop = 0f
        left.spaceBottom = 0f
        left.setDrawGridLines(true)
        left.gridColor = ContextCompat.getColor(context, R.color.gray_E1E1E1)

        left.valueFormatter = IAxisValueFormatter { value, axis ->
            getYLabel(value)
        }

        val l = barChart.legend
        l.form = Legend.LegendForm.CIRCLE
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.textColor = ContextCompat.getColor(context, R.color.black_222328)
        l.formSize = 10f
        l.xEntrySpace = 50f
        l.xOffset = -10f
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setTouchEnabled(false)

        val set1: BarDataSet
        val set2: BarDataSet
        set1 = BarDataSet(yRobot, context.getString(R.string.ThisFund))
        set1.color = ContextCompat.getColor(context, R.color.orange_FFAC2A)
        set1.isHighlightEnabled = false

        set2 = BarDataSet(yGroup, data.controlGroup)
        set2.color = ContextCompat.getColor(context, R.color.blue_43AEFF)
        set2.isHighlightEnabled = false

        val data = BarData(set1, set2)
        data.setDrawValues(false)
        data.isHighlightEnabled = false

        var axisMin = data.getYMin(YAxis.AxisDependency.LEFT)
        var axisMax = data.getYMax(YAxis.AxisDependency.LEFT)

        if (axisMax > 0 && axisMin < 0) {
            var spaceTop = 0f
            var spaceBottom = 0f

            var countTop = 0
            var countBottom = 0
            var interval = 0.0

            var range = Math.abs(axisMax - axisMin)
            axisMin -= range / 100f * PERCENT_SPACE
            axisMax += range / 100f * PERCENT_SPACE
            range = Math.abs(axisMax - axisMin)

            while (true) {
                val minimum = axisMin - range / 100f * spaceBottom
                val maximum = axisMax + range / 100f * spaceTop

                interval = (Math.abs(maximum - minimum) / PRESSURE_LABEL_COUNT.toFloat()).toDouble()

                var count = Math.abs(minimum / interval)
                countBottom = Math.floor(count).toInt()
                count = Math.abs(maximum / interval)
                countTop = Math.floor(count).toInt()

                val tempMin = countBottom * interval
                val tempMax = countTop * interval

                var topOk = false
                var bottomOk = false
                if (java.lang.Double.compare(tempMin, Math.abs(axisMin).toDouble()) < 0) {
                    spaceBottom += 1f
                } else {
                    bottomOk = true
                }

                if (java.lang.Double.compare(tempMax, axisMax.toDouble()) < 0) {
                    spaceTop += 1f
                } else {
                    topOk = true
                }

                if (topOk && bottomOk) {
                    break
                }
            }

            left.axisMaximum = (interval * countTop).toFloat()
            left.axisMinimum = -(interval * countBottom).toFloat()
            left.setLabelCount(countTop + countBottom + 1, true)


        } else if (axisMax < 0 && axisMin < 0) {
            left.axisMaximum = 0f
            left.setLabelCount(PRESSURE_LABEL_COUNT, true)
            left.spaceTop = 0f
            left.spaceBottom = 5f
        } else {
            left.axisMinimum = 0f
            left.setLabelCount(PRESSURE_LABEL_COUNT, true)
            left.spaceTop = 5f
            left.spaceBottom = 0f
        }


        val groupSpace = 0.66f //0.58f;
        val barSpace = 0.05f  // 2x DataSet
        val barWidth = 0.12f //0.15f; // 2x DataSet

        data.setValueTextSize(8f)
        data.barWidth = barWidth
        data.isHighlightEnabled = true
        data.setValueTextColor(ContextCompat.getColor(context, R.color.black_222328))
        data.setDrawValues(false)
        data.setValueFormatter(PercentFormatter())

        val max = data.maxEntryCountSet

        val maximum = data.getGroupWidth(groupSpace, barSpace) * max.entryCount
        barChart.xAxis.axisMaximum = maximum
        barChart.data = data
        barChart.groupBars(0f, groupSpace, barSpace)
        barChart.extraRightOffset = 10f
        barChart.extraLeftOffset = 10f
        barChart.extraBottomOffset = 10f
        barChart.invalidate()
    }

    private fun initDecimalFormat(digits: Int): DecimalFormat {
        val b = StringBuffer()
        for (i in 0 until digits) {
            if (i == 0)
                b.append(".")
            b.append("0")
        }
        return DecimalFormat("###,###,###,##0$b")
    }

    fun hideTitle() {
        tv_title.setVisibility(View.GONE)
    }

    private fun getXLabel(xLabels: List<String>?, value: Float): String {
        return if (xLabels != null && value >= 0 && value < xLabels.size) {
            xLabels.get(value.toInt())
        } else ""
    }

    private fun getYLabel(value: Float): String {
        var label = value
        if (value > -ZERO_LIMIT && value < ZERO_LIMIT) {
            label = 0f
        }
        return initDecimalFormat(0).format(label.toDouble()) + "%"
    }
}
