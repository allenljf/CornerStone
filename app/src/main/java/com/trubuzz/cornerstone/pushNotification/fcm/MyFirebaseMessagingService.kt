package com.trubuzz.cornerstone.pushNotification.fcm

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.trubuzz.cornerstone.App
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.pushNotification.model.PushNotificationModel
import com.trubuzz.cornerstone.util.SystemUtil
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private val TAG = "FCM"
        fun register(context: Context?) {
            if (context == null) return
            val i = Intent(context, MyFirebaseMessagingService::class.java)
            context!!.startService(i)
            sendUserDeviceInfo()
        }

        fun sendUserDeviceInfo() {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Old token: " + task.result?.token)

                    val body = HashMap<String, String>()
                    body["hwid"] = SystemUtil.getAndroidId(App.instance())
                    body["type"] = "android"
                    body["pushToken"] = task.result?.token!!
                    body["deviceSpec"] =
                        SystemUtil.deviceBrand + "_" + SystemUtil.systemModel + "_" + SystemUtil.systemVersion
                    body["pushType"] = "fcm"

                    PushNotificationModel.sendUserDeviceInfo(body).observeForever {
                        it?.data?.let {
                            Log.d("FCM", "register success")
                        }
                        it?.serverError?.let {
                            Log.d("FCM", "serverError " + it.errorCode)
                        }
                        it?.networkError?.let {
                            Log.d("FCM", "serverError" + it.localizedMessage)
                        }
                    }
                }
            }
        }
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage!!.from!!)
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            var json = JSONObject()
            json.put("msg", remoteMessage.data?.get("msg"))

            if (json != null) {
                val intent = Intent(getString(R.string.action_push_received))
                intent.putExtra("json", json.toString())
                sendBroadcast(intent)
            }
        }
    }
}

