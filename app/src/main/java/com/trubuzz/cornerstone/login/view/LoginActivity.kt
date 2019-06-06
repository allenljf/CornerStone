package com.trubuzz.cornerstone.login.view

import android.content.Intent
import android.os.CountDownTimer
import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.base.BaseActivity
import com.trubuzz.cornerstone.common.activity.WebViewActivity
import com.trubuzz.cornerstone.login.vm.LoginViewModel
import com.trubuzz.cornerstone.main.view.activity.MainActivity
import com.trubuzz.cornerstone.util.FormatUtils
import com.trubuzz.cornerstone.util.SPUtil
import com.trubuzz.cornerstone.util.SystemUtil
import com.trubuzz.cornerstone.util.net.NetworkType
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : BaseActivity<LoginViewModel>() {
    var phoneAccount: String = ""
    var emailAccount: String = ""
    var isPhoneLogin: Boolean = true
    var isPhoneCounting: Boolean = false
    var isEmailCounting: Boolean = false
    var isPhoneCountType: Boolean = true
    var isPhoneSend: Boolean = false
    var isEmailSend: Boolean = false
    lateinit var phoneTimer: CountDownTimer
    lateinit var emailTimer: CountDownTimer
    var isConfirmChecked: Boolean = true

    override fun initView() {
        setToolbar(false)
        setCustomLayout(R.layout.activity_login)

        //登入
        btn_login.onClick {
            loginOrRegister()
        }

        //獲取驗證碼
        btn_get_veri_code.onClick {
            sendAuthCode()
        }

        //用戶協議按鈕
        iv_tick.setOnClickListener {
            if (isConfirmChecked) {
                isConfirmChecked = false
                iv_tick.setImageResource(R.drawable.confirm_uncheck)
            } else {
                isConfirmChecked = true
                iv_tick.setImageResource(R.drawable.confirm_check)
            }
            changeLoginBtnUI()
        }

        //用戶協議連結
        tv_user_contract.setOnClickListener {
            startActivity<WebViewActivity>(
                Constant.ARG_URL to getString(R.string.privacy_policy_url),
                Constant.ARG_TITLE to getString(R.string.SettingPrivacyPolicyTitle)
            )
        }

        //選擇區碼
        tv_area_code.text = "+852"
        var lang = Locale.getDefault().toString()
        if (lang.indexOf("zh_CN") != -1) {
            tv_area_code.text = "+86"
        }
        if (lang.indexOf("zh_HK") != -1) {
            tv_area_code.text = "+852"
        }
        if (lang.indexOf("zh_TW") != -1) {
            tv_area_code.text = "+886"
        }
        if (lang.indexOf("zh_MO") != -1) {
            tv_area_code.text = "+853"
        }

        //清除帳號
        iv_clean_account.setOnClickListener {
            et_account.setText("")
        }

        //清除驗證碼
        iv_clean_pwd.setOnClickListener {
            et_verify_code.setText("")
        }

        //切換登入方式
        btn_login_type.setOnClickListener {
            isPhoneLogin = !isPhoneLogin
            if (isPhoneLogin) {
                tv_area_code.visibility = View.VISIBLE
                btn_login_type.text = getString(R.string.EmailLoginText)
                et_account.setText(phoneAccount)
                et_account.hint = getString(R.string.Phone)
                et_account.inputType = InputType.TYPE_CLASS_NUMBER
                tv_wrong_account.text = getString(R.string.PhoneFormatError)
                isPhoneCountType = true
            } else {
                tv_area_code.visibility = View.GONE
                btn_login_type.text = getString(R.string.PhoneLoginText)
                et_account.setText(emailAccount)
                et_account.hint = getString(R.string.Email)
                et_account.inputType = InputType.TYPE_CLASS_TEXT
                tv_wrong_account.text = getString(R.string.EmailFormatError)
                isPhoneCountType = false
            }
            enableVeriCode()
        }

        et_account.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                tv_wrong_account.visibility = View.INVISIBLE
            }
        }

        et_account.inputType = InputType.TYPE_CLASS_NUMBER
        et_account.addTextChangedListener(object : SystemUtil.SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeLoginBtnUI()
                if (s.toString().length == 0) {
                    iv_clean_account.visibility = View.GONE
                } else {
                    iv_clean_account.visibility = View.VISIBLE
                }

                if (isPhoneLogin) {
                    phoneAccount = s.toString()
                } else {
                    emailAccount = s.toString()
                }
            }
        })

        et_verify_code.addTextChangedListener(object : SystemUtil.SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeLoginBtnUI()
                if (s.toString().length == 0) {
                    iv_clean_pwd.visibility = View.GONE
                } else {
                    iv_clean_pwd.visibility = View.VISIBLE
                }
            }

        })

        with(btn_get_veri_code) {
            isClickable = false
            textColor = ContextCompat.getColor(context, R.color.gray_B4B4B4)
        }

        //自動填上之前的帳號
        if (SPUtil.loginType == Constant.LOGIN_TYPE_PHONE) {
            if (SPUtil.areaCode != "") {
                tv_area_code.setText(SPUtil.areaCode)
            }
        } else if (SPUtil.loginType == Constant.LOGIN_TYPE_EMAIL) {
            isPhoneLogin = false
            tv_area_code.visibility = View.GONE
            btn_login_type.text = getString(R.string.PhoneLoginText)
            et_account.setText(emailAccount)
            et_account.hint = getString(R.string.Email)
            et_account.inputType = InputType.TYPE_CLASS_TEXT
            tv_wrong_account.text = getString(R.string.EmailFormatError)
            isPhoneCountType = false
        } else {
            tv_area_code.setText(SPUtil.areaCode)
            et_account.setText("")
        }
        et_account.setText(SPUtil.account)

        tv_area_code.setOnClickListener {
            startActivityForResult<AreaCodeActivity>(1,
                "ARG_CODE" to tv_area_code.text)
        }
    }

    override fun getData() {

    }

    override fun onNetConnected(networkType: NetworkType) {
        isNetworkOk = true
    }

    override fun onNetDisconnected() {
        isNetworkOk = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.AREA_CODE -> {
                tv_area_code.text = data?.getStringExtra(Constant.ARG_AREA_CODE)
                changeLoginBtnUI()
            }
        }
    }

    //獲取驗證碼
    private fun sendAuthCode() {
        if (!isNetworkOk) {
            toast(getString(R.string.NetworkDisconnect))
            return
        }

        val body = HashMap<String, String>()
        body["type"] = "login"
        body["userName"] = et_account.text.toString()
        if (isPhoneLogin) {
            body["areaCode"] = tv_area_code.text.toString()
            if (isPhoneCounting){
                return
            }
        }else{
            if (isEmailCounting) {
                return
            }
        }

        //开启60s倒计时
        et_account.isEnabled = false
        if (isPhoneLogin) {
            phoneTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    isPhoneSend = true
                    if (isPhoneCountType) {
                        isPhoneCounting = true
                        btn_get_veri_code.text = getString(R.string.Resend) + " ${millisUntilFinished / 1000}s"
                        disableVeriCode()
                    }
                }

                override fun onFinish() {
                    isPhoneCounting = false
                    enableVeriCode()
                }

            }.start()
        } else {
            emailTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    isEmailSend = true
                    if (!isPhoneCountType) {
                        isEmailCounting = true
                        btn_get_veri_code.text = getString(R.string.Resend) + " ${millisUntilFinished / 1000}s"
                        disableVeriCode()
                    }
                }

                override fun onFinish() {
                    isEmailCounting = false
                    enableVeriCode()
                }

            }.start()
        }

        mViewModel?.sendAuthCode(body).observe(this, Observer {

        })
    }

    private fun disableVeriCode() {
        btn_get_veri_code.textColor = ContextCompat.getColor(this@LoginActivity, R.color.gray_B4B4B4)
        btn_get_veri_code.setPadding(0, 0, dip(28), dip(2))
    }

    private fun enableVeriCode() {
        if (isPhoneLogin) {
            if (isPhoneCounting) {
                return
            }
            if (isPhoneSend) {
                btn_get_veri_code.text = getString(R.string.Resend)
                btn_get_veri_code.setPadding(0, 0, dip(8.5f), dip(2))
            } else {
                btn_get_veri_code.text = getString(R.string.SendCode)
            }
        } else {
            if (isEmailCounting) {
                return
            }
            if (isEmailSend) {
                btn_get_veri_code.text = getString(R.string.Resend)
                btn_get_veri_code.setPadding(0, 0, dip(8.5f), dip(2))
            } else {
                btn_get_veri_code.text = getString(R.string.SendCode)
            }
        }

        btn_get_veri_code.textColor = ContextCompat.getColor(mContext, R.color.gold_B9A88F)
        et_account.isEnabled = true
        changeLoginBtnUI()
    }

    fun changeLoginBtnUI() {
        var isAccountOk = false
        var isVeriCodeOk = false

        if (isPhoneLogin) {
            if (FormatUtils.checkPhone(et_account.text.toString(), tv_area_code.text.toString())) {
                isAccountOk = true
            }
        } else {
            if (FormatUtils.checkEmail(et_account.text.toString())) {
                isAccountOk = true
            }
        }

        if(FormatUtils.checkVeriCode(et_verify_code.text.toString())){
            isVeriCodeOk = true
        }

        if (isAccountOk) {
            tv_wrong_account.visibility = View.INVISIBLE
            with(btn_get_veri_code) {
                isClickable = true
                if (isPhoneLogin) {
                    if (!isPhoneCounting) {
                        textColor = ContextCompat.getColor(mContext, R.color.gold_B9A88F)
                    }
                } else {
                    if (!isEmailCounting) {
                        textColor = ContextCompat.getColor(mContext, R.color.gold_B9A88F)
                    }
                }
            }
        } else {
            tv_wrong_account.visibility = View.VISIBLE
            with(btn_get_veri_code) {
                isClickable = false
                textColor = ContextCompat.getColor(context, R.color.gray_B4B4B4)
            }
        }

        if (et_account.text.toString().isEmpty()) {
            tv_wrong_account.visibility = View.INVISIBLE
        }

        if(isVeriCodeOk){
            tv_wrong_code.visibility = View.INVISIBLE
        }else{
            tv_wrong_code.visibility = View.VISIBLE
        }

        if (et_verify_code.text.toString().isEmpty()) {
            tv_wrong_code.visibility = View.INVISIBLE
        }

        if (isAccountOk && isVeriCodeOk && isConfirmChecked) {
            with(btn_login) {
                isClickable = true
                background = getDrawable(R.drawable.round_3dp_gold)
                onClick {
                    loginOrRegister()
                }
            }
        } else {
            with(btn_login) {
                isClickable = false
                background = getDrawable(R.drawable.round_3dp_gold_50)
                onClick {

                }
            }
        }
    }

    //登入
    private fun loginOrRegister() {
        if (!isConfirmChecked) {
            toast(getString(R.string.NoAgreeContract))
            return
        }

        if (!isNetworkOk) {
            toast(getString(R.string.NetworkDisconnect))
            return
        }

        var deviceUUID = SPUtil.deviceUUID
        if (deviceUUID.isEmpty()) {
            deviceUUID = UUID.randomUUID().toString()
            SPUtil.deviceUUID = deviceUUID
        }

        val body = HashMap<String, String>()
        body["deviceId"] = deviceUUID
        body["deviceType"] = "android"
        body["userName"] = et_account.text.toString()
        body["authCode"] = et_verify_code.text.toString()
        body["steps"] = "login,register,login"
        if (isPhoneLogin) {
            body["areaCode"] = tv_area_code.text.toString()
        }

        mViewModel?.loginOrRegister(body).observe(this, Observer {
            it?.data?.let { data ->

                val session = data?.session
                val token = data?.token
                val userInfo = data?.userInfo
                var phone = ""
                var email = ""
                var account = ""

                if (userInfo?.phone != null) {
                    account = userInfo?.phone
                    phone = account
                } else {
                    account = userInfo?.email
                    email = account
                }

                SPUtil.session = session
                SPUtil.token = token
                SPUtil.phone = phone
                SPUtil.email = email
                SPUtil.account = account

                if (isPhoneLogin) {
                    SPUtil.loginType = Constant.LOGIN_TYPE_PHONE
                    SPUtil.areaCode = "+" + getAreaCode()
                }else{
                    SPUtil.loginType = Constant.LOGIN_TYPE_EMAIL
                }

                startActivity<MainActivity>()
                finish()
            }

            it?.serverError?.let { serverError ->
                showServerErrorToast(serverError)
            }

            it?.networkError?.let { networkError ->
                showNetworkErrorToast(networkError)
            }
        })
    }

    private fun getAreaCode(): String {
        return tv_area_code.text.toString().replace("+", "")
    }
}
