package com.example.activitystarter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class ForthActivity extends BaseActivity {

    @Arg Serializable serializable;
    @Arg Parcelable parcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button b = (Button) findViewById(R.id.button);
        TextView textView = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        if(intent.hasExtra("KOKO")) intent.getStringExtra("KOKO");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityStarter.start(ForthActivity.this, "Some value");
                finish();
            }
        });
    }
}
