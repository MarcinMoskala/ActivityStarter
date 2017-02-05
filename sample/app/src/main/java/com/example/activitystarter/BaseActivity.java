package com.example.activitystarter;

import android.app.Activity;
import activitystarter.Arg;
import activitystarter.Optional;

public class BaseActivity extends Activity {
    @Arg String name;
    @Optional int id;
}