package com.marcinmoskala.kotlinapp

import activitystarter.Arg
import activitystarter.MakeActivityStarter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

@MakeActivityStarter
class SecondActivity : AppCompatActivity() {

//    @Arg var s: String = "koko"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

    }
}
