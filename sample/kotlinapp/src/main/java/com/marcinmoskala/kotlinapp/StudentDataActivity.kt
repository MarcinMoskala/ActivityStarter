package com.marcinmoskala.kotlinapp

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import android.annotation.SuppressLint
import android.os.Bundle
import com.marcinmoskala.activitystarter.argExtra
import kotlinx.android.synthetic.main.activity_data.*

@MakeActivityStarter
class StudentDataActivity : BaseActivity() {

    @get:Arg(optional = true) var name: String by argExtra(defaultName)
    @get:Arg(optional = true) var id: Int by argExtra(defaultId)
    @get:Arg var grade: Char  by argExtra()
    @get:Arg var passing: Boolean  by argExtra()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        nameView.text = "Name: $name"
        idView.text = "Id: $id"
        gradeView.text = "Grade: $grade"
        isPassingView.text = "Passing status: $passing"
    }

    companion object {
        private const val NO_ID = -1
        const val defaultName = "No name provided"
        const val defaultId = NO_ID
    }
}
