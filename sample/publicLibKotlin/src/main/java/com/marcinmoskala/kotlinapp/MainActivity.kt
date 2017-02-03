package com.marcinmoskala.kotlinapp

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDataButton.setOnClickListener {
            val gradeString = studentGradeView.text.toString()
            if (gradeString.length != 1) {
                studentGradeLayoutView.error = "You must provide some grade"
                return@setOnClickListener
            } else {
                studentGradeLayoutView.error = null
            }

            val name = studentNameView.text.toString()
            val idString = studentIdView.text.toString()
            val grade = gradeString[0]
            val isPassing = studentIsPassingView.isChecked

            try {
                val id = Integer.parseInt(idString)
                if (name.trim { it <= ' ' } == "") {
                    StudentDataActivityStarter.start(baseContext, id, grade, isPassing)
                } else {
                    StudentDataActivityStarter.start(baseContext, name, id, grade, isPassing)
                }
            } catch (e: NumberFormatException) {
                // Id is not valid
                if (name.trim { it <= ' ' } == "") {
                    StudentDataActivityStarter.start(baseContext, grade, isPassing)
                } else {
                    StudentDataActivityStarter.start(baseContext, name, grade, isPassing)
                }
            }
        }
        showParcelableDataButton.setOnClickListener {
            val student = StudentParcelable(10, "Marcin", 'A')
            StudentParcelableActivityStarter.start(baseContext, student)
        }
        showSerializableDataButton.setOnClickListener {
            val student = StudentSerializable(20, "Marcin Moskala", 'A', true)
            StudentSerializableActivityStarter.start(baseContext, student)
        }
    }
}
