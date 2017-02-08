package com.marcinmoskala.kotlinapp.notification

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.marcinmoskala.kotlinapp.R
import com.marcinmoskala.kotlinapp.notificationManager

class NotificationPublisher : BroadcastReceiver() {

    @Arg var id: Int = -1
    @Arg lateinit var time: String

    override fun onReceive(context: Context, intent: Intent) {
        ActivityStarter.fill(this, intent)
        val notification = createSimpleNotification(context)
        context.notificationManager.notify(id, notification)
    }

    fun createSimpleNotification(context: Context) = Notification.Builder(context)
            .setContentTitle(getTextTitle(id))
            .setContentText(getTextSubtitle(time))
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()!!

    companion object {
        fun getTextTitle(id: Int) = "I am notification $id"
        fun getTextSubtitle(time: String) = "It is $time"
    }
}