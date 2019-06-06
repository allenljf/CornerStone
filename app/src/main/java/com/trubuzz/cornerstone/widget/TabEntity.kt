package com.trubuzz.cornerstone.widget

import com.flyco.tablayout.listener.CustomTabEntity
import com.trubuzz.cornerstone.App

class TabEntity(var res: Int, private var selectedIcon: Int, private var unSelectedIcon: Int) : CustomTabEntity {

    override fun getTabTitle(): String {
        return App.instance().getString(res)
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }
}