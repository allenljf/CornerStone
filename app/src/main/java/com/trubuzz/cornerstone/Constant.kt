package com.trubuzz.cornerstone

import android.content.Context

object Constant {
    const val APP_TAG = "CornerStone"
    const val APP_SOURCE = "cornerstoneAPP"
    const val MAIN_URL = "https://www.google.com.tw/"
    const val MARKET_URL = "https://www.google.com.tw/"
    const val NO_VALUE = "—"
    const val SIX_STAR = "******"

    var APP_BUILD_TYPE = "APP_BUILD_DAILY"
    const val APP_BUILD_DAILY = "APP_BUILD_DAILY"
    const val APP_BUILD_GLOBAL = "APP_BUILD_GLOBAL"
    const val APP_BUILD_CN = "APP_BUILD_CN"
    var APP_IS_STORE_RELEASE = false

    fun getDomain(context: Context): String {
        return when (APP_BUILD_TYPE) {
            APP_BUILD_DAILY -> context.getString(R.string.domain_url_daily)
            APP_BUILD_GLOBAL -> context.getString(R.string.domain_url_global)
            APP_BUILD_CN -> context.getString(R.string.domain_url_cn)
            else -> context.getString(R.string.domain_url_daily)
        }
    }

    fun getIntercomAppKey(context: Context): String {
        return when (APP_BUILD_TYPE) {
            APP_BUILD_DAILY -> context.getString(R.string.intercom_app_key_daily)
            APP_BUILD_GLOBAL -> context.getString(R.string.intercom_app_key_global)
            APP_BUILD_CN -> context.getString(R.string.intercom_app_key_cn)
            else -> context.getString(R.string.intercom_app_key_daily)
        }
    }

    fun getIntercomAppId(context: Context): String {
        return when (APP_BUILD_TYPE) {
            APP_BUILD_DAILY -> context.getString(R.string.intercom_app_id_daily)
            APP_BUILD_GLOBAL -> context.getString(R.string.intercom_app_id_global)
            APP_BUILD_CN -> context.getString(R.string.intercom_app_id_cn)
            else -> context.getString(R.string.intercom_app_id_daily)
        }
    }

    const val ARG_AREA_CODE = "ARG_AREA_CODE"
    const val AREA_CODE = 1
    const val LOGIN_TYPE_PHONE = "LOGIN_TYPE_PHONE"
    const val LOGIN_TYPE_EMAIL = "LOGIN_TYPE_EMAIL"
    const val AUTH_CODE_CHANCE: String = "authCode_error_chances_"
    const val ARG_URL = "ARG_URL"
    const val ARG_TITLE = "ARG_TITLE"
    const val SESSIONID = "sessionId"
    const val TOKEN = "token"
    const val API_VERSION = "version"
    const val LANG = "lang"
    const val SOURCE = "source"
    const val TRACE_ID = "traceId"

    const val ARG_FUND_ID = "ARG_FUND_ID"
    const val ARG_FUND_TYPE = "ARG_FUND_TYPE"

    const val ARG_OPEN_ACCOUNT = "openAccount"
    const val ARG_BUY_FUND = "buyFund"
    const val ACTION_PUSH_CommonNotice = "commonNotice"
}