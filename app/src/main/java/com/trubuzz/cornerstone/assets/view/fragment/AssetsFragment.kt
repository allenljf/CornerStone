package com.trubuzz.cornerstone.assets.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.assets.model.bean.AssetsData
import com.trubuzz.cornerstone.assets.model.bean.FundPosition
import com.trubuzz.cornerstone.assets.vm.AssetsViewModel
import com.trubuzz.cornerstone.base.BaseAdapter
import com.trubuzz.cornerstone.base.BaseFragment
import com.trubuzz.cornerstone.common.activity.WebViewActivity
import com.trubuzz.cornerstone.util.CustomerUtils
import com.trubuzz.cornerstone.util.FormatUtils
import com.trubuzz.cornerstone.util.SPUtil
import kotlinx.android.synthetic.main.fragment_assets.*
import kotlinx.android.synthetic.main.list_item_assets_fund.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class AssetsFragment : BaseFragment<AssetsViewModel>() {
    companion object {
        fun newInstance(): AssetsFragment {
            val fragment = AssetsFragment()
            return fragment
        }
    }

    override fun initView() {
        setCustomLayout(R.layout.fragment_assets)
        initRefreshLayout()
    }

    private fun initRefreshLayout() {
        refreshLayout.setOnRefreshListener { getData() }
        refreshLayout.setEnableLoadMore(false)
    }

    private fun finishRefreshLayout() {
        refreshLayout.finishRefresh()
    }

    private fun getData() {
        mViewModel?.getAssets().observe(this, Observer {
            it?.data?.let { data ->
                setAssets(data)
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

    override fun onResume() {
        super.onResume()
        lazyLoad()
    }

    override fun lazyLoad() {
        showLoadingView()
        getData()
    }

    private fun setAssets(data: AssetsData) {
        if (data.accountStatus == "0") {
            ll_no_account.visibility = View.VISIBLE
            ll_has_account.visibility = View.GONE

            btn_open_account.onClick {
                CustomerUtils.logEvent(Constant.ARG_OPEN_ACCOUNT, null)
                toast(R.string.OpenAccountToast)
            }
        } else if (data.accountStatus == "1") {
            if (SPUtil.privacyMode) {
                iv_eye.setImageResource(R.drawable.closed_eyes)
            } else {
                iv_eye.setImageResource(R.drawable.open_eyes)
            }

            iv_eye.onClick {
                SPUtil.privacyMode = !SPUtil.privacyMode
                if (SPUtil.privacyMode) {
                    iv_eye.setImageResource(R.drawable.closed_eyes)
                } else {
                    iv_eye.setImageResource(R.drawable.open_eyes)
                }
                setAssets(data)
            }

            ll_no_account.visibility = View.GONE
            ll_has_account.visibility = View.VISIBLE

            FormatUtils.formatTextView(data.totalAmount, FormatUtils.numberNormalNoSign, tv_total_amount, mContext)
            FormatUtils.formatTextView(data.fundAmount, FormatUtils.numberNormalNoSign, tv_fund_amount, mContext)
            FormatUtils.formatTextView(data.totalPNL, FormatUtils.numberColor, tv_pnl, mContext)
            FormatUtils.formatTextView(data.totalPNLRate, FormatUtils.percentColor, tv_pnl_rate, mContext)

            if (SPUtil.privacyMode) {
                tv_total_amount.text = Constant.SIX_STAR
                tv_fund_amount.text = Constant.SIX_STAR
                tv_pnl.text = Constant.SIX_STAR
            }

            var adapter: BaseAdapter<FundPosition> =
                BaseAdapter(R.layout.list_item_assets_fund, data.fundPosition) { view: View, item: FundPosition ->
                    view.tv_title.text = item.name
                    FormatUtils.formatTextView(
                        item.totalAmount,
                        FormatUtils.numberNormalNoSign,
                        view.tv_total_amount,
                        mContext
                    )
                    FormatUtils.formatTextView(item.totalPNL, FormatUtils.percentColor, view.tv_pnl, mContext)
                    FormatUtils.formatTextView(item.annualPNL, FormatUtils.percentColor, view.tv_annual_pnl, mContext)
                    FormatUtils.formatTextView(
                        item.allotValue,
                        FormatUtils.numberNormalNoSign,
                        view.tv_allot_value,
                        mContext
                    )
                    view.tv_allot_date.text = item.allotDate
                    view.tv_lock_during.text = item.lockDuring

                    if (SPUtil.privacyMode) {
                        view.tv_total_amount.text = Constant.SIX_STAR
                        view.tv_allot_value.text = Constant.SIX_STAR
                    }

                    view.btn_buy.onClick {
                        var params = hashMapOf("fundId" to item.fundId, "fundName" to item.name)
                        CustomerUtils.logEvent(Constant.ARG_BUY_FUND, params)
                        toast(R.string.BuyFundToast)
                    }
                }
            rv_fund.adapter = adapter
        }

        tv_risk.text = data.riskLevel
        rl_risk.onClick {
            startActivity<WebViewActivity>(
                Constant.ARG_URL to getString(R.string.risk_assessment_url),
                Constant.ARG_TITLE to ""
            )
        }
    }
}