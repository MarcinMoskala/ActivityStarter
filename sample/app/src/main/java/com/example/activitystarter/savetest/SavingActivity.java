package com.example.activitystarter.savetest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.activitystarter.R;

import activitystarter.ActivityStarter;
import activitystarter.Arg;

public class SavingActivity extends Activity {

    public static final int DEFAULT_I = -1;
    public static final String DEFAULT_STR = "AAA";
    public static final boolean DEFAULT_B = false;

    public static final int NEW_I = 100;
    public static final String NEW_STR = "BBB";
    public static final boolean NEW_B = true;

    @Arg(optional = true) public int i = DEFAULT_I;
    @Arg(optional = true) public String str = DEFAULT_STR;
    @Arg(optional = true) public boolean b = DEFAULT_B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this);
        setContentView(R.layout.activity_save_test);
        ((TextView) findViewById(R.id.i)).setText(""+i);
        ((TextView) findViewById(R.id.str)).setText(""+str);
        ((TextView) findViewById(R.id.b)).setText(""+b);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
