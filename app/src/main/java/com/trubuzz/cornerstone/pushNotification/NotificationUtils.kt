package com.trubuzz.cornerstone.pushNotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.main.view.activity.SplashActivity

class NotificationUtils(private val context: Context) {

    private var notificationManager: NotificationManager? = null
    private var builder: NotificationCompat.Builder? = null
    private var builder26: Notification.Builder? = null
    private val notifyId = 1
    internal var pendingIntent: PendingIntent
    private val manager: NotificationManager
        get() {
            if (notificationManager == null) {
                notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager!!
        }
    internal var time = 0

    init {

        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        channel.enableVibration(true)

        manager.createNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getChannelNotification(title: String, content: String): Notification.Builder? {
        createNotificationChannel()
        builder26 = Notification.Builder(context.applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        return builder26
    }

    fun getNotification_25(title: String, content: String): NotificationCompat.Builder? {
        builder = NotificationCompat.Builder(context.applicationContext)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        return builder
    }

    fun sendNotification(title: String, content: String) {
        val notification: Notification
        if (Build.VERSION.SDK_INT >= 26) {
            notification = getChannelNotification(title, content)!!.build()
        } else {
            notification = getNotification_25(title, content)!!.build()
        }
        manager.notify(notifyId, notification)
    }

    companion object {
        val channelId = "channel_1"
        val channelName = "channel_name_1"
    }
}