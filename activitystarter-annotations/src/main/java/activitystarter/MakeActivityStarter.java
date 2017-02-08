package activitystarter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Bind a field to the view for the specified ID. The view will automatically be cast to the field
 * type.
 * <pre><code>
 * {@literal @}Arg(R.id.title) TextView title;
 * </code></pre>
 */
@Retention(RUNTIME) @Target(TYPE)
public @interface MakeActivityStarter {}
