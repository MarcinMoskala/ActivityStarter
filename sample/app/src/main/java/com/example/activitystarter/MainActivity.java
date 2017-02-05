package com.example.activitystarter;
import android.app.Activity;
import activitystarter.Arg;
import activitystarter.Optional;

public class MainActivity extends Activity {
    @Arg @Optional String name;
    @Arg @Optional int id;
}