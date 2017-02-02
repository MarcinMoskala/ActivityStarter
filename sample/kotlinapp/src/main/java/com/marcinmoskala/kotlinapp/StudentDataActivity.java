package com.marcinmoskala.kotlinapp;

import android.os.Bundle;
import android.widget.TextView;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import activitystarter.Optional;

@MakeActivityStarter
public class StudentDataActivity extends BaseActivity {

    private static int NO_ID = -1;

    @Arg @Optional String name = "No name provided";
    @Arg @Optional int id = NO_ID;
    @Arg char grade;
    @Arg boolean isPassing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        TextView nameView = (TextView) findViewById(R.id.name_view);
        TextView idView = (TextView) findViewById(R.id.id_view);
        TextView gradeView = (TextView) findViewById(R.id.grade_view);
        TextView isPassingView = (TextView) findViewById(R.id.is_passing_view);

        nameView.setText("Name: "+name);
        idView.setText("Id: "+id);
        gradeView.setText("Grade: "+grade);
        isPassingView.setText("Passing status: "+isPassing);
    }
}
