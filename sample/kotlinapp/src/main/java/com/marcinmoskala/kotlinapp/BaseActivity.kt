package com.marcinmoskala.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import activitystarter.ActivityStarter

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this)
    }
}
