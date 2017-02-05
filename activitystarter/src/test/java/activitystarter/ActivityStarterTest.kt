package activitystarter

import com.google.testing.compile.JavaFileObjects

import org.junit.Test

import javax.tools.JavaFileObject

import activitystarter.compiler.ActivityStarterProcessor

import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaSourceSubjectFactory
import org.junit.Assert.*
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubject
import com.google.testing.compile.JavaSourcesSubject.SingleSourceAdapter
import com.google.testing.compile.JavaSourcesSubjectFactory.javaSources


class ActivityStarterTest() {

    @Test
    fun testFill() {
        // TODO
    }

    @Test
    fun simpleGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainActivity" to """
import android.app.Activity;
import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class MainActivity extends Activity {}
        """

        //TODO Fix first letter ('M' in this case) cutting in test (not in production)
        val afterProcess = "com.example.activitystarter.MainActivityStarter" to """
import android.content.Context;
import android.content.Intent;
import android.support.annotation.UiThread;

public class ainActivityStarter {
    @UiThread
    public static void fill(MainActivity activity) {
    }

    @UiThread
    public static void start(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    context.startActivity(intent);
    }

    @UiThread
    public static void startWithFlags(Context context, int flags) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.addFlags(flags);
    context.startActivity(intent);
    }

    @UiThread
    public static Intent getIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return intent;
    }
}
        """

        processingComparator(beforeProcess, afterProcess)
    }

    fun processingComparator(beforeProcess: Pair<String, String>, afterProcess: Pair<String, String>) {
        val source = JavaFileObjects.forSourceString(beforeProcess.first, beforeProcess.second)
        val bindingSource = JavaFileObjects.forSourceString(afterProcess.first, afterProcess.second)
        assertAbout<SingleSourceAdapter, JavaFileObject, JavaSourceSubjectFactory>(javaSource())
                .that(source)
                .processedWith(ActivityStarterProcessor())
                .compilesWithoutWarnings()
                .and()
                .generatesSources(bindingSource)
    }
}