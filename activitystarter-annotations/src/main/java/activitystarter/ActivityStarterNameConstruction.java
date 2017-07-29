package activitystarter;

import com.sun.xml.internal.ws.util.StringUtils;

public class ActivityStarterNameConstruction {

    public static String getterFieldAccessorName(String fieldName) {
        String capitalizedName = StringUtils.capitalize(fieldName);
        return "getValueOf" + capitalizedName + "From";
    }

    public static String getterFieldCheckerName(String fieldName) {
        String capitalizedName = StringUtils.capitalize(fieldName);
        return "isFilledValueOf" + capitalizedName + "From";
    }
}
