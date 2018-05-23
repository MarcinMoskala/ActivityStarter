package com.marcinmoskala.kotlinapp.notification

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.marcinmoskala.kotlinapp.R
import com.marcinmoskala.kotlinapp.notificationManager

class NotificationPublisher : BroadcastReceiver() {

    @Arg var id: Int = -1
    @Arg lateinit var time: String

    override fun onReceive(context: Context, intent: Intent) {
        ActivityStarter.fill(this, intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "activitystarter", NotificationManager.IMPORTANCE_HIGH)
            context.notificationManager.createNotificationChannel(channel)
        }

        val notification = createSimpleNotification(context)
        context.notificationManager.notify(id, notification)
    }

    fun createSimpleNotification(context: Context): Notification
    {
        val builder = Notification.Builder(context)
            .setContentTitle(getTextTitle(id))
            .setContentText(getTextSubtitle(time))
            .setSmallIcon(R.mipmap.ic_launcher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        }

        return builder.build()
    }

    companion object {
        fun getTextTitle(id: Int) = "I am notification $id"
        fun getTextSubtitle(time: String) = "It is $time"

        const val NOTIFICATION_CHANNEL_ID = "activitystarter-notification-channel"
    }
}
