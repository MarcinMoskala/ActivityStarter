Warning: Library is not maintained anymore. If you want to take care of this library, propose it via Pull Request. It needs adjustmensts for newer versions of Android and Gradle.

# ActivityStarter
Android Library that provides simpler way to start the Activities with multiple arguments.

[![](https://jitpack.io/v/MarcinMoskala/ActivityStarter.svg)](https://jitpack.io/#MarcinMoskala/ActivityStarter)
[![codebeat badge](https://codebeat.co/badges/a1727670-96fe-4c89-9bdb-f1818a6dc066)](https://codebeat.co/projects/github-com-marcinmoskala-activitystarter)
[![Build Status](https://travis-ci.org/MarcinMoskala/ActivityStarter.svg?branch=master)](https://travis-ci.org/MarcinMoskala/ActivityStarter)
[![Stories in Ready](https://badge.waffle.io/MarcinMoskala/ActivityStarter.svg?label=ready&title=Ready)](http://waffle.io/MarcinMoskala/ActivityStarter)
[![Join the chat at https://gitter.im/ActivityStarter/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ActivityStarter/Lobby)
[![Analytics](https://ga-beacon.appspot.com/UA-92159206-2/main-page?pixel)](https://github.com/MarcinMoskala/ActivityStarter)
[![Analytics](https://ga-beacon.appspot.com/UA-92159206-7/main-page?pixel)](https://github.com/MarcinMoskala/ActivityStarter)

Library bindes fields to Actity, Fragment, Service or Receiver arguments and generates simple starters. Thanks to that you can: 
 * Eliminate all putExtra and getXXXExtra methods
 * Forget about all keys that were used to pass arguments (unless you want to define custom onces)
 * Start liking starting flags and intent creation
 
Media:
* [Way to ActivityStarter](https://medium.com/@marcinmoskala/way-to-activitystarter-5f9ea239faa8)

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

To stay up-to-date with news about library [![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/fold_left.svg?style=social&label=Follow%20%40marcinmoskala)](https://twitter.com/marcinmoskala?ref_src=twsrc%5Etfw)

# Example

With ActivityStarter, to pass arguments to Activity, Fragment, Service or BroadcastReceiver, all you need is `@Arg` annotation before parameters that needs to be passed:

```java
public class MainActivity extends BaseActivity {

    @Arg String name;
    @Arg int id;
    @Arg char grade;
    @Arg boolean passing;
}
```

And `ActivityStarter.fill(this, savedInstanceState);` in BaseActivity:

```java
class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityStarter.fill(this, savedInstanceState);
    }

    @Override // This is optional, only when we want to keep arguments changes in case of rotation etc.
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActivityStarter.save(this, outState);
    }
}
```

Then, you can start Activity using generated starter:

```java
MainActivityStarter.start(context, name, id, grade, passing);
```

Similar way, you can take Intent or start activity with flags:

```java
MainActivityStarter.getIntent(context, name, id, grade, passing);
MainActivityStarter.startWithFlags(context, name, id, grade, passing, FLAG_ACTIVITY_SINGLE_TOP);
```

Arguments can be passed to [Activities](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Activities), [Fragments](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Fragments), [Services](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-Services)
or [BroadcastReceiver](https://github.com/MarcinMoskala/ActivityStarter/wiki/Usage-for-BroadcastReceiver). Arguments can also be [Optional](https://github.com/MarcinMoskala/ActivityStarter/wiki/Optional-annotation). 

## Optional

You can make optional arguments:

```java
public class MainActivity extends BaseActivity {

    @Arg(optional = true) String name;
    @Arg(optional = true) long id = -1;
}
```

Then additional generators with not all arguments will be created

```java
MainActivityStarter.start(context);
MainActivityStarter.start(context, name);
MainActivityStarter.start(context, id);
MainActivityStarter.start(context, name, id);
```

Further reading [here](https://github.com/MarcinMoskala/ActivityStarter/wiki/Optional-argument).

## Kotlin

ActivityStarter is supporting Kotlin to allow properties that are not-null and both read-write or read-only:

```kotlin
class StudentDataActivity : BaseActivity() {

    @get:Arg(optional = true) var name: String by argExtra(defaultName)
    @get:Arg(optional = true) val id: Int by argExtra(defaultId)
    @get:Arg var grade: Char  by argExtra()
    @get:Arg val passing: Boolean by argExtra()
}
```

Values are taken lazily and kept as fields, but there are still saved if `ActivityStarter.save(this)` is called in`onSaveInstanceState`. When all properties are provided by delegate, then there is no need to call `ActivityStarter.fill(this, savedInstanceState)` in `onCreate`.

## Parceler

Since version 0.70, there is native support for [Parceler library](https://github.com/johncarl81/parceler). To wrap and unwrap parameter using Parceler, use `Arg` annotation with `parceler` property set to `true`:

```
@Arg(parceler = true) StudentParcel studentParceler;
```

See example [here](https://github.com/MarcinMoskala/ActivityStarter/blob/master/sample/app/src/main/java/com/example/activitystarter/parceler/StudentParcelerActivity.java).

## Installation

For Java project add in `build.gradle` file:

```groovy
dependencies {
    compile 'com.marcinmoskala.activitystarter:activitystarter:1.10'
    apt 'com.marcinmoskala.activitystarter:activitystarter-compiler:1.10'
}
```

For Kotlin project add in `build.gradle` file:

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
    compile 'com.marcinmoskala.activitystarter:activitystarter:1.10'
    kapt 'com.marcinmoskala.activitystarter:activitystarter-compiler:1.10'
}
```

If you want to use Kotlin-specific elements (property delegate `argExtra`), then add in `build.gradle` file:

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
    compile 'com.marcinmoskala.activitystarter:activitystarter:1.10'
    compile 'com.marcinmoskala.activitystarter:activitystarter-kotlin:1.10'
    kapt 'com.marcinmoskala.activitystarter:activitystarter-compiler:1.10'
}
```

And while library is located on JitPack, remember to add on module build.gradle (unless you already have it):

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

More information on [Installation](https://github.com/MarcinMoskala/ActivityStarter/wiki/Installation) page.

## Other libraries

If you like it, remember to leave the star and check out my other libraries:
 * [PreferenceHolder](https://github.com/MarcinMoskala/PreferenceHolder) - Library for simple SharedPreference management in Kotlin
 * [ArcSeekBar](https://github.com/MarcinMoskala/ArcSeekBar) - Good looking curved Android SeekBar
 * [VideoPlayView](https://github.com/MarcinMoskala/VideoPlayView) - Custom Android view with video player, loader and placeholder image
 * [KotlinAndroidViewBindings](https://github.com/MarcinMoskala/KotlinAndroidViewBindings) - Bindings for properties with simple Kotlin types (Boolean, String) to layout traits (visibility, text).

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
