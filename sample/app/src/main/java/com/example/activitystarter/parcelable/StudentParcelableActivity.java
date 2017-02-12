package com.example.activitystarter.parcelable;

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
public class StudentParcelableActivity extends BaseActivity {

    @Arg StudentParcelable student;

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

        studentNameView.setText(student.getName());
        studentIdView.setText(""+student.getId());
        studentGradeView.setText(""+student.getGrade());
        studentIsPassingView.setVisibility(View.GONE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = studentNameView.getText().toString();
                int id = studentIdView.getId();
                char grade = studentGradeView.getText().charAt(0);
                student = new StudentParcelable(id, name, grade);
            }
        });
    }
}
