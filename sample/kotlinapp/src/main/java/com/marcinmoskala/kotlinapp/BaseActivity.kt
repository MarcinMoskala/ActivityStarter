package com.marcinmoskala.kotlinapp

import activitystarter.ActivityStarter
import activitystarter.ActivityStarterConfig
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marcinmoskala.activitystarterparcelerargconverter.ParcelarArgConverter

@ActivityStarterConfig(converters = arrayOf(ParcelarArgConverter::class))
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStarter.fill(this, savedInstanceState)
    }
}
