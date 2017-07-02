package com.marcinmoskala.kotlinapp

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import activitystarter.Optional
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_data.*

@MakeActivityStarter
class StudentDataActivity : BaseActivity() {

    @Arg @Optional var name: String = defaultName
    @Arg @Optional var id: Int = defaultId
    @Arg var grade: Char = ' '
    @Arg var passing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        nameView.text = "Name: " + name
        idView.text = "Id: " + id
        gradeView.text = "Grade: " + grade
        isPassingView.text = "Passing status: " + passing
    }

    companion object {
        private val NO_ID = -1
        val defaultName = "No name provided"
        val defaultId = NO_ID
    }
}
