package com.trubuzz.cornerstone.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.api.ServerError
import com.trubuzz.cornerstone.login.view.LoginActivity
import com.trubuzz.cornerstone.util.CustomerUtils
import com.trubuzz.cornerstone.util.net.NetStateChangeObserver
import com.trubuzz.cornerstone.util.net.NetStateChangeReceiver
import com.trubuzz.cornerstone.util.net.NetUtils
import com.trubuzz.cornerstone.util.net.NetworkType
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_network_state.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.lang.reflect.ParameterizedType
import java.util.*


abstract class BaseActivity<T : ViewModel> : AppCompatActivity(), NetStateChangeObserver {
    var isNetworkOk: Boolean = true
    lateinit var mContext: Context

    val mViewModel: T by lazy {
        ViewModelProviders.of(this).get(getTClass())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        mContext = this

        initView()
    }

    abstract fun initView()

    fun setToolbar(isShow: Boolean) {
        toolbar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setTitleRes(resId: Int) {
        if (resId != 0) {
            toolbar.toolbar_title.visibility = View.VISIBLE
            toolbar.toolbar_title.setText(resId)
        } else {
            toolbar.toolbar_title.visibility = View.GONE
        }
    }

    fun setBack(isShow: Boolean) {
        toolbar.rl_left_icon.visibility = if (isShow) View.VISIBLE else View.GONE
        toolbar.rl_left_icon.onClick {
            finish()
        }
    }

    fun setCustomerServiceIcon(isShow: Boolean) {
        toolbar.rl_right_icon.visibility = if (isShow) View.VISIBLE else View.GONE
        toolbar.rl_right_icon.onClick {
            CustomerUtils.help(mContext)
        }
    }

    fun setCustomLayout(resId: Int) {
        ll_container.addView(LayoutInflater.from(this).inflate(resId, null))
    }

    abstract fun getData()

    /*******************
    生命週期
     *******************/

    override fun onResume() {
        super.onResume()
        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.registerObserver(this)
            checkNetState()
        }
    }

    override fun onStop() {
        super.onStop()
        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.unregisterObserver(this)
        }
    }

    /*******************
    監聽網路狀態
     *******************/

    open fun needRegisterNetworkChangeObserver(): Boolean {
        return true
    }

    open fun checkNetState() {
        val networkType = NetUtils.getNetworkType(this)
        if (networkType == NetworkType.NETWORK_NO) {
            onNetDisconnected()
        } else {
            onNetConnected(networkType)
        }
    }

    override fun onNetConnected(networkType: NetworkType) {
        isNetworkOk = true
        hideNoNetworkView()
    }

    override fun onNetDisconnected() {
        isNetworkOk = false
        showNoNetworkView()
    }

    /*******************
    畫面及提示狀態
     *******************/

    fun showLoadingView() {
        view_network_state.loading_view.visibility = View.VISIBLE
    }

    fun hideLoadingView() {
        view_network_state.loading_view.visibility = View.GONE
    }

    fun showNoNetworkView() {
        view_network_state.no_network_view.visibility = View.VISIBLE
    }

    fun hideNoNetworkView() {
        view_network_state.no_network_view.visibility = View.GONE
    }

    fun showEmptyView() {
        view_network_state.empty_view.visibility = View.VISIBLE
    }

    fun hideEmptyView() {
        view_network_state.empty_view.visibility = View.GONE
    }

    fun showErrorView() {
        view_network_state.error_view.visibility = View.VISIBLE
    }

    fun hideErrorView() {
        view_network_state.error_view.visibility = View.GONE
    }

    fun showServerErrorToast(serverError: ServerError) {
        log("serverError " + serverError)
        serverError?.msg?.let {
            toast(it)
            return
        }

        when (serverError.errorCode) {
            -1000 -> {
                toast(R.string.UserLogout)
                startActivity<LoginActivity>()
            }
            -17 -> {
                toast(R.string.AuthCodeError)
            }
            -22 -> {
                toast(R.string.AuthcodeErrorExpired)
            }
            -19 -> {
                toast(R.string.AreaCodeExceedErrorText)
            }
            else -> {
                toast(R.string.ServerErrorRetry)
            }
        }
    }

    fun showNetworkErrorToast(throwable: Throwable) {
        toast(throwable.localizedMessage)
    }

    /*******************
    共用工具
     *******************/

    private fun getTClass(): Class<T> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    fun log(str: String) {
        Log.d(Constant.APP_TAG, str)
    }
}
