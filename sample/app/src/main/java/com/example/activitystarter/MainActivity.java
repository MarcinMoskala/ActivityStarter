package com.example.activitystarter;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends BaseActivity {

    Button showDataButton;
    Button showParcelableDataButton;
    Button showSerializableDataButton;
    Button showTabbedFragmentActivityButton;
    AutoCompleteTextView studentNameView;
    AutoCompleteTextView studentIdView;
    AutoCompleteTextView studentGradeView;
    TextInputLayout studentGradeLayoutView;
    Switch studentIsPassingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        setUpButtons();
        SomeServiceStarter.start(this, "Name", 10);
    }

    private void getViews() {
        showDataButton = (Button) findViewById(R.id.show_data_button);
        showParcelableDataButton = (Button) findViewById(R.id.show_parcelable_data_button);
        showSerializableDataButton = (Button) findViewById(R.id.show_serializable_data_button);
        showTabbedFragmentActivityButton = (Button) findViewById(R.id.show_tabbed_fragment_activity_button);
        studentNameView = (AutoCompleteTextView) findViewById(R.id.student_name);
        studentIdView = (AutoCompleteTextView) findViewById(R.id.student_id);
        studentGradeView = (AutoCompleteTextView) findViewById(R.id.student_grade);
        studentGradeLayoutView = (TextInputLayout) findViewById(R.id.student_grade_layout);
        studentIsPassingView = (Switch) findViewById(R.id.student_is_passing);
    }

    private void setUpButtons() {
        showDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailsActivity();
            }
        });
        showParcelableDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startParcelableActivity();
            }
        });
        showSerializableDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSerializableActivity();
            }
        });
        showTabbedFragmentActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabbedFragmentActivityStarter.start(MainActivity.this);
            }
        });
    }

    private void startSerializableActivity() {
        StudentSerializable student = new StudentSerializable(20, "Marcin Moskala", 'A', true);
        StudentSerializableActivityStarter.start(MainActivity.this, student);
    }

    private void startParcelableActivity() {
        StudentParcelable student = new StudentParcelable(10, "Marcin", 'A');
        StudentParcelableActivityStarter.start(MainActivity.this, student);
    }

    private void startDetailsActivity() {
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
                StudentDataActivityStarter.startWithFlags(MainActivity.this, id, grade, isPassing, FLAG_ACTIVITY_NEW_TASK);
            } else {
                StudentDataActivityStarter.startWithFlags(MainActivity.this, name, id, grade, isPassing, FLAG_ACTIVITY_NEW_TASK);
            }
        } catch (NumberFormatException e) {
            // Id is not valid
            if(name.trim().equals("")) {
                StudentDataActivityStarter.startWithFlags(MainActivity.this, grade, isPassing, FLAG_ACTIVITY_NEW_TASK);
            } else {
                StudentDataActivityStarter.startWithFlags(MainActivity.this, name, grade, isPassing, FLAG_ACTIVITY_NEW_TASK);
            }
        }
    }
}
