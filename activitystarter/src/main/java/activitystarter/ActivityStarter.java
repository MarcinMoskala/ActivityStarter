package activitystarter;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ActivityStarter {

    public static void fill(@NonNull Activity target) {
        innerFill(target, null, Bundle.class);
    }

    public static void fill(@NonNull Activity target, @Nullable Bundle savedInstanceState) {
        innerFill(target, savedInstanceState, Bundle.class);
    }

    public static void fill(@NonNull Fragment target) {
        innerFill(target, null, Bundle.class);
    }

    public static void fill(@NonNull Fragment target, @Nullable Bundle savedInstanceState) {
        innerFill(target, savedInstanceState, Bundle.class);
    }

    public static void fill(@NonNull android.support.v4.app.Fragment target) {
        innerFill(target, null, Bundle.class);
    }

    public static void fill(@NonNull android.support.v4.app.Fragment target, @Nullable Bundle savedInstanceState) {
        innerFill(target, savedInstanceState, Bundle.class);
    }

    public static void fill(@NonNull Service target, @NonNull Intent intent) {
        innerFill(target, intent, Intent.class);
    }

    public static void fill(@NonNull BroadcastReceiver target, @NonNull Intent intent) {
        innerFill(target, intent, Intent.class);
    }

    public static void save(@NonNull Activity target, Bundle bundle) {
        Class<?> targetClass = target.getClass();
        Class<?> starterClass = getStarterClass(targetClass);
        if (starterClass == null) return;
        Method method = getMethod(starterClass, "save", targetClass, Bundle.class);
        if (method == null) return;
        invokeMethod(method, target, bundle);
    }

    public static void save(@NonNull Fragment target, Bundle bundle) {
        Class<?> targetClass = target.getClass();
        Class<?> starterClass = getStarterClass(targetClass);
        if (starterClass == null) return;
        Method method = getMethod(starterClass, "save", targetClass, Bundle.class);
        if (method == null) return;
        invokeMethod(method, target, bundle);
    }

    public static void save(@NonNull android.support.v4.app.Fragment target, Bundle bundle) {
        Class<?> targetClass = target.getClass();
        Class<?> starterClass = getStarterClass(targetClass);
        if (starterClass == null) return;
        Method method = getMethod(starterClass, "save", targetClass, Bundle.class);
        if (method == null) return;
        invokeMethod(method, target, bundle);
    }

    private static void innerFill(@NonNull Object target) {
        Class<?> targetClass = target.getClass();
        Class<?> starterClass = getStarterClass(targetClass);
        if (starterClass == null) return;
        Method method = getMethod(starterClass, "fill", targetClass);
        if (method == null) return;
        invokeMethod(method, target);
    }

    private static void innerFill(@NonNull Object target, Object otherArg, Class<?> otherClass) {
        Class<?> targetClass = target.getClass();
        Class<?> starterClass = getStarterClass(targetClass);
        if (starterClass == null) return;
        Method method = getMethod(starterClass, "fill", targetClass, otherClass);
        if (method == null) return;
        invokeMethod(method, target, otherArg);
    }

    private static Class<?> getStarterClass(Class<?> targetClass) {
        String clsName = targetClass.getName();
        String starterName = clsName + "Starter";
        try {
            return Class.forName(starterName);
        } catch (ClassNotFoundException e) {
            // No binding for this class
            return null;
        }
    }

    private static Method getMethod(Class<?> starterClass, String methodName, Class<?>... classes) {
        try {
            return starterClass.getMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find " + methodName + " method for " + starterClass.getName(), e);
        }
    }

    private static void invokeMethod(@NonNull Method method, @NonNull Object... args) {
        try {
            method.invoke(null, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + method + "because of illegal access", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unable to invoke " + method + " because of illegal argument", e);
        } catch (InvocationTargetException e) {
            throwInvokeMethodError(e);
        }
    }

    private static void throwInvokeMethodError(InvocationTargetException e) {
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
