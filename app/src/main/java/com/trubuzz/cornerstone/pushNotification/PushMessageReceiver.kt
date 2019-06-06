package com.trubuzz.cornerstone.pushNotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.pushNotification.model.CommonNotice
import org.json.JSONException
import org.json.JSONObject

//FCM
//https://fcm.googleapis.com/fcm/send
//Content-Type
//application/json
//Authorization
//key=AAAA9rzJs_Q:APA91bG2a5YLHb1Q4jL27250amDwmGbPc3NLgAgX1P8Euv553orXGNhZwAg3kcKQKCkQqcnOxH54rrS7yvLhiEQhkkoLYTpRm0HueGzy0Qm8Nh5XljJCOB5IJYQXo8RRxpXbdCWH-w_6

//數據格式FCM
//{
//    "to":"c794mYYOvNs:APA91bEGpMW8258_ocgLYtrhFTPwPHoX1eqMgVlVH4yhIdGJGsO8uVYFVA3_J0GnKxaXdCiVReKLyr4J6Ji9tjEC2WclA0j6Kvba0bnhGT2XyO_3IRpv3SP7OIlLRanN-OacYmOUBVKP",
//    "data":{
//        "msg": {
//        "noticeType": "commonNotice",
//        "title":"投資組合推薦",
//        "body": "Dave向你推荐了一大波投资组合"
//        }
//    },
//    "traceId":"9ee10767-b198-42b2-92de-ef1ca50c0d90"
//}

//數據格式FCM
//    "msg": {
//        "noticeType": "commonNotice",
//        "title":"投資組合推薦",
//        "body": "Dave向你推荐了一大波投资组合"
//    }

class PushMessageReceiver : BroadcastReceiver() {
    companion object {
        var TAG = "PushMessageReceiver"
        val ARG_JSON = "json"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            var gson = Gson()
            val json = JSONObject(intent.getStringExtra(ARG_JSON))
            val msg = json.get("msg").toString()
            val jsonMsg = JSONObject(msg)
            val type = jsonMsg.get("noticeType")

            when (type) {
                Constant.ACTION_PUSH_CommonNotice -> showCommonNotice(gson?.fromJson(msg, CommonNotice::class.java))
            }
        } catch (ignored: JSONException) {

        }
    }

    private fun showCommonNotice(data: CommonNotice) {
        showPushNotification(Constant.ACTION_PUSH_CommonNotice, data.title, data.body)
    }


    private fun showPushNotification(channel: String, title: String, message: String) {
        NotificationUtils(App.instance()).sendNotification(title, message)
    }
}

