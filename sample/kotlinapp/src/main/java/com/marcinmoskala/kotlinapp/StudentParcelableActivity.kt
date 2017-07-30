package com.marcinmoskala.kotlinapp

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import android.os.Bundle
import com.marcinmoskala.activitystarter.argExtra
import kotlinx.android.synthetic.main.activity_data.*

@MakeActivityStarter
class StudentParcelableActivity : BaseActivity() {

    @get:Arg var student: StudentParcelable by argExtra()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        nameView.text = "Name: " + student.name
        idView.text = "Id: " + student.id
        gradeView.text = "Grade: " + student.grade
    }
}
