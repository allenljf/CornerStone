package com.trubuzz.cornerstone.setting.view.fragment

import android.app.Activity
import android.app.AlertDialog
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.base.BaseFragment
import com.trubuzz.cornerstone.common.activity.WebViewActivity
import com.trubuzz.cornerstone.login.view.LoginActivity
import com.trubuzz.cornerstone.login.vm.LoginViewModel
import com.trubuzz.cornerstone.util.CustomerUtils
import com.trubuzz.cornerstone.util.FormatUtils
import com.trubuzz.cornerstone.util.SPUtil
import kotlinx.android.synthetic.main.fragment_setting.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class SettingFragment : BaseFragment<LoginViewModel>() {

    companion object {
        fun newInstance(): SettingFragment {
            val fragment = SettingFragment()
            return fragment
        }
    }

    override fun lazyLoad() {

    }

    override fun initView() {
        setCustomLayout(R.layout.fragment_setting)

        var accountName = ""
        if (SPUtil.loginType == Constant.LOGIN_TYPE_PHONE) {
            tv_account_title.text = getString(R.string.Phone)
            accountName = FormatUtils.getStarString(SPUtil.areaCode, 3, 0) +
                    "-" + FormatUtils.getStarString(SPUtil.account, 0, 2)
        } else {
            tv_account_title.text = getString(R.string.Email)
            accountName = FormatUtils.getStarString(SPUtil.account, 2, 0)
        }
        tv_account.text = accountName

        ll_privacy.onClick {
            startActivity<WebViewActivity>(
                Constant.ARG_URL to getString(R.string.privacy_policy_url),
                Constant.ARG_TITLE to getString(R.string.SettingPrivacyPolicyTitle)
            )
        }

        var version = "v${mContext.packageManager.getPackageInfo(
            mContext.packageName, 0
        ).versionName}"
        tv_build_version.text = when (Constant.APP_BUILD_TYPE) {
            Constant.APP_BUILD_DAILY -> "DAILY"
            Constant.APP_BUILD_GLOBAL -> "GLOBAL"
            Constant.APP_BUILD_CN -> "CN"
            else -> "Unknow"
        } + " " + version

            tv_logout.onClick {
            AlertDialog.Builder(mContext)
                .setMessage(R.string.CheckLogout)
                .setPositiveButton(R.string.ModalOK) { dialog, which ->

                    CustomerUtils.logout()
                    SPUtil.session = ""
                    SPUtil.token = ""
                    (mContext as Activity).finish()
                    startActivity<LoginActivity>()
                }
                .setNegativeButton(R.string.ModalCancel) { dialog, which ->

                }
                .show()
        }
    }
}