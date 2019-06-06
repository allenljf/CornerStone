package com.trubuzz.cornerstone.pushNotification.tencentXG

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tencent.android.tpush.*
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.pushNotification.model.PushNotificationModel
import com.trubuzz.cornerstone.util.SystemUtil
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class XGPushReceiver : XGPushBaseReceiver() {

    // 通知展示
    override fun onNotifactionShowedResult(
        context: Context,
        notifiShowedRlt: XGPushShowedResult
    ) {

    }

    //反注册的回调
    override fun onUnregisterResult(context: Context, errorCode: Int) {

    }

    //设置tag的回调
    override fun onSetTagResult(context: Context, errorCode: Int, tagName: String) {

    }

    //删除tag的回调
    override fun onDeleteTagResult(context: Context, errorCode: Int, tagName: String) {

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    override fun onNotifactionClickedResult(
        context: Context,
        message: XGPushClickedResult
    ) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    //注册的回调
    override fun onRegisterResult(
        context: Context, errorCode: Int,
        message: XGPushRegisterResult
    ) {
    }

    // 消息透传的回调
    override fun onTextMessage(context: Context, message: XGPushTextMessage) {
        //发送Push Notification
        val content = message.content
        if (content != null && content.length != 0) {
            try {
                val json = JSONObject(content)
                if (json != null) {
                    val intent = Intent(context.getString(R.string.action_push_received))
                    intent.putExtra("json", json.toString())
                    context.sendBroadcast(intent)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    companion object {

        fun initXg(context: Context) {
            XGPushManager.registerPush(context, object : XGIOperateCallback {

                override fun onSuccess(data: Any, flag: Int) {
                    Log.d("XINGE", "xg token " + data.toString())
                    val body = HashMap<String, String>()
                    body["hwid"] = SystemUtil.getAndroidId(context)
                    body["type"] = "android"
                    body["pushToken"] = data.toString()
                    body["deviceSpec"] =
                        SystemUtil.deviceBrand + "_" + SystemUtil.systemModel + "_" + SystemUtil.systemVersion
                    body["pushType"] = "xinge"

                    PushNotificationModel.sendUserDeviceInfo(body).observeForever {
                        it?.data?.let {
                            Log.d("XINGE", "register success")
                        }
                        it?.serverError?.let {
                            Log.d("XINGE", "serverError " + it.errorCode)
                        }
                        it?.networkError?.let {
                            Log.d("XINGE", "serverError" + it.localizedMessage)
                        }
                    }
                }

                override fun onFail(data: Any, errCode: Int, msg: String) {
                    Log.d("XINGE", "xg onFail " + msg)
                }
            })
        }
    }

}
