package activitystarter

import org.junit.Test

class ActivityGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainActivity" to """
package com.example.activitystarter;

import android.app.Activity;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class MainActivity extends Activity {}
        """

        val afterProcess = "com.example.activitystarter.MainActivityStarter" to """
// Generated code from ActivityStarter. Do not modify!
package com.example.activitystarter;

import android.content.Context;
import android.content.Intent;

public final class MainActivityStarter {
  
  public static void fill(MainActivity activity) {
  }
  
  public static Intent getIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }
  
  public static void start(Context context) {
    Intent intent = getIntent(context);
    context.startActivity(intent);
  }
  
  public static void startWithFlags(Context context, int flags) {
    Intent intent = getIntent(context);
    intent.addFlags(flags);
    context.startActivity(intent);
  }
}
        """

        processingComparator(beforeProcess, afterProcess)
    }

    @Test
    fun singleArgGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainActivity" to """
package com.example.activitystarter;
import android.app.Activity;
import activitystarter.Arg;

public class MainActivity extends Activity {
    @Arg String name;
}
        """

        val afterProcess = "com.example.activitystarter.MainActivityStarter" to """
// Generated code from ActivityStarter. Do not modify!
package com.example.activitystarter;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

public final class MainActivityStarter {
  
  public static void fill(MainActivity activity) {
    Intent intent = activity.getIntent();
    if(intent.hasExtra("nameStarterKey")) activity.name = intent.getStringExtra("nameStarterKey");
  }

  public static Intent getIntent(Context context, String name) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("nameStarterKey", name);
    return intent;
  }
  
  public static void start(Context context, String name) {
    Intent intent = getIntent(context, name);
    context.startActivity(intent);
  }

  public static void startWithFlags(Context context, String name, int flags) {
    Intent intent = getIntent(context, name);
    intent.addFlags(flags);
    context.startActivity(intent);
  }
}
        """.trimMargin()

        processingComparator(beforeProcess, afterProcess)
    }

    @Test
    fun optionalArgGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainActivity" to """
package com.example.activitystarter;
import android.app.Activity;
import activitystarter.Arg;
import activitystarter.Optional;

public class MainActivity extends Activity {
    @Arg @Optional String name;
    @Arg @Optional int id;
}
        """

        val afterProcess = "com.example.activitystarter.MainActivityStarter" to """
package com.example.activitystarter;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

public final class MainActivityStarter {
  public static void fill(MainActivity activity) {
    Intent intent = activity.getIntent();
    if(intent.hasExtra("nameStarterKey")) activity.name = intent.getStringExtra("nameStarterKey");
    if(intent.hasExtra("idStarterKey")) activity.id = intent.getIntExtra("idStarterKey", -1);
  }

  public static Intent getIntent(Context context, int id) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("idStarterKey", id);
    return intent;
  }

  public static void start(Context context, int id) {
    Intent intent = getIntent(context, id);
    context.startActivity(intent);
  }

  public static void startWithFlags(Context context, int id, int flags) {
    Intent intent = getIntent(context, id);
    intent.addFlags(flags);
    context.startActivity(intent);
  }

  public static Intent getIntent(Context context, String name, int id) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("nameStarterKey", name);
    intent.putExtra("idStarterKey", id);
    return intent;
  }

  public static void start(Context context, String name, int id) {
    Intent intent = getIntent(context, name, id);
    context.startActivity(intent);
  }

  public static void startWithFlags(Context context, String name, int id, int flags) {
    Intent intent = getIntent(context, name, id);
    intent.addFlags(flags);
    context.startActivity(intent);
  }

  public static Intent getIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }

  public static void start(Context context) {
    Intent intent = getIntent(context);
    context.startActivity(intent);
  }

  public static void startWithFlags(Context context, int flags) {
    Intent intent = getIntent(context);
    intent.addFlags(flags);
    context.startActivity(intent);
  }

  public static Intent getIntent(Context context, String name) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("nameStarterKey", name);
    return intent;
  }

  public static void start(Context context, String name) {
    Intent intent = getIntent(context, name);
    context.startActivity(intent);
  }

  public static void startWithFlags(Context context, String name, int flags) {
    Intent intent = getIntent(context, name);
    intent.addFlags(flags);
    context.startActivity(intent);
  }
}
        """.trimMargin()

        processingComparator(beforeProcess, afterProcess)
    }
}