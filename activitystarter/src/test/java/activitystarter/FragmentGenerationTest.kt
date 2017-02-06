package activitystarter

import activitystarter.compiler.ActivityStarterProcessor
import com.google.common.truth.Truth.assertAbout
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.JavaSourceSubjectFactory
import com.google.testing.compile.JavaSourceSubjectFactory.javaSource
import com.google.testing.compile.JavaSourcesSubject.SingleSourceAdapter
import org.junit.Test
import javax.tools.JavaFileObject


class FragmentGenerationTest: GenerationTest() {

    @Test
    fun simpleGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainFragment" to """
package com.example.activitystarter;

import android.app.Fragment;

import activitystarter.MakeActivityStarter;

@MakeActivityStarter
public class MainFragment extends Fragment {}
        """

        val afterProcess = "com.example.activitystarter.MainFragmentStarter" to """
package com.example.activitystarter;

public final class MainFragmentStarter {
  public static void fill(MainFragment fragment) {
  }

  public static MainFragment newInstance() {
    MainFragment fragment = new MainFragment();
    return fragment;
  }
}
        """

        processingComparator(beforeProcess, afterProcess)
    }

    @Test
    fun singleArgGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainFragment" to """
package com.example.activitystarter;

import android.app.Fragment;

import activitystarter.Arg;

public class MainFragment extends Fragment {
    @Arg String name;
}
        """

        val afterProcess = "com.example.activitystarter.MainFragmentStarter" to """
package com.example.activitystarter;

import android.os.Bundle;
import java.lang.String;

public final class MainFragmentStarter {
  public static void fill(MainFragment fragment) {
    Bundle arguments = fragment.getArguments();
    if(arguments.containsKey("nameStarterKey")) fragment.name = arguments.getString("nameStarterKey");
  }

  public static MainFragment newInstance(String name) {
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putString("nameStarterKey", name);
    fragment.setArguments(args);
    return fragment;
  }
}
        """.trimMargin()

        processingComparator(beforeProcess, afterProcess)
    }

    @Test
    fun optionalArgGenerationTest() {
        val beforeProcess = "com.example.activitystarter.MainFragment" to """
package com.example.activitystarter;

import android.app.Fragment;

import activitystarter.Arg;
import activitystarter.Optional;

public class MainFragment extends Fragment {
    @Arg @Optional String name;
    @Arg @Optional int id;
}
        """

        val afterProcess = "com.example.activitystarter.MainFragmentStarter" to """
package com.example.activitystarter;

import android.os.Bundle;
import java.lang.String;

public final class MainFragmentStarter {
  public static void fill(MainFragment fragment) {
    Bundle arguments = fragment.getArguments();
    if(arguments.containsKey("nameStarterKey")) fragment.name = arguments.getString("nameStarterKey");
    if(arguments.containsKey("idStarterKey")) fragment.id = arguments.getInt("idStarterKey", -1);
  }

  public static MainFragment newInstance(int id) {
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putInt("idStarterKey", id);
    fragment.setArguments(args);
    return fragment;
  }

  public static MainFragment newInstance(String name, int id) {
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putString("nameStarterKey", name);
    args.putInt("idStarterKey", id);
    fragment.setArguments(args);
    return fragment;
  }

  public static MainFragment newInstance() {
    MainFragment fragment = new MainFragment();
    return fragment;
  }

  public static MainFragment newInstance(String name) {
    MainFragment fragment = new MainFragment();
    Bundle args = new Bundle();
    args.putString("nameStarterKey", name);
    fragment.setArguments(args);
    return fragment;
  }
}
        """.trimMargin()

        processingComparator(beforeProcess, afterProcess)
    }
}