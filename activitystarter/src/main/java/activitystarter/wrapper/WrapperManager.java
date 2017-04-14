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
