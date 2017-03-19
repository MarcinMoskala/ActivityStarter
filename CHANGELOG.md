Change Log
==========

Version 0.02 *(2017-02-25)*
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