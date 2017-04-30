# ActivityStarter
Android Library that provide simpler way to start the Activities with multiple arguments.

[![](https://jitpack.io/v/MarcinMoskala/ActivityStarter.svg)](https://jitpack.io/#MarcinMoskala/ActivityStarter)
[![codebeat badge](https://codebeat.co/badges/a1727670-96fe-4c89-9bdb-f1818a6dc066)](https://codebeat.co/projects/github-com-marcinmoskala-activitystarter)
[![Build Status](https://travis-ci.org/MarcinMoskala/ActivityStarter.svg?branch=master)](https://travis-ci.org/MarcinMoskala/ActivityStarter)
[![Stories in Ready](https://badge.waffle.io/MarcinMoskala/ActivityStarter.svg?label=ready&title=Ready)](http://waffle.io/MarcinMoskala/ActivityStarter)

Field and method binding for Android Activity arguments, which uses annotation processing to generate boilerplate code for you, and:
 * Eliminate all putExtra and getXXXExtra methods.
 * Allows you to forget about all keys that were used to pass agruments.
 * Support flags and Intent provide.

Full documentation is located [here](https://github.com/MarcinMoskala/ActivityStarter/wiki). Here is TOC:
* [Introdution](https://github.com/MarcinMoskala/ActivityStarter/wiki/Introdution)
* [Installation](https://github.com/MarcinMoskala/ActivityStarter/wiki/Installation)
* [Usage for Activities](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Activities)
* [Usage for Fragments](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Fragments)
* [Usage for Services](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Services)
* [Usage for BroadcastReceiver](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-BroadcastReceiver)
* [Usage of custom keys](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-of-custom-keys)
* [Optional annotation usage](https://github.com/MarcinMoskala/ActivityStarter/wiki/Optional-annotation)
* [NonSavable annotation usage](https://github.com/MarcinMoskala/ActivityStarter/wiki/NonSavable-annotation)
* [Activity startForResult usage](https://github.com/MarcinMoskala/ActivityStarter/wiki/Start-Activity-for-result)
* [Converters usage](https://github.com/MarcinMoskala/ActivityStarter/wiki/Converters-usage)
* [How does it really work?](https://github.com/MarcinMoskala/ActivityStarter/wiki/How-does-it-really-work?)

# Example

This is Activity with starter made in standard way: (it is just short. See [full example](https://github.com/MarcinMoskala/ActivityStarter/wiki/Activity-equivalent-example).)

```java
public class MainActivity extends BaseActivity {

    private static String NAME_KEY = "nameArg";
    private static String ID_KEY = "idArg";
    private static String GRADE_KEY = "gradeArg";
    private static String PASSING_KEY = "passingArg";

    public static void start(Context context, String name, int id, char grade, boolean passing) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(ID_KEY, id);
        intent.putExtra(GRADE_KEY, grade);
        intent.putExtra(PASSING_KEY, passing);
        context.startActivity(intent);
    }

    String name;
    int id;
    char grade;
    boolean passing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        name = intent.getStringExtra(NAME_KEY);
        id = intent.getIntExtra(ID_KEY, -1);
        grade = intent.getCharExtra(GRADE_KEY, 'a');
        passing = intent.getBooleanExtra(PASSING_KEY, false);
    }
}
```

With ActivityStarter all you need is:

```java
public class MainActivity extends BaseActivity {

    @Arg String name;
    @Arg int id;
    @Arg char grade;
    @Arg boolean passing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStarter.fill(this); // This can be located in BaseActivity, one for all activities

        //...
    }

    @Override // This is optional, only when we want to keep arguments changes in case of rotation etc.
    protected void onSaveInstanceState(Bundle outState) { // Also can be located in BaseActivity, one for all activities
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
```

And you start it nearly the same way:

```java
MainActivityStarter.start(context, name, id, grade, passing);
```

Simillar way you can take Intent or start activity with flags:

```java
MainActivityStarter.getIntent(context, name, id, grade, passing);
MainActivityStarter.startWithFlags(context, name, id, grade, passing, FLAG_ACTIVITY_SINGLE_TOP);
```

This can be applayed to [Activities](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Activities), [Fragments](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Fragments), [Services](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Services)
or [BroadcastReceiver](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-BroadcastReceiver). Arguments can also be [Optional](https://github.com/MarcinMoskala/ActivityStarter/wiki/Optional-annotation). 

## Installation

For Java project add in build.gradle file:

```groovy
dependencies {
    compile 'com.github.marcinmoskala.activitystarter:activitystarter:0.50'
    apt 'com.github.marcinmoskala.activitystarter:activitystarter-compiler:0.50'
}
```

For Kotlin project add in build.gradle file:

```groovy

kapt {
    generateStubs = true
}

dependencies {
    compile 'com.github.marcinmoskala.activitystarter:activitystarter:0.50'
    kapt 'com.github.marcinmoskala.activitystarter:activitystarter-compiler:0.50'
}
```

And while library is located on JitPack, remember to add on module build.gradle (unless you already have it):

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

More information on [Installation](https://github.com/MarcinMoskala/ActivityStarter/wiki/Installation) page.

## Converters

If you are using [Parceler library](https://github.com/johncarl81/parceler) and you want to pass this objects as an arguments then you can do it using ActivityStarter:
```
@Arg StudentParceler student;
```
Instruction how to allow it are [here](https://github.com/MarcinMoskala/ActivityStarter/wiki/Parceler-Arg-Converter-usage). If you want to define your own converters then read about it [here](https://github.com/MarcinMoskala/ActivityStarter/wiki/Converters-usage).

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

