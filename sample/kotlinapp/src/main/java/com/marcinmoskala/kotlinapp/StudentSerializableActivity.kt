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
        val nameView = findViewById(R.id.nameView) as TextView
        val idView = findViewById(R.id.idView) as TextView
        val gradeView = findViewById(R.id.gradeView) as TextView
        val isPassingView = findViewById(R.id.isPassingView) as TextView

        nameView.text = "Name: ${student.name}"
        idView.text = "Id: ${student.id}"
        gradeView.text = "Grade: ${student.grade}"
        isPassingView.text = "Passing: ${student.passing}"
    }
}
