package com.marcinmoskala.kotlinapp

import android.os.Bundle
import android.widget.TextView

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.Optional
import kotlinx.android.synthetic.main.activity_data.*

@MakeActivityStarter
class StudentDataActivity : BaseActivity() {

    @Arg @Optional var name = "No name provided"
    @Arg @Optional var id = NO_ID
    @Arg var grade: Char = ' '
//    @Arg var isPassing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        nameView.text = "Name: " + name
        idView.text = "Id: " + id
        gradeView.text = "Grade: " + grade
//        isPassingView.text = "Passing status: " + isPassing
    }

    companion object {

        private val NO_ID = -1
    }
}
