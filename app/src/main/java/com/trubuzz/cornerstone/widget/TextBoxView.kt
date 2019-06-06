package com.trubuzz.cornerstone.widget

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.trubuzz.cornerstone.R
import kotlinx.android.synthetic.main.view_text_box_section.view.*


class TextBoxView(context: Context) : RelativeLayout(context) {
    private var isExpanded = true

    init {
        View.inflate(getContext(), R.layout.view_text_box_section, this)
        init()
    }

    private fun init() {
        iv_arrow.setOnClickListener {
            if (isExpanded) {
                expandableLayout.collapse()
                iv_arrow.setImageResource(R.drawable.down)
            } else {
                expandableLayout.expand()
                iv_arrow.setImageResource(R.drawable.up)
            }
            isExpanded = !isExpanded
        }
    }

    fun setTitle(value: String) {
        tv_title.text = value
    }

    fun setText(value: String) {
        tv_text.text = value
    }
}
