package com.example.activitystarter;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class SecondActivity extends BaseActivity {

    @Arg String name;
    @Arg int id;
    @Arg char c;
    @Arg boolean canDoIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button b = (Button) findViewById(R.id.button);
        TextView textView = (TextView) findViewById(R.id.textView);

        if(name != null) {
            textView.setText(name + id);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityStarter.start(SecondActivity.this, "Some value");
                finish();
            }
        });
    }
}
