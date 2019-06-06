package com.trubuzz.cornerstone.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.model.bean.GisViewData
import org.jetbrains.anko.dip
import java.util.*

class GisDrawView : View {
    private var sectionBean: GisViewData? = null
    private var proportionX: Float = 0.toFloat()
    private var proportionY: Float = 0.toFloat()
    private val gisColors = context.resources.getStringArray(R.array.gis_colors)

    private var mContext: Context? = null


    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mContext = context

    }

    fun setBean(bean: GisViewData) {
        this.sectionBean = bean
        invalidate()
    }

    private fun drawPoint(canvas: Canvas) {
        val p1 = Paint()
        val p2 = Paint()
        val p3 = Paint()

        p1.isAntiAlias = true
        p2.isAntiAlias = true
        p3.isAntiAlias = true

        for (i in 0 until sectionBean!!.datas.size) {
            val bean = sectionBean!!.datas[i]
            val position = pointTable[bean.title] ?: continue

            val c = Color.parseColor(gisColors[i])
            val r = c and 0xff0000 shr 16
            val g = c and 0x00ff00 shr 8
            val b = c and 0x0000ff

            p1.setARGB(255, r, g, b)
            p2.setARGB(36, r, g, b)
            p3.setARGB(23, r, g, b)

            var radius = 0f
            val value = bean.value
            if (value <= 1) {
                radius = 3.5f
            } else {
                radius = 3.5f + value.toFloat() * 0.25f
            }
            radius = dip(radius).toFloat()

            canvas.drawCircle(position.x * proportionX, position.y * proportionY, radius, p3)
            //            canvas.drawCircle(position.x * proportionX, position.y * proportionY, CIRCLE_TWO * proportionX, p2);
            canvas.drawCircle(position.x * proportionX, position.y * proportionY, CIRCLE_ONE * proportionX, p1)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (null == sectionBean) return

        proportionX = width / 375.0.toFloat()
        proportionY = height / 226.0.toFloat()

        // draw world map
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.map)
        val rect = Rect(0, 0, width, height)
        canvas.drawBitmap(bitmap, null, rect, null)

        drawPoint(canvas)
    }

    companion object {

        private val CIRCLE_ONE = 1
        private val CIRCLE_TWO = 4
        private val CIRCLE_THREE = 8

        private val pointTable = object : HashMap<String, Point>() {
            init {
                put("NorthAmerica", Point(75, 87))
                put("LatinAmerica", Point(118, 146))
                put("EuropeDeveloped", Point(182, 85))
                put("EuropeEmerging", Point(194, 61))
                put("AfricaAndMiddleEast", Point(218, 116))
                put("Japan", Point(308, 100))
                put("Australasia", Point(303, 165))
                put("AsiaDeveloped", Point(296, 95))
                put("AsiaEmerging", Point(279, 110))
            }
        }
    }
}