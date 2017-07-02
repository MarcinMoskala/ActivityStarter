package com.example.activitystarter.serializable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.activitystarter.BaseActivity;
import com.example.activitystarter.R;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.ButterKnife;

@MakeActivityStarter
public class StudentSerializableActivity extends BaseActivity {

    public @Arg StudentSerializable student;

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
                student.name = studentNameView.getText().toString();
                student.id = studentIdView.getId();
                student.grade = studentGradeView.getText().charAt(0);
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
        studentNameView.setText(student.name);
        studentIdView.setText(""+student.id);
        studentGradeView.setText(""+student.grade);
        studentIsPassingView.setChecked(student.passing);
    }
}
