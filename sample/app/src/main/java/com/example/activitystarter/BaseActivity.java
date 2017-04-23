package com.example.activitystarter;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.marcinmoskala.activitystarterparcelerargwrapper.ParcelarArgWrapper;

import activitystarter.ActivityStarter;
import activitystarter.ActivityStarterConfig;

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
