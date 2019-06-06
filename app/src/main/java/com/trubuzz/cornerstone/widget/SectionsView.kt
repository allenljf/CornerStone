package com.trubuzz.cornerstone.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.api.SectionConverter
import kotlinx.android.synthetic.main.view_sections.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.windowManager


class SectionsView(
    internal var mContext: Context,
    internal var patternInformation: SectionConverter.PatternInformation
) : LinearLayout(mContext) {
    var nameList = patternInformation.nameList
    var sectionList = patternInformation.sectionList

    init {
        View.inflate(context, R.layout.view_sections, this)
        init()
    }

    private fun init() {
        tabs.weightSum = nameList.size.toFloat()
        val metrics = DisplayMetrics()
        mContext.windowManager.defaultDisplay.getMetrics(metrics)
        val width = (metrics.widthPixels - dip(30)) / nameList.size

        for (i in nameList.indices) {
            val radioButton = RadioButton(context)
            val childParam1 = RadioGroup.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT)
            radioButton.id = i
            radioButton.gravity = Gravity.CENTER
            radioButton.layoutParams = childParam1
            radioButton.text = nameList[i]
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.black_222328))
            val drawable: Drawable
            if (i == 0) {
                drawable = context.getDrawable(R.drawable.toggle_widget_left_bg)
                radioButton.setTextColor(ContextCompat.getColor(context, R.color.white_FFFFFF))
                radioButton.isChecked = true
            } else if (i == nameList.size - 1) {
                drawable = context.getDrawable(R.drawable.toggle_widget_right_bg)
            } else {
                drawable = context.getDrawable(R.drawable.toggle_widget_center_bg)
            }
            radioButton.background = drawable

            radioButton.buttonDrawable = null
            tabs.addView(radioButton, childParam1)
        }

        pager.adapter = MyViewPagerAdapter(sectionList)
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                tabs.check(position)
            }
        })

        tabs.setOnCheckedChangeListener { radioGroup, checkedId ->
            pager.currentItem = checkedId

            for (i in 0 until nameList.size) {
                var button = tabs.getChildAt(i) as RadioButton
                if (checkedId == i) {
                    button.setTextColor(ContextCompat.getColor(context, R.color.white_FFFFFF))
                } else {
                    button.setTextColor(ContextCompat.getColor(context, R.color.black_222328))
                }
            }
        }

        pager.currentItem = 0
    }

    inner class MyViewPagerAdapter(private val mListViews: List<View>) : PagerAdapter() {

        override fun getCount(): Int {
            return mListViews.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mListViews[position])
        }


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.removeView(mListViews[position])
            container.addView(mListViews[position], 0)
            return mListViews[position]
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }
    }
}
