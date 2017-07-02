package activitystarter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import activitystarter.wrapping.ArgConverter;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) @Target(TYPE)
public @interface ActivityStarterConfig {

    Class<? extends ArgConverter<?, ?>>[] converters() default {};
}
