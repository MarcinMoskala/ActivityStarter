package activitystarter.wrapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import activitystarter.Helpers;

import static activitystarter.Helpers.isSubtype;

public class WrapperManager {

    private ArrayList<ArgWrapper> wrappers;

    private WrapperManager(ArrayList<ArgWrapper> wrappers){
        this.wrappers = wrappers;
    }

    public Class<?> mappingType(Class<?> clazz) {
        for (ArgWrapper w: wrappers) {
            Type[] genericInterfaces = w.getClass().getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                    Type fromClass = genericTypes[0];
                    Type toClass = genericTypes[1];
                    if(isSubtype((Class<?>) fromClass, clazz)) {
                        return (Class<?>) toClass;
                    }
                }
            }
        }
        return null;
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
