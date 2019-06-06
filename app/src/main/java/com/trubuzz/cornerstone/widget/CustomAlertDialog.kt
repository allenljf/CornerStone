package com.trubuzz.cornerstone.widget

import android.app.Dialog
import android.content.Context
import android.widget.RelativeLayout
import android.widget.TextView
import com.trubuzz.cornerstone.R
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick


class CustomAlertDialog(context: Context,title: String, message:String) : RelativeLayout(context) {
    open var mDialog: Dialog? = null
    private var mContext: Context? = context
    private var title = title
    private var message = message

    fun show(): CustomAlertDialog {
        mDialog = Dialog(mContext)
        mDialog?.setContentView(R.layout.view_alert_dialog)

        mDialog?.find<TextView>(R.id.tv_title)?.text = title
        mDialog?.find<TextView>(R.id.tv_message)?.text = message
        mDialog?.find<TextView>(R.id.tv_btn)?.onClick {
            mDialog?.dismiss()
        }

        mDialog?.setCanceledOnTouchOutside(false)
        mDialog?.show()

        return this
    }
}