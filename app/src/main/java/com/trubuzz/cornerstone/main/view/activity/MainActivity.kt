package com.trubuzz.cornerstone.main.view.activity

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.base.BaseActivity
import com.trubuzz.cornerstone.login.vm.LoginViewModel
import com.trubuzz.cornerstone.widget.TabEntity
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.FragmentStatePagerAdapter
import com.trubuzz.cornerstone.assets.view.fragment.AssetsFragment
import com.trubuzz.cornerstone.fund.view.fragment.FundFragment
import com.trubuzz.cornerstone.setting.view.fragment.SettingFragment
import com.trubuzz.cornerstone.util.CustomerUtils
import com.trubuzz.cornerstone.util.SPUtil
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UserAttributes
import org.jetbrains.anko.sdk27.coroutines.onClick


class MainActivity : BaseActivity<LoginViewModel>() {

    private val mFragments = mutableListOf<Fragment>()
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mTitles = arrayOf(R.string.TabBarItemAssetsTitle, R.string.TabBarItemFundTitle, R.string.TabBarMe)
    private val mIconUnSelectIds = intArrayOf(R.drawable.assets_dark, R.drawable.fund_dark, R.drawable.me_dark)
    private val mIconSelectIds = intArrayOf(R.drawable.assets_light, R.drawable.fund_light, R.drawable.me_light)

    override fun needRegisterNetworkChangeObserver(): Boolean {
        return false
    }

    override fun initView() {
        setToolbar(true)
        setCustomLayout(R.layout.activity_main)

        initToolbar()
        initTabPager()
        initIntercom()
    }

    override fun getData() {

    }

    private fun initToolbar(){
        setBack(false)
        setCustomerServiceIcon(true)
    }

    private fun initTabPager() {
        (0 until mTitles.size).mapTo(mTabEntities) {
            TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it])
        }

        mFragments.add(AssetsFragment.newInstance())
        mFragments.add(FundFragment.newInstance())
        mFragments.add(SettingFragment.newInstance())

        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })

        pager.adapter = object : FragmentStatePagerAdapter(this.supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return getString(mTitles[position])
            }
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                switchFragment(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        pager.offscreenPageLimit = 3
        switchFragment(0)
    }

    private fun switchFragment(position: Int){
        pager.currentItem = position
        tab_layout.currentTab = position
        when(position){
            0 -> {
                setTitleRes(mTitles[position])
                setCustomerServiceIcon(true)
            }
            1 -> {
                setTitleRes(0)
                setCustomerServiceIcon(false)
            }
            2 -> {
                setTitleRes(mTitles[position])
                setCustomerServiceIcon(true)
            }
        }
    }

    private fun initIntercom(){
        var session = SPUtil.session
        var token = SPUtil.token
        var account: String? = null
        var phone = ""
        var email = ""

        if(SPUtil.phone != ""){
            account = SPUtil.phone
            phone = SPUtil.areaCode + " " + account
        }else{
            account = SPUtil.email
            email = account
        }

        log("email " + email)
        log("phone " + phone)
        log("account " + account)
        log("session " + session)
        log("token " + token)

        CustomerUtils.register(session)
        val userAttributes = UserAttributes.Builder()
            .withEmail(email)
            .withPhone(phone)
            .withName(account)
            .build()

        Intercom.client().updateUser(userAttributes)


    }
}
