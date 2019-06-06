package com.trubuzz.cornerstone.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.model.bean.Manager
import kotlinx.android.synthetic.main.view_manager_box_section.view.*


class ManagerBoxView(internal var mContext: Context, internal var data: Manager) : LinearLayout(mContext) {
    init {
        View.inflate(context, R.layout.view_manager_box_section, this)
        init()
    }

    private fun init() {
        tv_title.text = data.title
        tv_manager.text = data.manager
        tv_detail.text = data.detail
    }
}
