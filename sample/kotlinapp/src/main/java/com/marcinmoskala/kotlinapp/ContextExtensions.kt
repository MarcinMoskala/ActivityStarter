package com.marcinmoskala.kotlinapp

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

val Context.alarmManager: AlarmManager
    get() = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

val Context.notificationManager: NotificationManager
    get() = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

