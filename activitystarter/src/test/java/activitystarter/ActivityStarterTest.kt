package activitystarter

import activitystarter.compiler.ActivityStarterProcessor
import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubject.SingleSourceAdapter
import org.junit.Test
import javax.tools.JavaFileObject


class ActivityStarterTest() {

    @Test
    fun testFill() {
        // TODO
    }

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
import android.support.annotation.UiThread;

public final class MainActivityStarter {
  @UiThread
  public static void fill(MainActivity activity) {
  }

  @UiThread
  public static Intent getIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }

  @UiThread
  public static void start(Context context) {
    Intent intent = getIntent(context);
    context.startActivity(intent);
  }

  @UiThread
  public static void startWithFlags(Context context, int flags) {
    Intent intent = getIntent(context);
    context.startActivity(intent);
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
import android.support.annotation.UiThread;
import java.lang.String;

public final class MainActivityStarter {
  @UiThread
  public static void fill(MainActivity activity) {
    Intent intent = activity.getIntent();
    if(intent.hasExtra("nameArg")) activity.name = intent.getStringExtra("nameArg");
  }

  @UiThread
  public static Intent getIntent(Context context, String name) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("nameArg", name);
    return intent;
  }

  @UiThread
  public static void start(Context context, String name) {
    Intent intent = getIntent(context, name);
    context.startActivity(intent);
  }

  @UiThread
  public static void startWithFlags(Context context, String name, int flags) {
    Intent intent = getIntent(context, name);
    context.startActivity(intent);
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
import android.support.annotation.UiThread;
import java.lang.String;

public final class MainActivityStarter {
  @UiThread
  public static void fill(MainActivity activity) {
    Intent intent = activity.getIntent();
    if(intent.hasExtra("nameArg")) activity.name = intent.getStringExtra("nameArg");
    if(intent.hasExtra("idArg")) activity.id = intent.getIntExtra("idArg", -1);
  }

  @UiThread
  public static Intent getIntent(Context context, String name, int id) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("nameArg", name);
    intent.putExtra("idArg", id);
    return intent;
  }

  @UiThread
  public static void start(Context context, String name, int id) {
    Intent intent = getIntent(context, name, id);
    context.startActivity(intent);
  }

  @UiThread
  public static void startWithFlags(Context context, String name, int id, int flags) {
    Intent intent = getIntent(context, name, id);
    context.startActivity(intent);
    intent.addFlags(flags);
    context.startActivity(intent);
  }

  @UiThread
  public static Intent getIntent(Context context, int id) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("idArg", id);
    return intent;
  }

  @UiThread
  public static void start(Context context, int id) {
    Intent intent = getIntent(context, id);
    context.startActivity(intent);
  }

  @UiThread
  public static void startWithFlags(Context context, int id, int flags) {
    Intent intent = getIntent(context, id);
    context.startActivity(intent);
    intent.addFlags(flags);
    context.startActivity(intent);
  }

  @UiThread
  public static Intent getIntent(Context context, String name) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra("nameArg", name);
    return intent;
  }

  @UiThread
  public static void start(Context context, String name) {
    Intent intent = getIntent(context, name);
    context.startActivity(intent);
  }

  @UiThread
  public static void startWithFlags(Context context, String name, int flags) {
    Intent intent = getIntent(context, name);
    context.startActivity(intent);
    intent.addFlags(flags);
    context.startActivity(intent);
  }

  @UiThread
  public static Intent getIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
  }

  @UiThread
  public static void start(Context context) {
    Intent intent = getIntent(context);
    context.startActivity(intent);
  }

  @UiThread
  public static void startWithFlags(Context context, int flags) {
    Intent intent = getIntent(context);
    context.startActivity(intent);
    intent.addFlags(flags);
    context.startActivity(intent);
  }
}
        """.trimMargin()

        processingComparator(beforeProcess, afterProcess)
    }

    fun processingComparator(beforeProcess: Pair<String, String>, afterProcess: Pair<String, String>) {
        val source = JavaFileObjects.forSourceString(beforeProcess.first, beforeProcess.second)
        val bindingSource = JavaFileObjects.forSourceString(afterProcess.first, afterProcess.second)
        assertAbout<SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(javaSource())
                .that(source)
                .withCompilerOptions("-Xlint:-processing")
                .processedWith(ActivityStarterProcessor())
                .compilesWithoutWarnings()
                .and()
                .generatesSources(bindingSource)
    }
}