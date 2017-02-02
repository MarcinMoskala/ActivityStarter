package com.example.activitystarter;

import android.os.Bundle;
import android.widget.TextView;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class StudentParcelableActivity extends BaseActivity {

    @Arg StudentParcelable student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        TextView nameView = (TextView) findViewById(R.id.name_view);
        TextView idView = (TextView) findViewById(R.id.id_view);
        TextView gradeView = (TextView) findViewById(R.id.grade_view);

        nameView.setText("Name: "+student.getName());
        idView.setText("Id: "+student.getId());
        gradeView.setText("Grade: "+student.getGrade());
    }
}
