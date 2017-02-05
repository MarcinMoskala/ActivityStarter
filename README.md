# ActivityStarter
Android Library that provide simpler way to start the Activities with multiple arguments.

[![](https://jitpack.io/v/MarcinMoskala/ActivityStarter.svg)](https://jitpack.io/#MarcinMoskala/ActivityStarter)
[![codebeat badge](https://codebeat.co/badges/a1727670-96fe-4c89-9bdb-f1818a6dc066)](https://codebeat.co/projects/github-com-marcinmoskala-activitystarter)
[![Build Status](https://travis-ci.org/MarcinMoskala/ActivityStarter.svg?branch=master)](https://travis-ci.org/MarcinMoskala/ActivityStarter)

Field and method binding for Android Activity arguments, which uses annotation processing to generate boilerplate code for you, and:
 * Eliminate all putExtra and getXXXExtra methods.
 * Allows you to forget about all keys that were used to pass agruments.
 * Support flags and Intent provide.

Let's look at the example. This is Activity made in standard way:

```java
public class MainActivity extends BaseActivity {

    @UiThread
    public static void start(Context context, String name, int id, char grade, boolean isPassing) {
        Intent intent = new Intent(context, StudentDataActivity.class);
        intent.putExtra("nameArg", name);
        intent.putExtra("idArg", id);
        intent.putExtra("gradeArg", grade);
        intent.putExtra("isPassingArg", isPassing);
        context.startActivity(intent);
    }

    String name;
    int id;
    char grade;
    boolean isPassing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        name = intent.getStringExtra("nameArg");
        id = intent.getIntExtra("idArg", -1);
        grade = intent.getCharExtra("gradeArg", 'a');
        isPassing = intent.getBooleanExtra("isPassingArg", false);
    }
}
```

And we start it by:

```java
MainActivity.start(context, name, id, grade, isPassing);
```

With ActivityStarter all you need is:

```java
public class MainActivity extends BaseActivity {

    @Arg String name;
    @Arg int id;
    @Arg char grade;
    @Arg boolean isPassing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStarter.start(this); // This can be located in BaseActivity, one for all activities

        //...
    }
}
```

And you start it nearly the same way:

```java
MainActivityStarter.start(context, name, id, grade, isPassing);
```

Simillar way you can take Intent or start activity with flags:

```java
MainActivityStarter.getIntent(context, name, id, grade, isPassing);
MainActivityStarter.startWithFlags(context, name, id, grade, isPassing, FLAG_ACTIVITY_SINGLE_TOP);
```

What is more, if you need to make different variants, without some arguments, then you can just use @Optional annotation.

```java
@Arg @Optional String name = "No name provided";
@Arg @Optional int id = NO_ID;
@Arg char grade;
@Arg boolean isPassing;
```

And use it:

```java
MainActivityStarter.start(context, id, grade, isPassing);
MainActivityStarter.start(context, name, grade, isPassing);
MainActivityStarter.start(context, id, name, grade, isPassing);
```

Download
--------

For Java project add in build.gradle file:

```groovy
dependencies {
    compile 'com.github.MarcinMoskala.ActivityStarter:activitystarter:0.03'
    annotationProcessor 'com.github.MarcinMoskala.ActivityStarter:activitystarter-compiler:0.03'
}
```

For Kotlin project add in build.gradle file:

```groovy

kapt {
    generateStubs = true
}

dependencies {
    compile 'com.github.MarcinMoskala.ActivityStarter:activitystarter:0.03'
    kapt 'com.github.MarcinMoskala.ActivityStarter:activitystarter-compiler:0.03'
}
```

TODO
-------

- [ ] Annotation-specified key names
- [X] Fragment support
- [ ] Service support
- [ ] Mutliple @Optional arguments of the same type one after another naming conflict
- [ ] Webpage
- [ ] Kotlin delegate instad of annotation (+ lazy access)
 
Feel invited to contribute :)
 
License
-------

    Copyright 2017 Marcin Moskała

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

