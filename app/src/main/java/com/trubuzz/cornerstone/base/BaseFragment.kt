package com.trubuzz.cornerstone.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.api.ServerError
import com.trubuzz.cornerstone.login.view.LoginActivity
import com.trubuzz.cornerstone.util.net.NetStateChangeObserver
import com.trubuzz.cornerstone.util.net.NetStateChangeReceiver
import com.trubuzz.cornerstone.util.net.NetUtils
import com.trubuzz.cornerstone.util.net.NetworkType
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_network_state.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.lang.reflect.ParameterizedType
import java.util.*


abstract class BaseFragment<T : ViewModel> : Fragment(), NetStateChangeObserver {
    var isNetworkOk: Boolean = true
    lateinit var mContext: Context

    var isViewPrepare = false
    var hasLoadData = false

    val mViewModel: T by lazy {
        ViewModelProviders.of(this).get(getTClass())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context!!
        var view = inflater.inflate(R.layout.fragment_base, null, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        isViewPrepare = true
        lazyLoadDataIfPrepared()
    }

    abstract fun initView()

    fun setCustomLayout(resId: Int) {
        ll_container.addView(LayoutInflater.from(context).inflate(resId, null))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    abstract fun lazyLoad()

    /*******************
    生命週期
     *******************/

    override fun onResume() {
        super.onResume()
        NetStateChangeReceiver.registerObserver(this)
        checkNetState()
    }

    override fun onStop() {
        super.onStop()
        NetStateChangeReceiver.unregisterObserver(this)
    }

    /*******************
    監聽網路狀態
     *******************/

    open fun needRegisterNetworkChangeObserver(): Boolean {
        return true
    }

    open fun checkNetState() {
        val networkType = NetUtils.getNetworkType(mContext!!)
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
    畫面狀態
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
