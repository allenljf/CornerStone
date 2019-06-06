package com.trubuzz.cornerstone

import android.app.Application
import android.content.IntentFilter
import android.content.res.Configuration
import com.google.firebase.FirebaseApp
import com.trubuzz.cornerstone.pushNotification.tencentXG.XGPushReceiver
import com.trubuzz.cornerstone.util.SPUtil
import com.trubuzz.cornerstone.util.SystemUtil
import com.trubuzz.cornerstone.pushNotification.PushMessageReceiver
import com.trubuzz.cornerstone.pushNotification.fcm.MyFirebaseMessagingService

class App : Application() {
    companion object {
        var instance: Application? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        SPUtil.init(this)
        SystemUtil.initRefreshHeader(this)
        initPush()
    }

    private fun isInPlay() = BuildConfig.FLAVOR == "play"

    private fun initPush() {
        val intentFilterPush = IntentFilter(getString(R.string.action_push_received))
        registerReceiver(PushMessageReceiver(), intentFilterPush)

        if (isInPlay()) {
            FirebaseApp.initializeApp(this)
            MyFirebaseMessagingService.register(this)
        } else {
            XGPushReceiver.initXg(this)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        SystemUtil.initRefreshHeader(this)
        if (isInPlay()) {
            MyFirebaseMessagingService.sendUserDeviceInfo()
        } else {
            XGPushReceiver.initXg(this)
        }
    }
}