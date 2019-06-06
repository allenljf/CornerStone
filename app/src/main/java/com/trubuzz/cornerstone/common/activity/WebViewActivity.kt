package com.trubuzz.cornerstone.common.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.common.JsApi
import com.trubuzz.cornerstone.util.SPUtil
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.rl_left_icon
import kotlinx.android.synthetic.main.view_toolbar.view.rl_right_icon
import kotlinx.android.synthetic.main.view_toolbar.view.toolbar_title
import kotlinx.android.synthetic.main.view_toolbar_white.view.*


class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        toolbar.toolbar_title.text = intent.getStringExtra(Constant.ARG_TITLE)
        if(intent.getStringExtra(Constant.ARG_TITLE) == ""){
            toolbar.visibility = View.GONE
        }

        toolbar.rl_left_icon.setOnClickListener {
            finish()
        }

        toolbar.rl_right_icon.visibility = View.GONE

        webview.addJavascriptObject(JsApi(this), null)
        webview.webViewClient = WebViewClient()
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webview.webChromeClient = WebChromeClient()
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url.toString().startsWith("jsbridge://", true)) {
                    return true
                }
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (request.url.toString().startsWith("jsbridge://", true)) {
                    return true
                }
                return false
            }

//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//            }
        }


        webview.clearCache(true)
        webview.clearFormData()
        webview.clearMatches()
        webview.clearHistory()
        WebView.setWebContentsDebuggingEnabled(true)
        setCookie(intent.getStringExtra(Constant.ARG_URL))
        webview.loadUrl(intent.getStringExtra(Constant.ARG_URL))

//        webview.loadUrl("file:///android_asset/test.html")
    }

    private fun setCookie(url: String) {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setCookie(url, Constant.SESSIONID + "=" + SPUtil.session)
        cookieManager.setCookie(url, Constant.TOKEN + "=" + SPUtil.token)
        cookieManager.setCookie(url, Constant.LANG + "=" + App.instance().getString(R.string.lang))
        cookieManager.setCookie(
            url,
            Constant.API_VERSION + "=" + packageManager.getPackageInfo(packageName, 0).versionName
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync()
        } else {
            cookieManager.flush()
        }
    }
}