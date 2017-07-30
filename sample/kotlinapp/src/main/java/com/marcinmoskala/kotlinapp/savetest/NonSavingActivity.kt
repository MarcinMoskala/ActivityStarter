package com.marcinmoskala.kotlinapp.savetest

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

import com.marcinmoskala.kotlinapp.R

import activitystarter.ActivityStarter
import activitystarter.Arg
import com.marcinmoskala.activitystarter.argExtra

class NonSavingActivity : Activity() {

    @get:Arg(optional = true) var i by argExtra(DEFAULT_I)
    @get:Arg(optional = true) var str = DEFAULT_STR
    @get:Arg(optional = true) var b = DEFAULT_B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this)
        setContentView(R.layout.activity_save_test)
        (findViewById(R.id.i) as TextView).text = "" + i
        (findViewById(R.id.str) as TextView).text = "" + str
        (findViewById(R.id.b) as TextView).text = "" + b
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
