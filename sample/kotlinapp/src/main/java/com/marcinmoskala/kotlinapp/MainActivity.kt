package com.marcinmoskala.kotlinapp

import activitystarter.MakeActivityStarter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

@MakeActivityStarter
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { SecondActivityStarter.start(this) }
    }
}