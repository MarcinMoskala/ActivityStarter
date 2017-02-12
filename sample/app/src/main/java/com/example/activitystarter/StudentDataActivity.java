package com.example.activitystarter;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import activitystarter.Optional;
import butterknife.BindView;
import butterknife.ButterKnife;

@MakeActivityStarter
public class StudentDataActivity extends BaseActivity {

    private static int NO_ID = -1;

    @Arg @Optional String name = "No name provided";
    @Arg @Optional int id = NO_ID;
    @Arg char grade;
    @Arg boolean passing;

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

        studentNameView.setText(name);
        studentIdView.setText(""+id);
        studentGradeView.setText(""+grade);
        studentIsPassingView.setChecked(passing);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = studentNameView.getText().toString();
                id = Integer.parseInt(studentIdView.getText().toString());
                grade = studentGradeView.getText().toString().charAt(0);
                passing = studentIsPassingView.isChecked();
            }
        });
    }
}
