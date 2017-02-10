# ActivityStarter
Android Library that provide simpler way to start the Activities with multiple arguments.

[![](https://jitpack.io/v/MarcinMoskala/ActivityStarter.svg)](https://jitpack.io/#MarcinMoskala/ActivityStarter)
[![codebeat badge](https://codebeat.co/badges/a1727670-96fe-4c89-9bdb-f1818a6dc066)](https://codebeat.co/projects/github-com-marcinmoskala-activitystarter)
[![Build Status](https://travis-ci.org/MarcinMoskala/ActivityStarter.svg?branch=master)](https://travis-ci.org/MarcinMoskala/ActivityStarter)

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
* [Optional annotation usage](https://github.com/MarcinMoskala/ActivityStarter/wiki/Optional-annotation)
* [How does it really work?](https://github.com/MarcinMoskala/ActivityStarter/wiki/How-does-it-really-work)

# Example

This is Activity with starter made in standard way:

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

This can be applayed to [Activities](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Activities), [Fragments](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Fragments), [Services](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Services)
or [BroadcastReceiver](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-BroadcastReceiver). Arguments can also be [Optional](https://github.com/MarcinMoskala/ActivityStarter/wiki/Optional-annotation). 

## Download

For Java project add in build.gradle file:

```groovy
dependencies {
    compile 'com.github.marcinmoskala.activitystarter:activitystarter:0.11'
    annotationProcessor 'com.github.marcinmoskala.activitystarter:activitystarter-compiler:0.11'
}
```

For Kotlin project add in build.gradle file:

```groovy

kapt {
    generateStubs = true
}

dependencies {
    compile 'com.github.marcinmoskala.activitystarter:activitystarter:0.11'
    kapt 'com.github.marcinmoskala.activitystarter:activitystarter-compiler:0.11'
}
```

And while library is located on JitPack, remember to add on module build.gradle (unless you already have it):

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

More information on [Installation](https://github.com/MarcinMoskala/ActivityStarter/wiki/Installation) page.

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

