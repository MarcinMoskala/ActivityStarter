package com.example.activitystarter.serializable;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;

import com.example.activitystarter.BaseActivity;
import com.example.activitystarter.R;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.ButterKnife;

@MakeActivityStarter
public class StudentSerializableActivity extends BaseActivity {

    @Arg StudentSerializable student;

    @BindView(R.id.student_name) AutoCompleteTextView studentNameView;
    @BindView(R.id.student_id) AutoCompleteTextView studentIdView;
    @BindView(R.id.student_grade) AutoCompleteTextView studentGradeView;
    @BindView(R.id.student_is_passing) Switch studentIsPassingView;
    @BindView(R.id.save_button) Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);

        studentNameView.setText(student.name);
        studentIdView.setText(""+student.id);
        studentGradeView.setText(""+student.grade);
        studentIsPassingView.setChecked(student.passing);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student.name = studentNameView.getText().toString();
                student.id = studentIdView.getId();
                student.grade = studentGradeView.getText().charAt(0);
            }
        });
    }
}
