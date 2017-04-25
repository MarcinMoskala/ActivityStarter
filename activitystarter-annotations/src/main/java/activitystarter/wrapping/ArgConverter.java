package activitystarter.wrapping;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ArgConverter<T, R> {

    @Nullable
    public Class<? extends Annotation> requiredAnnotation();

    public R wrap(T toWrap);

    public T unwrap(R wrapped);
}
