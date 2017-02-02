package com.example.activitystarter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import activitystarter.Arg;

public class MainActivity extends BaseActivity {

    @Arg String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button b = (Button) findViewById(R.id.button);
        TextView textView = (TextView) findViewById(R.id.textView);
        if(name != null) {
            textView.setText(name);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivityStarter.start(MainActivity.this, "Some other value :D", 3, 'k', true);
                finish();
            }
        });
    }
}
