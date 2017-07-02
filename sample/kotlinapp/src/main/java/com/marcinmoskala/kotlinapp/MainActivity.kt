package com.marcinmoskala.kotlinapp

import activitystarter.MakeActivityStarter
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Bundle
import com.marcinmoskala.kotlinapp.notification.NotificationPublisherStarter
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime

@MakeActivityStarter
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDataButton.setOnClickListener { startDetailsActivity() }
        showParcelableDataButton.setOnClickListener { startParcelableActivity() }
        showSerializableDataButton.setOnClickListener { startSerializableActivity() }
        startNotification()
    }

    private fun startNotification() {
        val intent = NotificationPublisherStarter.getIntent(this, notificationId, notificationTime)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_UPDATE_CURRENT)
        alarmManager.set(RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent)
    }

    fun startDetailsActivity() {
        val gradeString = studentGradeView.text.toString()
        if (gradeString.length != 1) {
            studentGradeLayoutView.error = "You must provide some grade"
            return
        } else {
            studentGradeLayoutView.error = null
        }

        val name = studentNameView.text.toString()
        val idString = studentIdView.text.toString()
        val grade = gradeString[0]
        val isPassing = studentIsPassingView.isChecked

        try {
            val id = idString.toInt()
            if (name.isNullOrBlank()) {
                StudentDataActivityStarter.start(this, id, grade, isPassing)
            } else {
                StudentDataActivityStarter.start(this, name, id, grade, isPassing)
            }
        } catch (e: NumberFormatException) {
            // Id is not valid
            if (name.isNullOrBlank()) {
                StudentDataActivityStarter.start(this, grade, isPassing)
            } else {
                StudentDataActivityStarter.start(this, name, grade, isPassing)
            }
        }
    }

    fun startParcelableActivity() {
        StudentParcelableActivityStarter.start(this, parcelableStudent)
    }

    fun startSerializableActivity() {
        StudentSerializableActivityStarter.start(this, serializableStudent)
    }

    companion object {
        val notificationId = 10
        val notificationTime = DateTime.now().toString("HH:mm")
        val parcelableStudent = StudentParcelable(10, "Marcin", 'A')
        val serializableStudent = StudentSerializable(20, "Marcin Moskala", 'A', true)
    }
}
