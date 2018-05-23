package com.marcinmoskala.kotlinapp

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import android.os.Bundle
import android.widget.TextView
import com.marcinmoskala.activitystarter.argExtra

@MakeActivityStarter
class StudentSerializableActivity : BaseActivity() {

    @get:Arg val student: StudentSerializable  by argExtra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        val nameView = findViewById<TextView>(R.id.nameView)
        val idView = findViewById<TextView>(R.id.idView)
        val gradeView = findViewById<TextView>(R.id.gradeView)
        val isPassingView = findViewById<TextView>(R.id.isPassingView)

        nameView.text = "Name: ${student?.name}"
        idView.text = "Id: ${student?.id}"
        gradeView.text = "Grade: ${student?.grade}"
    }
}
