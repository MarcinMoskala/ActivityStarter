package com.example.activitystarter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class ThirdActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button b = (Button) findViewById(R.id.button);
        TextView textView = (TextView) findViewById(R.id.textView);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityStarter.start(ThirdActivity.this, "Some value");
                finish();
            }
        });
    }
}
