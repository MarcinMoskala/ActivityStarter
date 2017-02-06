package activitystarter

import org.junit.Test

class ServiceGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        val beforeProcess = "com.example.activitystarter.SomeService" to """
package com.example.activitystarter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class SomeService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
        """

        val afterProcess = "com.example.activitystarter.SomeService" to """
// Generated code from ActivityStarter. Do not modify!
package com.example.activitystarter;

import android.content.Context;
import android.content.Intent;

public final class SomeServiceStarter {
  public static void fill(SomeService service, Intent intent) {
  }

  public static Intent getIntent(Context context) {
    Intent intent = new Intent(context, SomeService.class);
    return intent;
  }

  public static void start(Context context) {
    Intent intent = getIntent(context);
    context.startService(intent);
  }
}
        """

        processingComparator(beforeProcess, afterProcess)
    }

    @Test
    fun singleArgGenerationTest() {
        val beforeProcess = "com.example.activitystarter.SomeService" to """
package com.example.activitystarter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class SomeService extends Service {

    @Arg String name = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
        """

        val afterProcess = "com.example.activitystarter.SomeService" to """
// Generated code from ActivityStarter. Do not modify!
package com.example.activitystarter;

import android.content.Context;
import android.content.Intent;
import java.lang.String;

public final class SomeServiceStarter {
  public static void fill(SomeService service, Intent intent) {
    if(intent.hasExtra("nameStarterKey")) service.name = intent.getStringExtra("nameStarterKey");
  }

  public static Intent getIntent(Context context, String name) {
    Intent intent = new Intent(context, SomeService.class);
    intent.putExtra("nameStarterKey", name);
    return intent;
  }

  public static void start(Context context, String name) {
    Intent intent = getIntent(context, name);
    context.startService(intent);
  }
}
        """.trimMargin()

        processingComparator(beforeProcess, afterProcess)
    }
}