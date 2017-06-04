package com.example.activitystarter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marcinmoskala.activitystarterparcelerargconverter.ParcelerArgConverter;

import activitystarter.ActivityStarter;
import activitystarter.ActivityStarterConfig;

@ActivityStarterConfig(converters = { ParcelerArgConverter.class })
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
