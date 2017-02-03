package com.marcinmoskala.kotlinapp

import android.os.Bundle
import android.widget.TextView

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import kotlinx.android.synthetic.main.activity_data.*

@MakeActivityStarter
class StudentParcelableActivity : BaseActivity() {

    @Arg var student: StudentParcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        nameView.text = "Name: " + student!!.name
        idView.text = "Id: " + student!!.id
        gradeView.text = "Grade: " + student!!.grade
    }
}
