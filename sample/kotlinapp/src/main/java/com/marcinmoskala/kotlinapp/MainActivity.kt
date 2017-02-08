package com.marcinmoskala.kotlinapp

import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.os.Bundle
import com.marcinmoskala.kotlinapp.notification.BookingNotificationPublisherStarter
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime

class MainActivity : BaseActivity() {

    val parcelableStudent = StudentParcelable(10, "Marcin", 'A')
    val serializableStudent = StudentSerializable(20, "Marcin Moskala", 'A', true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDataButton.setOnClickListener { startDetailsActivity() }
        showParcelableDataButton.setOnClickListener { startParcelableActivity() }
        showSerializableDataButton.setOnClickListener { startSerializableActivity() }
        startNotification()
    }

    private fun startNotification() {
        val intent = BookingNotificationPublisherStarter.getIntent(this, 10, DateTime.now().toString("HH:mm"))
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_UPDATE_CURRENT)
        alarmManager.setExact(RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent)
    }

    private fun startDetailsActivity() {
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

    private fun startParcelableActivity() {
        StudentParcelableActivityStarter.start(this, parcelableStudent)
    }

    private fun startSerializableActivity() {
        StudentSerializableActivityStarter.start(this, serializableStudent)
    }
}
