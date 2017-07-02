package activitystarter;

public class Helpers {

    public static boolean isSubtype(Class<?> subclass, Class<?> superclass) {
        return subclass.isAssignableFrom(superclass);
    }
}
