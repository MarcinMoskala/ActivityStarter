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
import com.example.activitystarter.serializable.StudentSerializable;
import com.example.activitystarter.serializable.StudentSerializableActivityStarter;

import java.util.ArrayList;

import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllTypesActivity extends BaseActivity {

    @Arg String str;
    @Arg int i;
    @Arg long l;
    @Arg float f;
    @Arg boolean bool;
    @Arg double d;
    @Arg char c;
    @Arg byte b;
    @Arg short s;
    @Arg CharSequence cs;

    @Arg String[] stra;
    @Arg int[] ia;
    @Arg long[] la;
    @Arg float[] fa;
    @Arg boolean[] boola;
    @Arg double[] da;
    @Arg char[] ca;
    @Arg byte[] ba;
    @Arg short[] sa;
    @Arg CharSequence[] csa;

    @Arg ArrayList<Integer> intList;
    @Arg ArrayList<String> strList;
    @Arg ArrayList<CharSequence> csList;

    @Arg StudentParcelable sp;
    @Arg StudentSerializable ss;

    @Arg StudentParcelable[] spArray;
    @Arg ArrayList<StudentParcelable> spList;
}
