package com.trubuzz.cornerstone.widget

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.trubuzz.cornerstone.R
import kotlinx.android.synthetic.main.view_stylebox.view.*

class StyleBoxView(internal var mContext: Context, internal var data: FloatArray) : LinearLayout(mContext) {
    private val mBackGroundColor: Int
    private val textViews = mutableListOf<TextView>()

    init {
        View.inflate(mContext, R.layout.view_stylebox, this)
        mBackGroundColor = ContextCompat.getColor(context, R.color.orange_FFAC2A)

        textViews.add(value00)
        textViews.add(value01)
        textViews.add(value02)

        textViews.add(value10)
        textViews.add(value11)
        textViews.add(value12)

        textViews.add(value20)
        textViews.add(value21)
        textViews.add(value22)

        var max = java.lang.Float.MIN_VALUE
        for (f in data) {
            max = Math.max(max, f)
        }

        val end = Math.min(data.size, textViews.size)
        for (i in 0 until end) {
            textViews[i].text = data[i].toString()
            val alpha = ((data[i] / max * 0.8 + 0.1) * 255).toInt()
            val color = Color.argb(
                alpha,
                Color.red(mBackGroundColor),
                Color.green(mBackGroundColor),
                Color.blue(mBackGroundColor)
            )
            textViews[i].setBackgroundColor(color)
        }
    }
}
