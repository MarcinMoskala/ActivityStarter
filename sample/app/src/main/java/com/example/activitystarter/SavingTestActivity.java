package com.example.activitystarter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import androidx.appcompat.app.AppCompatActivity;

public class SavingTestActivity extends AppCompatActivity
{

    @Arg(optional = true)
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_test);
        ActivityStarter.fill(this, savedInstanceState);

        final Button incrementButton = findViewById(R.id.increment);
        final TextView valueDisplay = findViewById(R.id.value);

        valueDisplay.setText("" + value);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value++;
                valueDisplay.setText("" + value);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
