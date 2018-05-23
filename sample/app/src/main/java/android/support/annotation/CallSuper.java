package android.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Denotes that any overriding methods should invoke this method as well.
 * <p>
 * Example:
 *
 * <pre>
 * <code>
 *  &#64;CallSuper
 *  public abstract void onFocusLost();
 * </code>
 * </pre>
 *
 * @memberDoc If you override this method you <em>must</em> call through to the
 *            superclass implementation.
 * @hide
 */
@Retention(SOURCE)
@Target({METHOD})
public @interface CallSuper {
}
