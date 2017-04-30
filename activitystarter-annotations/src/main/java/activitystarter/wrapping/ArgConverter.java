package activitystarter.wrapping;

public interface ArgConverter<T, R> {

    public R wrap(T toWrap);

    public T unwrap(R wrapped);
}
