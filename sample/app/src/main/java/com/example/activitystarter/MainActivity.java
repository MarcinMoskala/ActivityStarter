package com.example.activitystarter;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Switch;

import com.example.activitystarter.fragment.TabbedFragmentActivityStarter;
import com.example.activitystarter.parcelable.StudentParcelable;
import com.example.activitystarter.parcelable.StudentParcelableActivityStarter;
import com.example.activitystarter.parceler.StudentParceler;
import com.example.activitystarter.parceler.StudentParcelerActivityStarter;
import com.example.activitystarter.serializable.StudentSerializable;
import com.example.activitystarter.serializable.StudentSerializableActivityStarter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.show_data_button) Button showDataButton;
    @BindView(R.id.show_parcelable_data_button) Button showParcelableDataButton;
    @BindView(R.id.show_parceler_data_button) Button showParcelaberDataButton;
    @BindView(R.id.show_serializable_data_button) Button showSerializableDataButton;
    @BindView(R.id.show_tabbed_fragment_activity_button) Button showTabbedFragmentActivityButton;

    @BindView(R.id.student_name) AutoCompleteTextView studentNameView;
    @BindView(R.id.student_id) AutoCompleteTextView studentIdView;
    @BindView(R.id.student_grade) AutoCompleteTextView studentGradeView;
    @BindView(R.id.student_grade_layout) TextInputLayout studentGradeLayoutView;
    @BindView(R.id.student_is_passing) Switch studentIsPassingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpButtons();
        SomeServiceStarter.start(this, "Name", 10);
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
        showParcelaberDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentParcelerActivityStarter.start(MainActivity.this, new StudentParceler(5, "Marcin", 'A'));
            }
        });
    }

    private void startSerializableActivity() {
        StudentSerializable student = new StudentSerializable(20, "Marcin Moskala", 'A', true);
        StudentSerializableActivityStarter.start(this, student);
    }

    private void startParcelableActivity() {
        StudentParcelable student = new StudentParcelable(10, "Marcin", 'A');
        StudentParcelableActivityStarter.start(this, student);
    }

    private void startDetailsActivity() {
        String gradeString = studentGradeView.getText().toString();
        if (gradeString.length() != 1) {
            studentGradeLayoutView.setError("You must provide some grade");
            return;
        } else {
            studentGradeLayoutView.setError(null);
        }

        String name = studentNameView.getText().toString();
        String idString = studentIdView.getText().toString();
        char grade = gradeString.charAt(0);
        boolean passing = studentIsPassingView.isChecked();

        try {
            int id = Integer.parseInt(idString);
            if (name.trim().equals("")) {
                StudentDataActivityStarter.start(this, id, grade, passing);
            } else {
                StudentDataActivityStarter.start(this, name, id, grade, passing);
            }
        } catch (NumberFormatException e) {
            // Id is not valid
            if (name.trim().equals("")) {
                StudentDataActivityStarter.start(this, grade, passing);
            } else {
                StudentDataActivityStarter.start(this, name, grade, passing);
            }
        }
    }
}
