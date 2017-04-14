package activitystarter.wrapper;

import java.util.ArrayList;

public class WrapperManager {

    ArrayList<ArgWrapper> wrappers;

    private WrapperManager(ArrayList<ArgWrapper> wrappers){
        this.wrappers = wrappers;
    }

    public Class<?> mapingType(Class<?> clazz) {
        return String.class;
    }

    static class Builder {

        ArrayList<ArgWrapper> wrappers = new ArrayList<>();

        Builder() {}

        private Builder(ArrayList<ArgWrapper> wrappers) {
            this.wrappers = wrappers;
        }

        Builder with(ArgWrapper argWrapper) {
            ArrayList<ArgWrapper> newWrappers = new ArrayList<>();
            newWrappers.addAll(wrappers);
            newWrappers.add(argWrapper);
            return new Builder(newWrappers);
        }

        WrapperManager build() {
            return new WrapperManager(wrappers);
        }
    }
}
