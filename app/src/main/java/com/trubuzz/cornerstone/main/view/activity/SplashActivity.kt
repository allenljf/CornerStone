package com.trubuzz.cornerstone.main.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.base.BaseActivity
import com.trubuzz.cornerstone.login.view.LoginActivity
import com.trubuzz.cornerstone.main.vm.AppUpdateViewModel
import com.trubuzz.cornerstone.util.CustomerUtils
import com.trubuzz.cornerstone.util.SPUtil
import com.trubuzz.cornerstone.widget.CustomAlertDialog
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity<AppUpdateViewModel>() {
    private val jumpToMainActivity = {
        startActivity<MainActivity>()
        finish()
    }

    private val jumpToLoginActivity = {
        startActivity<LoginActivity>()
        finish()
    }

    override fun needRegisterNetworkChangeObserver(): Boolean {
        return false
    }

    override fun initView() {
        setToolbar(false)
        setCustomLayout(R.layout.activity_splash)

        resetServerAndTool()
//        Handler().postDelayed(getData(), 500)
        goToPage()
    }

    override fun getData() {
        mViewModel.getAppUpdate(packageManager.getPackageInfo(packageName, 0).versionName, "android")
            .observe(this, Observer {
                it?.data?.let { data ->
                    if (data.result == "true") {
                        var customAlertDialog =
                            CustomAlertDialog(this, getString(R.string.UpdateAppTitle), data.message)
                        customAlertDialog.show()

                        customAlertDialog.mDialog?.findViewById<TextView>(R.id.tv_sub_title)?.visibility = View.VISIBLE
                        customAlertDialog.mDialog?.findViewById<TextView>(R.id.tv_sub_title)?.text = data.title

                        customAlertDialog.mDialog?.findViewById<TextView>(R.id.tv_btn)?.text =
                            getString(R.string.ModalOK)
                        customAlertDialog.mDialog?.findViewById<TextView>(R.id.tv_btn)?.onClick {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(Constant.MARKET_URL)
                            startActivity(intent)
                        }
                    } else {
                        goToPage()
                    }
                }

                it?.serverError?.let { serverError ->
                    showServerErrorToast(serverError)
                }

                it?.networkError?.let { networkError ->
                    showNetworkErrorToast(networkError)
                }
            })
    }

    private fun goToPage() {
        val sessionId = SPUtil.session
        if (sessionId.isEmpty() || sessionId == "") {
            Handler().postDelayed(jumpToLoginActivity, 500)
        } else {
            Handler().postDelayed(jumpToMainActivity, 500)
        }
    }

    private fun resetServerAndTool() {
        Constant.APP_BUILD_TYPE = SPUtil.buildType
        RetrofitUtil.resetAll()

        CustomerUtils.initialization(
            App.instance(),
            Constant.getIntercomAppKey(this),
            Constant.getIntercomAppId(this)
        )
    }
}