Change Log
==========

Version 0.5 *(2017-04-05)*
----------------------------

Add startForResult functionality for Activity.

Now on @MakeActivityStarter there is a field includeStartForResult used to make Activities generate startForResult and startWithFlagsForResult mothods. Example:

```
@MakeActivityStarter(includeStartForResult = true)
public class MainActivity extends Activity {}
```

we can start using:

```
MainActivityStarter.startForResult(activity, code)
```

Version 0.4 *(2017-04-01)*
----------------------------

App is much more tested. There is no more static field in ActivityStarter. Add support to all types. All supported types:
* Subtype of Parcelable
* Subtype of Serializable
* ArrayList<Integer> intL
* ArrayList<String> strLi
* ArrayList<CharSequence>
* String
* int
* long
* float
* boolean bo
* double
* char
* byte
* short
* CharSequence
* String[]
* int[]
* long[]
* float[]
* boolean[]
* double[]
* char[]
* byte[]
* short[]
* CharSequence[]

Version 0.3 *(2017-02-25)*
----------------------------

Now custom keys are allowed. Example:

``` kotlin
public class MainActivity extends Activity {
    @Arg(key = "SOME_KEY") String name;
}
```

Also default keys were changed to include file package. Tests were improved. 

Version 0.2 *(2017-02-15)*
----------------------------

Release provided Activity support for saving state, annotation to omit that and better support for fields accesses by setter and getter.

Version 0.1 *(2017-02-08)*
----------------------------

Library is well-tested and stable. Checked also on real-life big project. Library contain support to:   
 * Activities
 * Fragments (+v4)
 * Services
 * BroadcastReceivers
 * Also fully functional in Kotlin (used in big project).
