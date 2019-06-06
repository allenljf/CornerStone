package com.trubuzz.cornerstone.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.model.bean.LineChartData
import com.trubuzz.cornerstone.util.FormatUtils
import kotlinx.android.synthetic.main.view_line_chart.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class LineChartView(context: Context, data: LineChartData) : LinearLayout(context) {
    private val ZERO_LIMIT = 0.005f
    private val sdYMD = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    private val LINE_X_LABEL_COUNT = 3
    private val LINE_Y_LABEL_COUNT = 4
    private val titleList = ArrayList<String>()
    private val colorList = ArrayList<String>()
    private val bean: LineChartData = data

    init {
        View.inflate(getContext(), R.layout.view_line_chart, this)
        init()
    }

    fun init() {
        val entryListSet = ArrayList<List<Entry>>()

        for (linesBean in bean.lines) {
            titleList.add(linesBean.title)
            colorList.add(linesBean.color)

            val entryList = ArrayList<Entry>()
            for (pointsBean in linesBean.points) {
                val entry = Entry((pointsBean.time / 1000).toFloat(), pointsBean.value.toFloat())
                entryList.add(entry)
            }

            if(entryList.isEmpty()){
                ll_line_chart.visibility = View.GONE
                continue
            }
            entryListSet.add(entryList)
        }

        //tabs
        val btnList = ArrayList<RadioButton>()
        btnList.add(btn_3m)
        btnList.add(btn_6m)
        btnList.add(btn_ytd)
        btnList.add(btn_1y)
        btnList.add(btn_max)
        setEnableTabs(entryListSet, btnList)

        tabs.setOnCheckedChangeListener{ group, checkedId ->
            setLineChart(filterLineData(entryListSet, checkedId))
        }

        setLineChart(filterLineData(entryListSet, R.id.btn_max))

        sdYMD.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun setLineChart(entryListSet: List<List<Entry>>) {
        setMarkerView(entryListSet)
        setChartListener()

        /*************
         * 設定
         */
        lineChart.description.isEnabled = false
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.setPinchZoom(false)
        lineChart.setScaleEnabled(false)
        lineChart.minOffset = 0f

        lineChart.isHighlightPerTapEnabled = true
        lineChart.isHighlightPerDragEnabled = true
        lineChart.extraTopOffset = 25f
        lineChart.extraLeftOffset = -10f
        lineChart.extraRightOffset = 0f
        lineChart.extraBottomOffset = 5f
        lineChart.setPadding(0, 0, 0, 0)

        val mXAxis: XAxis
        val mLeftYAxis: YAxis
        val mRightYAxis: YAxis

        mXAxis = lineChart.xAxis
        mXAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            sdYMD.format(Date(value.toLong() * 1000))
        }

        val textColor = ContextCompat.getColor(context, R.color.black_222328)

        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        mXAxis.setDrawGridLines(false)
        mXAxis.setDrawAxisLine(false)
        mXAxis.setAvoidFirstLastClipping(true)
        mXAxis.setTextColor(textColor)
        mXAxis.setTextSize(10f)

        mLeftYAxis = lineChart.axisLeft
        mLeftYAxis.setLabelCount(LINE_Y_LABEL_COUNT, true)
        mLeftYAxis.setDrawGridLines(false)
        mLeftYAxis.setSpaceTop(0f)
        mLeftYAxis.setSpaceBottom(0f)
        mLeftYAxis.setDrawAxisLine(false)
        mLeftYAxis.setTextColor(textColor)
        mLeftYAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)

        mLeftYAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            FormatUtils.initDecimalFormat(0).format(value) + "%"
        }
        mLeftYAxis.setXOffset(10f)

        mLeftYAxis.setGridColor(textColor)
        mLeftYAxis.setGridLineWidth(0.5f)

        mRightYAxis = lineChart.axisRight
        mRightYAxis.setEnabled(false)

        val l = lineChart.legend
        l.form = Legend.LegendForm.CIRCLE
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.textColor = textColor
        l.formSize = 10f
        l.xEntrySpace = 50f
        l.xOffset = -10f
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

        /*************
         * 資料
         */
        val sets = ArrayList<ILineDataSet>()
        for (i in 0 until entryListSet.size) {
            val lds = LineDataSet(entryListSet[i], titleList[i])
            lds.setLineWidth(1f)
            lds.setDrawCircles(false)
            lds.setDrawValues(false)
            lds.setColors(Color.parseColor(colorList[i]))
            sets.add(lds)
        }

        val data = LineData(sets)


        /*************
         * Y軸Label
         */
        val buffer = 1.1f
        val axisMin = data.getYMin(YAxis.AxisDependency.LEFT) * buffer
        val axisMax = data.getYMax(YAxis.AxisDependency.LEFT) * buffer

        val left = lineChart.axisLeft

        if (axisMin == 0f && axisMax == 0f) {
            left.axisMaximum = 20f
            left.axisMinimum = -10f
        } else if (axisMax >= 0 && axisMin >= 0) {
            val dividedNumber = 3.0
            left.axisMinimum = 0f

            if (axisMax <= 30) {
                left.axisMaximum = 30f
            } else {
                val performanceGranularity = calculateGranularity(axisMax.toDouble(), dividedNumber)
                left.axisMaximum = (performanceGranularity * dividedNumber).toFloat()
            }
        } else if (axisMax <= 0 && axisMin <= 0) {
            val dividedNumber = 3.0
            left.axisMaximum = 0f

            if (axisMin >= -30) {
                left.axisMinimum = -30f
            } else {
                val performanceGranularity = calculateGranularity(axisMin.toDouble(), dividedNumber)
                left.axisMinimum = (-performanceGranularity * dividedNumber).toFloat()
            }
        } else {
            val dividedNumber = 2.0
            val performanceGranularityMax = calculateGranularity(axisMax.toDouble(), dividedNumber)
            val performanceGranularityMin = calculateGranularity(axisMin.toDouble(), dividedNumber)

            if (performanceGranularityMax > performanceGranularityMin) {
                val performanceGranularity = calculateGranularity(axisMax.toDouble(), dividedNumber)
                left.axisMaximum = (performanceGranularity * dividedNumber).toFloat()
                left.axisMinimum = (-performanceGranularity).toFloat()

                if (Math.abs(axisMin) > axisMax / 2) {
                    left.axisMinimum = (-calculateGranularity(axisMin.toDouble(), 1.0)).toFloat()
                    left.axisMaximum = Math.abs(left.axisMinimum) * 2
                }
            } else {
                val performanceGranularity = calculateGranularity(axisMin.toDouble(), dividedNumber)
                left.axisMaximum = performanceGranularity.toFloat()
                left.axisMinimum = (-(performanceGranularity * dividedNumber)).toFloat()

                if (axisMax > Math.abs(axisMin) / 2) {
                    left.axisMaximum = calculateGranularity(axisMax.toDouble(), 1.0).toFloat()
                    left.axisMinimum = -left.axisMaximum * 2
                }
            }
        }

        left.setLabelCount(LINE_Y_LABEL_COUNT, true)

        /*************
         * X軸Label
         */

        val color = ContextCompat.getColor(context, R.color.black_222328)
        mXAxis.setLabelCount(LINE_X_LABEL_COUNT, true)

        for (i in 0 until data.getDataSetCount()) {
            (data.getDataSetByIndex(i) as LineDataSet).setHighLightColor(color)
            (data.getDataSetByIndex(i) as LineDataSet).setLineWidth(0.5f)
            data.getDataSetByIndex(i)!!.setHighlightEnabled(false)
        }

        if (data.getDataSetCount() == 1) {
            data.getDataSetByIndex(0)!!.setHighlightEnabled(true)
            (data.getDataSetByIndex(0) as LineDataSet).setDrawHorizontalHighlightIndicator(false)
            lineChart.legend.isEnabled = false
            setLineFill((data.getDataSetByIndex(0) as LineDataSet)!!)
        } else {
            for (i in 0 until data.getDataSetCount()) {
                data.getDataSetByIndex(i)!!.setHighlightEnabled(true)
                (data.getDataSetByIndex(i) as LineDataSet).setDrawHorizontalHighlightIndicator(false)
            }
            lineChart.legend.isEnabled = true
            lineChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }

        lineChart.data = data
        lineChart.invalidate()
    }

    private fun calculateGranularity(value: Double, dividedNumber: Double): Double {
        var value = value
        value = Math.abs(value)
        val ten = Math.floor(Math.log(value) / Math.log(10.0))
        val unit = Math.pow(10.0, ten) * 0.5 * dividedNumber
        val result = Math.ceil(value / unit) * unit / dividedNumber
        return if (result < 10) {
            10.0
        } else {
            result
        }
    }

    private fun setLineFill(ld: LineDataSet) {
        val baseColor = ld.getColor(0)

        val red = Color.red(baseColor)
        val green = Color.green(baseColor)
        val blue = Color.blue(baseColor)

        if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            val alpha = intArrayOf(0x5A, 0x2D, 0x0)
            val colors = IntArray(alpha.size)
            for (i in alpha.indices) {
                colors[i] = Color.argb(alpha[i], red, green, blue)
            }
            val drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            ld.fillDrawable = drawable
        } else {
            ld.fillColor = Color.argb(0x4d, red, green, blue)
        }
        ld.setDrawFilled(true)
        ld.fillFormatter = LineChartFillFormatter()
    }

    private class LineChartFillFormatter : IFillFormatter {
        override fun getFillLinePosition(dataSet: ILineDataSet, dataProvider: LineDataProvider): Float {
            var fillMin = 0f
            val chartMaxY = dataProvider.yChartMax
            val chartMinY = dataProvider.yChartMin

            val data = dataProvider.lineData

            if (dataSet.yMax > 0 && dataSet.yMin < 0) {
                fillMin = 0f
            } else {

                val max: Float
                val min: Float

                if (data.yMax > 0)
                    max = 0f
                else
                    max = chartMaxY
                if (data.yMin < 0)
                    min = 0f
                else
                    min = chartMinY

                fillMin = if (dataSet.yMin >= 0) min else max
            }

            return fillMin
        }
    }

    fun filterLineData(entryListSet: List<List<Entry>>, filter: Int): List<List<Entry>> {
        val checkTimePoint = checkTimePoint(filter)

        val resultListSet = ArrayList<List<Entry>>()
        for (entryList in entryListSet) {
            val resultList = ArrayList<Entry>()
            var isGetBase = false
            var baseRate = 0f
            for (e in entryList) {
                //重新計算區間內成長率
                if (e.x > checkTimePoint) {
                    if (!isGetBase) {
                        baseRate = 100 + e.y
                        isGetBase = true
                    }

                    if (baseRate == 0f || checkTimePoint == 0L) {
                        resultList.add(e)
                    } else {
                        val newEntry = Entry()
                        newEntry.x = e.x
                        val nowRate = 100 + e.y
                        val changeRate = (nowRate - baseRate) / baseRate * 100
                        newEntry.y = changeRate
                        resultList.add(newEntry)
                    }
                }
            }
            resultListSet.add(resultList)
        }

        return resultListSet
    }

    private fun checkTimePoint(filter: Int): Long {
        val MONTH_SECOND = (86400 * 365 / 12).toLong()
        val now_time = System.currentTimeMillis() / 1000
        var time_check_point: Long = 0

        when(filter){
            R.id.btn_3m -> time_check_point = MONTH_SECOND * 3
            R.id.btn_6m -> time_check_point = MONTH_SECOND * 6
            R.id.btn_1y -> time_check_point = MONTH_SECOND * 12
        }
        time_check_point = now_time - time_check_point

        if (filter == R.id.btn_ytd) {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val tsStr = "$year-01-01 00:00:00"
            time_check_point = Timestamp.valueOf(tsStr).time / 1000
        }

        if (filter == R.id.btn_max) {
            time_check_point = 0
        }

        return time_check_point
    }

    fun setEnableTabs(entryListSet: List<List<Entry>>, btnList: List<RadioButton>) {
        for (radioButton in btnList) {
            for (entryList in filterLineData(entryListSet, radioButton.id)) {
                if (entryList.size < 2) {
                    radioButton.isEnabled = false
                    radioButton.alpha = 0.3f
                }
            }
        }
    }

    private fun getValueByTime(entryListSet: List<List<Entry>>, inputX: Float, whichLine: Int): Float? {
        for (entry in entryListSet[whichLine]) {
            if (entry.x == inputX) {
                return entry.y
            }
        }
        return null
    }

    fun setMarkerView(entryListSet: List<List<Entry>>) {

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                lineChart.requestDisallowInterceptTouchEvent(true)
                tv_time.setText(sdYMD.format(Date(e.x.toLong() * 1000)))

                if (entryListSet.size >= 2) {
                    tv_title2.setVisibility(VISIBLE)
                    tv_value2.setVisibility(VISIBLE)

                    tv_title1.setText(titleList[0] + ": ")
                    tv_title2.setText(titleList[1] + ": ")

                    val performance1 = getValueByTime(entryListSet, e.x, 0)
                    val performance2 = getValueByTime(entryListSet, e.x, 1)

                    tv_value1.text = Constant.NO_VALUE
                    tv_value1.setTextColor(ContextCompat.getColor(context, R.color.gray_B4B4B4))
                    tv_value2.text = Constant.NO_VALUE
                    tv_value2.setTextColor(ContextCompat.getColor(context, R.color.gray_B4B4B4))

                    if (performance1 != null) {
                        FormatUtils.formatTextView(
                            performance1.toString(),
                            FormatUtils.percentColor,
                            tv_value1,
                            context
                        )
                    }
                    if (performance2 != null) {
                        FormatUtils.formatTextView(
                            performance2.toString(),
                            FormatUtils.percentColor,
                            tv_value2,
                            context
                        )
                    }
                } else {
                    tv_title2.setVisibility(GONE)
                    tv_value2.setVisibility(GONE)

                    tv_title1.setText(titleList[0] + ": ")
                    FormatUtils.formatTextView(e.y.toString(), FormatUtils.percentColor, tv_value1, context)
                }

                lineChart.invalidate()

                marker_view.setVisibility(VISIBLE)
                tabs.setVisibility(GONE)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, mDelayTime.toLong())

            }

            override fun onNothingSelected() {
                lineChart.requestDisallowInterceptTouchEvent(false)
            }
        })
    }

    internal var handler = Handler()
    internal var mDelayTime = 2000
    private val runnable = Runnable {
        lineChart.highlightValue(null)
        marker_view.visibility = View.GONE
        tabs.visibility = View.VISIBLE
    }

    private fun setChartListener() {
        lineChart.setOnChartGestureListener(object : OnChartGestureListener {
            override fun onChartGestureStart(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture) {
                val h = lineChart.getHighlightByTouchPoint(me.x, me.y)
                lineChart.highlightValue(h, true)
            }

            override fun onChartGestureEnd(me: MotionEvent, lastPerformedGesture: ChartTouchListener.ChartGesture) {

            }

            override fun onChartLongPressed(me: MotionEvent) {

            }

            override fun onChartDoubleTapped(me: MotionEvent) {

            }

            override fun onChartSingleTapped(me: MotionEvent) {

            }

            override fun onChartFling(me1: MotionEvent, me2: MotionEvent, velocityX: Float, velocityY: Float) {

            }

            override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {

            }

            override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {

            }
        })
    }
}
