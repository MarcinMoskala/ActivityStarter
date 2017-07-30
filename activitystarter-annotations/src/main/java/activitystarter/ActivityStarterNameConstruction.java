package activitystarter;

public class ActivityStarterNameConstruction {

    public static String getterFieldAccessorName(String fieldName) {
        String capitalizedName = capitalize(fieldName);
        return "getValueOf" + capitalizedName + "From";
    }

    public static String getterFieldCheckerName(String fieldName) {
        String capitalizedName = capitalize(fieldName);
        return "isFilledValueOf" + capitalizedName + "From";
    }

    private static String capitalize(String name) {
        if(name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }
}
