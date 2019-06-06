package com.trubuzz.cornerstone.fund.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.base.BaseAdapter
import com.trubuzz.cornerstone.base.BaseFragment
import com.trubuzz.cornerstone.fund.model.bean.Fund
import com.trubuzz.cornerstone.fund.model.bean.FundData
import com.trubuzz.cornerstone.fund.model.bean.FundItem
import com.trubuzz.cornerstone.fund.view.activity.FundActivity
import com.trubuzz.cornerstone.fund.vm.FundViewModel
import com.trubuzz.cornerstone.util.FormatUtils
import kotlinx.android.synthetic.main.fragment_fund.*
import kotlinx.android.synthetic.main.list_item_fund_index.view.*
import kotlinx.android.synthetic.main.list_item_fund_index_item.view.*
import kotlinx.android.synthetic.main.list_item_fund_index_title.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class FundFragment : BaseFragment<FundViewModel>() {

    companion object {
        fun newInstance(): FundFragment {
            val fragment = FundFragment()
            return fragment
        }
    }

    override fun initView() {
        setCustomLayout(R.layout.fragment_fund)
        initRefreshLayout()
    }

    override fun lazyLoad() {
        showLoadingView()
        getData()
    }

    private fun initRefreshLayout() {
        refreshLayout.setOnRefreshListener { getData() }
        refreshLayout.setEnableLoadMore(false)
    }

    private fun finishRefreshLayout() {
        refreshLayout.finishRefresh()
    }

    private fun getData() {
        mViewModel?.getFund().observe(this, Observer {
            it?.data?.let { data ->
                setFund(data)
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

    private fun setFund(data: FundData) {
        var specialFund = data.specialFund[0]
        tv_tag.text = specialFund.tag
        tv_introduce.text = specialFund.`class`
        tv_name.text = specialFund.name
        FormatUtils.formatTextView(specialFund.pnl, FormatUtils.percentColor, tv_pnl, mContext)

        tv_income_type.text = when (specialFund.incomeType) {
            "1" -> getString(R.string.FundExpectedReturn)
            "2" -> getString(R.string.FundPastAnnualizedReturnsTitle)
            "3" -> getString(R.string.FundPastAverageReturnsTitle)
            else -> ""
        }
        tv_view_btn.onClick {
            startActivity<FundActivity>(
                Constant.ARG_FUND_TYPE to data.specialFund[0].fundType,
                Constant.ARG_FUND_ID to data.specialFund[0].fundId
            )
        }

        var fundObjectList = mutableListOf<Any>()
        for (item in data.funds) {
            fundObjectList.add(item)

            for (subItem in item.list) {
                fundObjectList.add(subItem)
            }
        }

        var adapter: BaseAdapter<Any> =
            BaseAdapter(R.layout.list_item_fund_index, fundObjectList) { view: View, item: Any ->
                if (item is Fund) {
                    view.item_title.visibility = View.VISIBLE
                    view.item_title.tv_fund_type.text = item.tag
                    view.item_title.tv_fund_intro.text = item.introduce
                } else if (item is FundItem) {
                    view.item_item.visibility = View.VISIBLE
                    view.item_item.tv_fund_name.text = item.name
                    FormatUtils.formatTextView(
                        item.pnl,
                        FormatUtils.percentColor,
                        view.item_item.tv_fund_yield,
                        mContext
                    )

                    view.item_item.tv_fund_income_type.text = when (item.incomeType) {
                        "1" -> getString(R.string.FundExpectedReturn)
                        "2" -> getString(R.string.FundPastAnnualizedReturnsTitle)
                        "3" -> getString(R.string.FundPastAverageReturnsTitle)
                        else -> ""
                    }

                    view.item_item.tv_fund_class.text = item.`class`

                    when (item.fundType) {
                        "Fund" -> {
                            view.item_item.tv_fund_runtime.visibility = View.GONE
                            view.item_item.rating_bar.visibility = View.VISIBLE
                            view.item_item.rating_bar.setStar(item.stars.toFloat())
                        }
                        "Robot" -> {
                            view.item_item.tv_fund_runtime.visibility = View.VISIBLE
                            view.item_item.rating_bar.visibility = View.GONE
                            view.item_item.tv_fund_runtime.text = getString(R.string.FundHasRunningTitle) + item.during
                        }
                    }

                    view.onClick {
                        startActivity<FundActivity>(
                            Constant.ARG_FUND_TYPE to item.fundType,
                            Constant.ARG_FUND_ID to item.fundId
                        )
                    }
                }
            }
        rv_fund.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }
}