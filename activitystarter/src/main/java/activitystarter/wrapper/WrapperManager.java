package activitystarter.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static activitystarter.Helpers.isSubtype;

public class WrapperManager {

    private ArrayList<ArgWrapper> wrappers;

    private WrapperManager(ArrayList<ArgWrapper> wrappers){
        this.wrappers = wrappers;
    }

    public Class<?> mappingType(Class<?> clazz) {
        WrapperWithTypes wrapperWithTypes = findProperWrapper(clazz);
        return wrapperWithTypes == null ? null : (Class<?>) wrapperWithTypes.to;
    }

    public Object wrap(Object toWrap) {
        Class<?> toWrapClass = toWrap.getClass();
        WrapperWithTypes wrapperWithTypes = findProperWrapper(toWrapClass);
        if(wrapperWithTypes == null) throw new Error("");
        return wrapperWithTypes.getWrapper().wrap(toWrap);
    }

    private @Nullable WrapperWithTypes findProperWrapper(Class<?> clazz) {
        for (ArgWrapper w: wrappers) {
            Type[] genericInterfaces = w.getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                    Type fromClass = genericTypes[0];
                    Type toClass = genericTypes[1];
                    if(isSubtype((Class<?>) fromClass, clazz)) {
                        return new WrapperWithTypes(fromClass, toClass, w);
                    }
                }
            }
        }
        return null;
    }

    private class WrapperWithTypes {
        @NonNull private Type from;
        @NonNull private Type to;
        @NonNull private ArgWrapper wrapper;

        private WrapperWithTypes(@NonNull Type from, @NonNull Type to, @NonNull ArgWrapper wrapper) {
            this.from = from;
            this.to = to;
            this.wrapper = wrapper;
        }

        @NonNull Type getFrom() {
            return from;
        }

        @NonNull Type getTo() {
            return to;
        }

        @NonNull ArgWrapper getWrapper() {
            return wrapper;
        }
    }

    public static class Builder {

        ArrayList<ArgWrapper> wrappers = new ArrayList<>();

        public Builder() {}

        private Builder(ArrayList<ArgWrapper> wrappers) {
            this.wrappers = wrappers;
        }

        public Builder with(ArgWrapper argWrapper) {
            ArrayList<ArgWrapper> newWrappers = new ArrayList<>();
            newWrappers.addAll(wrappers);
            newWrappers.add(argWrapper);
            return new Builder(newWrappers);
        }

        public WrapperManager build() {
            return new WrapperManager(wrappers);
        }
    }
}
