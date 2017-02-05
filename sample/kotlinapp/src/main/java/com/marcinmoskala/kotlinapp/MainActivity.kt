package com.marcinmoskala.kotlinapp

import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDataButton.setOnClickListener { startDetailsActivity() }
        showParcelableDataButton.setOnClickListener { startParcelableActivity() }
        showSerializableDataButton.setOnClickListener { startSerializableActivity() }
    }

    fun performClickOn(id: Int) {
        runOnUiThread {
            val button = findViewById(id) as Button
            button.performClick()
        }
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
                StudentDataActivityStarter.start(baseContext, id, grade, isPassing)
            } else {
                StudentDataActivityStarter.start(baseContext, name, id, grade, isPassing)
            }
        } catch (e: NumberFormatException) {
            // Id is not valid
            if (name.isNullOrBlank()) {
                StudentDataActivityStarter.start(baseContext, grade, isPassing)
            } else {
                StudentDataActivityStarter.start(baseContext, name, grade, isPassing)
            }
        }
    }

    private fun startParcelableActivity() {
        val student = StudentParcelable(10, "Marcin", 'A')
        StudentParcelableActivityStarter.start(baseContext, student)
    }

    private fun startSerializableActivity() {
        val student = StudentSerializable(20, "Marcin Moskala", 'A', true)
        StudentSerializableActivityStarter.start(baseContext, student)
    }
}
