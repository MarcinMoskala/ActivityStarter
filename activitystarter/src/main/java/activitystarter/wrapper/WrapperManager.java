package activitystarter.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import activitystarter.wrapping.ArgWrapper;

import static activitystarter.Helpers.isSubtype;

public class WrapperManager {

    private List<WrapperWithTypes> wrapperWithTypes;

    private WrapperManager(ArrayList<ArgWrapper> wrappers){
        this.wrapperWithTypes = toWrappersWithTypes(wrappers);
    }

    public Class<?> mappingType(Class<?> clazz) {
        WrapperWithTypes wrapperWithTypes = wrapperByFromClass(clazz);
        return wrapperWithTypes == null ? null : (Class<?>) wrapperWithTypes.to;
    }

    public Object wrap(Object toWrap) {
        Class<?> toWrapClass = toWrap.getClass();
        WrapperWithTypes wrapperWithTypes = wrapperByFromClass(toWrapClass);
        if(wrapperWithTypes == null) throw new Error("");
        return wrapperWithTypes.getWrapper().wrap(toWrap);
    }

    public Object unwrap(Object toWrap) {
        Class<?> toWrapClass = toWrap.getClass();
        WrapperWithTypes wrapperWithTypes = wrapperByToClass(toWrapClass);
        if(wrapperWithTypes == null) throw new Error("");
        return wrapperWithTypes.getWrapper().unwrap(toWrap);
    }

    private @Nullable WrapperWithTypes wrapperByFromClass(Class<?> clazz) {
        for (WrapperWithTypes w: wrapperWithTypes) {
            if(isSubtype((Class<?>) w.from, clazz)) return w;
        }
        return null;
    }

    private @Nullable WrapperWithTypes wrapperByToClass(Class<?> clazz) {
        for (WrapperWithTypes w: wrapperWithTypes) {
            if(isSubtype((Class<?>) w.to, clazz)) return w;
        }
        return null;
    }

    private List<WrapperWithTypes> toWrappersWithTypes(ArrayList<ArgWrapper> wrappers) {
        List<WrapperWithTypes> wrapperWithTypesList = new ArrayList<>();
        for (ArgWrapper w: wrappers) {
            Type[] genericInterfaces = w.getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                    Type fromClass = genericTypes[0];
                    Type toClass = genericTypes[1];
                    wrapperWithTypesList.add(new WrapperWithTypes(fromClass, toClass, w));
                }
            }
        }
        return wrapperWithTypesList;
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
