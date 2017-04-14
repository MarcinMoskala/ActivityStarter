package activitystarter.wrapper;

public interface ArgWrapper<T, R> {

    public R wrap(T toWrap);

    public T unwrap(R toUnwrap);
}
