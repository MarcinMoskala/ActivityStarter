package android.support.annotation;

import android.os.Looper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Denotes that the annotated method or constructor should only be called on the
 * UI thread. If the annotated element is a class, then all methods in the class
 * should be called on the UI thread.
 * <p>
 * Example:
 *
 * <pre>
 * <code>
 *  &#64;UiThread
 *  public abstract void setText(@NonNull String text) { ... }
 * </code>
 * </pre>
 *
 * @memberDoc This method must be called on the thread that originally created
 *            this UI element. This is typically the
 *            {@linkplain Looper#getMainLooper() main thread} of your app.
 * @hide
 */
@Retention(SOURCE)
@Target({METHOD,CONSTRUCTOR,TYPE})
public @interface UiThread {
}
