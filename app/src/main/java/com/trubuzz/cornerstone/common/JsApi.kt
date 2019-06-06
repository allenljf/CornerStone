package com.trubuzz.cornerstone.common

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.webkit.JavascriptInterface
import androidx.annotation.Keep
import com.google.gson.Gson
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.widget.CustomAlertDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import wendu.dsbridge.CompletionHandler


class JsApi(context: Activity) {
    @Keep
    class JsApiTitleMessage(
        val message: String,
        val title: String
    )

    @Keep
    data class JsApiParams(
        val event: String,
        val parameters: MutableMap<String, String>
    )

    @Keep
    class JsApiWXShare(
        val title: String,
        val content: String,
        val webURL: String
    )

    var mContext = context

    //同步API
    @JavascriptInterface
    fun testSyn(msg: Any): String {
        return msg.toString() + "syn call"
    }

    //异步API
    @JavascriptInterface
    fun testAsyn(msg: Any, handler: CompletionHandler<String>) {
        handler.complete(msg.toString() + " asyn call")
    }

    //Back
    @JavascriptInterface
    fun back(msg: Any, handler: CompletionHandler<String>) {
        mContext.finish()
    }

    //Toast Message
    @JavascriptInterface
    fun toast(msg: Any, handler: CompletionHandler<String>) {
        try {
            val gson = Gson()
            val result = gson.fromJson(msg.toString(), JsApiTitleMessage::class.java)

            var hasTitle = (null != result?.title)
            var hasMessage = (null != result?.message)

            when {
                hasTitle && hasMessage -> App.instance().toast(result.title + "\n\n" + result.message)
                hasTitle -> App.instance().toast(result.title)
                hasMessage -> App.instance().toast(result.message)
            }
        } catch (e: Exception) {

        }
    }

    //Alert
    @JavascriptInterface
    fun alert(msg: Any, handler: CompletionHandler<String>) {
        try {
            val gson = Gson()
            val result = gson.fromJson(msg.toString(), JsApiTitleMessage::class.java)
            var customAlertDialog = CustomAlertDialog(mContext, result.title, result.message)
            customAlertDialog.show()
        } catch (e: Exception) {

        }
    }

    //Dialog
    @JavascriptInterface
    fun dialog(msg: Any, handler: CompletionHandler<String>) {
        try {
            val gson = Gson()
            val result = gson.fromJson(msg.toString(), JsApiTitleMessage::class.java)
            var title = result.title
            var message = result.message

            AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ModalOK, DialogInterface.OnClickListener { dialog, which ->
                    handler.complete(msg.toString() + " asyn call")
                })
                .setNegativeButton(R.string.ModalCancel, null)
                .show()
        } catch (e: Exception) {

        }
    }
}
