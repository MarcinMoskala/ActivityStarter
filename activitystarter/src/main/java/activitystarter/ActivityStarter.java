package activitystarter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;


public final class ActivityStarter {

    private ActivityStarter() {
        throw new AssertionError("No instances.");
    }

    @UiThread
    public static void fill(@NonNull Activity target) {
        Class<?> targetClass = target.getClass();
        Method method = getMethod(targetClass);

        if(method == null) return;

        try {
            method.invoke(null, target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + method + "because of illegal access", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unable to invoke " + method + " because of illegal argument", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    private static Method getMethod(Class<?> targetClass) {
        String clsName = targetClass.getName();
        try {
            Log.d("MakeActivityStarter", "Looking up binding for " + targetClass.getName());
            Class<?> bindingClass = Class.forName(clsName + "Starter");
            Log.d("MakeActivityStarter", "I found " + bindingClass);
            return bindingClass.getMethod("fill", targetClass);
        } catch (ClassNotFoundException e) {
            // No binding for this class
            return null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find fill method for " + clsName, e);
        }
    }
}
