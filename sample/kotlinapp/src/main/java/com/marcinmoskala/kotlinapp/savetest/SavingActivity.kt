package com.marcinmoskala.kotlinapp.savetest

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

import com.marcinmoskala.kotlinapp.R

import activitystarter.ActivityStarter
import activitystarter.Arg
import com.marcinmoskala.activitystarter.argExtra
import kotlinx.android.synthetic.main.activity_save_test.*

class SavingActivity : Activity() {

    @get:Arg(optional = true) var i by argExtra(NonSavingActivity.DEFAULT_I)
    @get:Arg(optional = true) var str by argExtra(NonSavingActivity.DEFAULT_STR)
    @get:Arg(optional = true) var b by argExtra(NonSavingActivity.DEFAULT_B)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this)
        setContentView(R.layout.activity_save_test)
        iView.text = "$i"
        strView.text = str
        bView.text = "$b"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ActivityStarter.save(this)
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
