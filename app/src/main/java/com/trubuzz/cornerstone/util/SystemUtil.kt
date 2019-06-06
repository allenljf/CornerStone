package com.trubuzz.cornerstone.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import java.security.MessageDigest
import java.util.*


object SystemUtil {
    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val systemModel: String
        get() = android.os.Build.MODEL

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    val systemLanguage: String
        get() = Locale.getDefault().language

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    val systemLanguageList: Array<Locale>
        get() = Locale.getAvailableLocales()

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemVersion: String
        get() = android.os.Build.VERSION.RELEASE

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val deviceBrand: String
        get() = android.os.Build.BRAND

    fun getAndroidId(context: Context): String {
        return Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getHashKey(context: Context): String {
        var key = ""

        try {
            var info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(
                    Base64.encode(
                        md.digest(),
                        0
                    )
                )//String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("TEST", key)

            }
        } catch (err: Exception) {
            Log.e("TEST", err.toString())
        }
        return key
    }

    //複製文字
    fun copyToClipboard(context: Context?, text: String?) {
        if (context == null || text == null) {
            return
        }

        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        val clipData = ClipData.newPlainText(null, text)
        clipboardManager.primaryClip = clipData
    }

    /**
     * 检测手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true-安装，false-未安装
     */
    fun isAppInstall(context: Context, appPackageName: String): Boolean {
        val packageManager = context.packageManager// 获取packagemanager
        val pinfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                if (appPackageName == pn) {
                    return true
                }
            }
        }
        return false
    }

    abstract class SimpleTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }

    fun initRefreshHeader(context:Context) {
        ClassicsHeader.REFRESH_HEADER_PULLING = context.getString(R.string.header_pulling)
        ClassicsHeader.REFRESH_HEADER_REFRESHING = context.getString(R.string.header_refreshing)
        ClassicsHeader.REFRESH_HEADER_LOADING = context.getString(R.string.header_loading)
        ClassicsHeader.REFRESH_HEADER_RELEASE = context.getString(R.string.header_release)
        ClassicsHeader.REFRESH_HEADER_FINISH = context.getString(R.string.header_finish)
        ClassicsHeader.REFRESH_HEADER_FAILED = context.getString(R.string.header_failed)

        ClassicsFooter.REFRESH_FOOTER_PULLING = context.getString(R.string.footer_pulling)
        ClassicsFooter.REFRESH_FOOTER_RELEASE = context.getString(R.string.footer_release)
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = context.getString(R.string.footer_refreshing)
        ClassicsFooter.REFRESH_FOOTER_LOADING = context.getString(R.string.footer_loading)
        ClassicsFooter.REFRESH_FOOTER_FINISH = context.getString(R.string.footer_finish)
        ClassicsFooter.REFRESH_FOOTER_FAILED = context.getString(R.string.footer_failed)
        ClassicsFooter.REFRESH_FOOTER_NOTHING = context.getString(R.string.footer_nothing)
    }
}
