package com.trubuzz.cornerstone.widget.chart

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.PieEntry
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.base.BaseAdapter
import com.trubuzz.cornerstone.util.FormatUtils
import kotlinx.android.synthetic.main.list_item_pie_legend.view.*

object ChartUtil {
    fun setPieLegend(recyclerView: RecyclerView, entryList: List<PieEntry>, colors: List<Int>) {
        var adapter: BaseAdapter<PieEntry> =
            BaseAdapter(R.layout.list_item_pie_legend, entryList) { view: View, item: PieEntry ->
                val colorIndex = entryList.indexOf(item) % colors.size

                setColorShape(view.v_dot, colors[colorIndex])
                view.tv_title.text = item.label
                FormatUtils.formatTextView(
                    item.value.toString(), FormatUtils.percentNormalNoSign,
                    view.tv_value, App.instance()
                )
            }

        recyclerView.adapter = adapter
    }

    private fun setColorShape(view: View, argb: Int) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.OVAL
        drawable.setSize(view.width, view.height)
        drawable.setColor(argb)
        view.background = drawable
    }
}
