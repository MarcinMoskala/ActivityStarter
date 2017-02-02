package com.marcinmoskala.kotlinapp;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showDataButton = (Button) findViewById(R.id.show_data_button);
        Button showParcelableDataButton = (Button) findViewById(R.id.show_parcelable_data_button);
        Button showSerializableDataButton = (Button) findViewById(R.id.show_serializable_data_button);
        final AutoCompleteTextView studentNameView = (AutoCompleteTextView) findViewById(R.id.student_name);
        final AutoCompleteTextView studentIdView = (AutoCompleteTextView) findViewById(R.id.student_id);
        final AutoCompleteTextView studentGradeView = (AutoCompleteTextView) findViewById(R.id.student_grade);
        final TextInputLayout studentGradeLayoutView = (TextInputLayout) findViewById(R.id.student_grade_layout);
        final Switch studentIsPassingView = (Switch) findViewById(R.id.student_is_passing);

        showDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gradeString = studentGradeView.getText().toString();
                if(gradeString.length() != 1) {
                    studentGradeLayoutView.setError("You must provide some grade");
                    return;
                } else {
                    studentGradeLayoutView.setError(null);
                }

                String name = studentNameView.getText().toString();
                String idString = studentIdView.getText().toString();
                char grade = gradeString.charAt(0);
                boolean isPassing = studentIsPassingView.isChecked();

                try {
                    int id = Integer.parseInt(idString);
                    if(name.trim().equals("")) {
                        StudentDataActivityStarter.start(getBaseContext(), id, grade, isPassing);
                    } else {
                        StudentDataActivityStarter.start(getBaseContext(), name, id, grade, isPassing);
                    }
                } catch (NumberFormatException e) {
                    // Id is not valid
                    if(name.trim().equals("")) {
                        StudentDataActivityStarter.start(getBaseContext(), grade, isPassing);
                    } else {
                        StudentDataActivityStarter.start(getBaseContext(), name, grade, isPassing);
                    }
                }
            }
        });
        showParcelableDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentParcelable student = new StudentParcelable(10, "Marcin", 'A');
                StudentParcelableActivityStarter.start(getBaseContext(), student);
            }
        });
        showSerializableDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentSerializable student = new StudentSerializable(20, "Marcin Moskala", 'A', true);
                StudentSerializableActivityStarter.start(getBaseContext(), student);
            }
        });
    }
}
