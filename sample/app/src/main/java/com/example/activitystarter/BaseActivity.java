package com.example.activitystarter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

import activitystarter.ActivityStarter;
import activitystarter.Arg;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this);
    }
}
