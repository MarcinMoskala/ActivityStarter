package com.marcinmoskala.kotlinapp.savetest

import activitystarter.ActivityStarter
import activitystarter.Arg
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.marcinmoskala.activitystarter.argExtra
import com.marcinmoskala.kotlinapp.R
import kotlinx.android.synthetic.main.activity_save_test.*

class NonSavingActivity : Activity() {

    @get:Arg(optional = true) var i by argExtra(DEFAULT_I)
    @get:Arg(optional = true) var str by argExtra(DEFAULT_STR)
    @get:Arg(optional = true) var b by argExtra(DEFAULT_B)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this)
        setContentView(R.layout.activity_save_test)
        iView.text = "$i"
        strView.text = str
        bView.text = "$b"
    }

    companion object {
        val DEFAULT_I = -1
        val DEFAULT_STR = "AAA"
        val DEFAULT_B = false

        val NEW_I = 100
        val NEW_STR = "BBB"
        val NEW_B = true
    }
}
