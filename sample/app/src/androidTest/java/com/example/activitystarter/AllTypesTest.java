package com.example.activitystarter;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.activitystarter.parcelable.StudentParcelable;
import com.example.activitystarter.serializable.StudentSerializable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AllTypesTest {

    @Rule
    public ActivityTestRule<AllTypesActivity> activityTestRule = new ActivityTestRule<>(AllTypesActivity.class, false, false);

    private static String string = "SomeString";
    private static int i = 1000;
    private static long l = 1000;
    private static float f = 1000;
    private static boolean bool = true;
    private static double d = 1000;
    private static char c = 'Z';
    private static byte b = 10;
    private static short s = 100;
    private static CharSequence cs = "CharSequence";

    private static String[] stringArray = {"SomeString"};
    private static int[] iArray = {1000};
    private static long[] lArray = {1000};
    private static float[] fArray = {1000};
    private static boolean[] boolArray = {true};
    private static double[] dArray = {1000};
    private static char[] cArray = {'Z'};
    private static byte[] bArray = {10};
    private static short[] sArray = {100};
    private static CharSequence[] csArray = {"CharSequence"};

    private static ArrayList<Integer> intList = new ArrayList<>();
    private static ArrayList<String> strList = new ArrayList<>();
    private static ArrayList<CharSequence> csList = new ArrayList<>();

    private static StudentParcelable sp = new StudentParcelable(1, "AAA", 'A');
    private static StudentSerializable ss = new StudentSerializable(2, "Marcin", 'A', true);
    private static ArrayList<StudentParcelable> spList = new ArrayList<>();

    static {
        intList.add(1);
        intList.add(2);
        intList.add(3);
        strList.add("A");
        strList.add("B");
        strList.add("C");
        strList.add("D");
        csList.add("ALALA");
        csList.add("BABABA");
        csList.add("KAKAKA");
        spList.add(sp);
    }

    @Test
    public void startTest() throws InterruptedException {
        Intent intent = getIntent();
        activityTestRule.launchActivity(intent);
    }

    @Test
    public void valuesTest() throws InterruptedException {
        Intent intent = getIntent();
        activityTestRule.launchActivity(intent);
        AllTypesActivity a = activityTestRule.getActivity();
        assertEquals(string, a.str);
        assertEquals(i, a.i);
        assertEquals(l, a.l);
        assertEquals(f, a.f, 0.01);
        assertEquals(bool, a.bool);
        assertEquals(d, a.d, 0.01);
        assertEquals(c, a.c);
        assertEquals(b, a.b);
        assertEquals(s, a.s);
        assertArrayEquals(stringArray, a.stra);
        assertArrayEquals(iArray, a.ia);
        assertArrayEquals(lArray, a.la);
        assertArrayEquals(fArray, a.fa, 0.01F);
        assertArrayEquals(boolArray, a.boola);
        assertArrayEquals(dArray, a.da, 0.01);
        assertArrayEquals(cArray, a.ca);
        assertArrayEquals(bArray, a.ba);
        assertArrayEquals(sArray, a.sa);
        assertArrayEquals(csArray, a.csa);
        assertEquals(intList, a.intList);
        assertEquals(strList, a.strList);
        assertEquals(csList, a.csList);
        assertEquals(sp, a.sp);
        assertEquals(ss, a.ss);
        assertEquals(spList, a.spList);
    }

    private Intent getIntent() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return AllTypesActivityStarter.getIntent(context,
                string, i, l, f, bool, d, c, b, s, cs,
                stringArray, iArray, lArray, fArray, boolArray, dArray, cArray, bArray, sArray, csArray,
                intList, strList, csList, sp, ss, spList
        );
    }
}
