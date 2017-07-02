package com.example.activitystarter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activitystarter.R;

import activitystarter.ActivityStarter;
import activitystarter.Arg;

public class TabbedPlaceholderFragment extends Fragment {

    @Arg int sectionNumber;

    public TabbedPlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
        ActivityStarter.fill(this, savedInstanceState);
        final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++sectionNumber;
                textView.setText(getString(R.string.section_format, sectionNumber));
            }
        });
        textView.setText(getString(R.string.section_format, sectionNumber));
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
