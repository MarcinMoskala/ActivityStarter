Change Log
==========

Version 0.04 *(2017-04-01)*
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

Version 0.03 *(2017-02-25)*
----------------------------

Now custom keys are allowed. Example:

``` kotlin
public class MainActivity extends Activity {
    @Arg(key = "SOME_KEY") String name;
}
```

Also default keys were changed to include file package. Tests were improved. 

Version 0.02 *(2017-02-15)*
----------------------------

Release provided Activity support for saving state, annotation to omit that and better support for fields accesses by setter and getter.

Version 0.01 *(2017-02-08)*
----------------------------

Library is well-tested and stable. Checked also on real-life big project. Library contain support to:   
 * Activities
 * Fragments (+v4)
 * Services
 * BroadcastReceivers
 * Also fully functional in Kotlin (used in big project).