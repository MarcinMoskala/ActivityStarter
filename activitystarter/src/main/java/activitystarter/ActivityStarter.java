package activitystarter;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public final class ActivityStarter {
    private ActivityStarter() {
        throw new AssertionError("No instances.");
    }

    public static void fill(@NonNull Activity target) {
        fill(target, null);
    }

    public static void fill(@NonNull Fragment target) {
        fill(target, null);
    }

    public static void fill(@NonNull android.support.v4.app.Fragment target) {
        fill(target, null);
    }

    public static void fill(@NonNull Service target, @NonNull Intent intent) {
        fill(target, (Object) intent);
    }

    public static void fill(@NonNull BroadcastReceiver target, @NonNull Intent intent) {
        fill(target, (Object) intent);
    }

    private static void fill(@NonNull Object target, @Nullable Object otherArg) {
        Class<?> targetClass = target.getClass();
        Method method = getMethod(targetClass, otherArg);
        if (method == null) return;
        invokeMethod(method, target, otherArg);
    }

    private static Method getMethod(Class<?> targetClass, @Nullable Object otherArg) {
        String clsName = targetClass.getName();
        String starterName = clsName + "Starter";
        try {
            if (otherArg == null) return Class.forName(starterName).getMethod("fill", targetClass);
            else return Class.forName(starterName).getMethod("fill", targetClass, otherArg.getClass());
        } catch (ClassNotFoundException e) {
            // No binding for this class
            return null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find fill method for " + clsName + " with type paramerer/s. ", e);
        }
    }

    private static void invokeMethod(@NonNull Method method, @NonNull Object target, @Nullable Object otherArg) {
        try {
            if (otherArg == null) method.invoke(null, target);
            else method.invoke(null, target, otherArg);
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
}
