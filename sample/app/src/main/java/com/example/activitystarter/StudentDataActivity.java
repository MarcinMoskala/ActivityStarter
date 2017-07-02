package com.example.activitystarter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import activitystarter.Optional;
import butterknife.BindView;
import butterknife.ButterKnife;

@MakeActivityStarter
public class StudentDataActivity extends BaseActivity {

    private static int NO_ID = -1;
    public static int DEFAULT_ID = NO_ID;
    public static String DEFAULT_NAME = "No name provided";

    @Arg @Optional String name = DEFAULT_NAME;
    @Arg @Optional int id = DEFAULT_ID;
    @Arg char grade;
    @Arg boolean passing;

    @BindView(R.id.student_name) EditText studentNameView;
    @BindView(R.id.student_id) EditText studentIdView;
    @BindView(R.id.student_grade) EditText studentGradeView;
    @BindView(R.id.student_is_passing) Switch studentIsPassingView;
    @BindView(R.id.save_button) Button saveButton;
    @BindView(R.id.restore_button) Button restoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);

        fill();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = studentNameView.getText().toString();
                id = Integer.parseInt(studentIdView.getText().toString());
                grade = studentGradeView.getText().toString().charAt(0);
                passing = studentIsPassingView.isChecked();
            }
        });
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill();
            }
        });
    }

    private void fill() {
        studentNameView.setText(name);
        studentIdView.setText(""+id);
        studentGradeView.setText(""+grade);
        studentIsPassingView.setChecked(passing);
    }
}
