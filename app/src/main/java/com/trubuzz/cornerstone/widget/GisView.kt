package com.trubuzz.cornerstone.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.github.mikephil.charting.data.PieEntry
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.model.bean.GisViewData
import com.trubuzz.cornerstone.widget.chart.ChartUtil
import kotlinx.android.synthetic.main.view_gis_view.view.*
import java.util.*


class GisView(internal var mContext: Context, internal var data: GisViewData) : LinearLayout(mContext) {
    private val gisColors = context.resources.getStringArray(R.array.gis_colors)

    private val locationStringValue = intArrayOf(
        R.string.InvestmentMapNorthAmerica,
        R.string.InvestmentMapLatinAmerica,
        R.string.InvestmentMapEuropeEmerging,
        R.string.InvestmentMapEuropeDeveloped,
        R.string.InvestmentMapAfricaAndMiddleEast,
        R.string.InvestmentMapJapan,
        R.string.InvestmentMapAustralasia,
        R.string.InvestmentMapAsiaEmerging,
        R.string.InvestmentMapAsiaDeveloped
    )

    private val locationKey = arrayOf(
        "NorthAmerica",
        "LatinAmerica",
        "EuropeDeveloped",
        "EuropeEmerging",
        "AfricaAndMiddleEast",
        "Japan",
        "Australasia",
        "AsiaDeveloped",
        "AsiaEmerging"
    )

    init {
        View.inflate(context, R.layout.view_gis_view, this)
        init()
    }

    private fun init() {
        tv_title.text = data.title
        gisDrawView.setBean(data)

        val entryList = ArrayList<PieEntry>()
        for (i in 0 until data.datas.size) {
            val data = data.datas[i]
            entryList.add(PieEntry(data.value.toFloat(), changeLocationString(data.title)))
        }

        ChartUtil.setPieLegend(rv_gis, entryList, getGisColors())
    }

    private fun getGisColors(): List<Int> {
        val colors = ArrayList<Int>()
        for (s in gisColors) {
            colors.add(Color.parseColor(s))
        }
        return colors
    }

    private fun changeLocationString(title: String): String {
        val index = locationKey.indexOf(title)
        return if (index < 0) Constant.NO_VALUE else context.getString(locationStringValue[index])
    }
}
