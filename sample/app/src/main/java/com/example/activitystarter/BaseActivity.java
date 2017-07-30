package com.example.activitystarter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import activitystarter.ActivityStarter;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this);
    }
}
