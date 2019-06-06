package com.trubuzz.cornerstone.fund.view.activity

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.api.SectionConverter
import com.trubuzz.cornerstone.base.BaseActivity
import com.trubuzz.cornerstone.common.model.bean.*
import com.trubuzz.cornerstone.fund.model.bean.FundDetailData
import com.trubuzz.cornerstone.fund.vm.FundDetailViewModel
import com.trubuzz.cornerstone.util.CustomerUtils
import com.trubuzz.cornerstone.util.FormatUtils
import com.trubuzz.cornerstone.util.SPUtil
import com.trubuzz.cornerstone.widget.*
import kotlinx.android.synthetic.main.activity_fund.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.Exception

class FundActivity : BaseActivity<FundDetailViewModel>() {
    var fundType: String = ""
    var fundId: String = ""
    var fundName: String = ""

    override fun initView() {
        setToolbar(true)
        setCustomLayout(R.layout.activity_fund)
        setBack(true)
        setCustomerServiceIcon(false)

        fundType = intent.getStringExtra(Constant.ARG_FUND_TYPE)
        fundId = intent.getStringExtra(Constant.ARG_FUND_ID)

        showLoadingView()
        initRefreshLayout()
    }

    private fun initRefreshLayout(){
        refreshLayout.setOnRefreshListener { getData() }
        refreshLayout.setEnableLoadMore(false)
    }

    private fun finishRefreshLayout(){
        refreshLayout.finishRefresh()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun getData() {
        mViewModel?.getFundDetail(fundType, fundId).observe(this, Observer {
            it?.data?.let { data ->
                setFundDetail(data)
            }

            it?.serverError?.let { serverError ->
                showServerErrorToast(serverError)
                showErrorView()
            }

            it?.networkError?.let { networkError ->
                showNetworkErrorToast(networkError)
                showErrorView()
            }

            hideLoadingView()
            finishRefreshLayout()
        })
    }

    private fun setFundDetail(data: FundDetailData) {
        fundName = data.name

        tv_fund_class.text = data.`class`
        tv_fund_name.text = data.name
        FormatUtils.formatTextView(
            data.pnl,
            FormatUtils.percentColor,
            tv_past_avg_pnl,
            mContext
        )
        tv_fund_income_type.text = when (data.incomeType) {
            "1" -> getString(R.string.FundExpectedReturn)
            "2" -> getString(R.string.FundPastAnnualizedReturnsTitle)
            "3" -> getString(R.string.FundPastAverageReturnsTitle)
            else -> ""
        }

        if (fundType == "Fund") {
            tv_grid1_title.text = getString(R.string.FundRatingTitle)
            rating_bar.visibility = View.VISIBLE
            rating_bar.setStarEmptyDrawable(resources.getDrawable(R.drawable.star_dark))
            rating_bar.setStarFillDrawable(resources.getDrawable(R.drawable.star_light))
            rating_bar.setStarCount(5)
            rating_bar.halfStar(true)
            rating_bar.setStarImageWidth(20f)
            rating_bar.setStarImageHeight(20f)
            rating_bar.setImagePadding(3f)
            rating_bar.setStar(data.stars.toFloat())
            rating_bar.setmClickable(false)
        } else {
            tv_grid1_title.text = getString(R.string.FundHasRunningTitle)
            tv_grid1_value.visibility = View.VISIBLE
            tv_grid1_value.text = data.during
        }

        tv_invest_plan_yield.text = data.minimumAmount
        tv_risk_level.text = data.riskLevel

        var sectionList = SectionConverter.convert(data.sections)
        container.removeAllViews()

        for (item in sectionList) {

            when (item.javaClass.simpleName) {
                "ArrayList" -> {
                    var patternInformation = SectionConverter.initPatternInformationList(item as List<Any>, mContext)
                    if(patternInformation.sectionList.isNotEmpty()){
                        var sectionsView = SectionsView(mContext, patternInformation)
                        container.addView(sectionsView)
                    }
                }
                "LineChartData" -> {
                    var lineChart = LineChartView(mContext, item as LineChartData)
                    container.addView(lineChart)
                }
                "PieChartData" -> {
                    var pieChart = PieChartView(mContext, item as PieChartData)
                    container.addView(pieChart)
                }
                "FundBaseInfoData" -> {
                    item as FundBaseInfoData
                    var infoView = ExpandableView(mContext)
                    infoView.setTitle(item.title)
                    var managerBoxView = ManagerBoxView(mContext, item.manager)
                    infoView.addView(managerBoxView)
                    container.addView(infoView)

                    for (titleValue in item.datas) {
                        var titleValueListView = TitleValueListView(mContext, titleValue)
                        container.addView(titleValueListView)
                    }
                }
                "AllotRuleData" -> {
                    item as AllotRuleData
                    var infoView = ExpandableView(mContext)
                    infoView.setTitle(item.title)
                    infoView.addView(layoutInflater.inflate(R.layout.view_trade_notice, null))
                    container.addView(infoView)

                    for (titleValue in item.datas) {
                        var titleValueListView = TitleValueListView(mContext, titleValue)
                        container.addView(titleValueListView)
                    }
                }
                "TextBoxData" -> {
                    var textBoxData = item as TextBoxData
                    var textBox = TextBoxView(mContext)
                    textBox.setTitle(textBoxData.title)
                    textBox.setText(textBoxData.text)
                    container.addView(textBox)
                }
            }
        }



        tv_advise.onClick {
            CustomerUtils.help(mContext)
        }

        tv_buy.onClick {
            var params = hashMapOf("fundId" to fundId, "fundName" to fundName)
            CustomerUtils.logEvent(Constant.ARG_BUY_FUND, params)
            toast(R.string.BuyFundToast)
        }
    }

}