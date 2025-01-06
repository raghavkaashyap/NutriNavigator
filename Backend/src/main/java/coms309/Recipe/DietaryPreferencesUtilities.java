package coms309.Recipe;

public class DietaryPreferencesUtilities {
    public static String enumArrayToString(DietaryPreferences[] restrictions) {
        StringBuilder builder = new StringBuilder();
        boolean notFirst = false;
        for (DietaryPreferences dr : restrictions) {
            if (notFirst) {
                builder.append(", ");
            }
            builder.append(dr.name());
            notFirst = true;
        }
        return builder.toString();
    }

    public static DietaryPreferences[] stringToEnumArray(String restrictions){
        if (restrictions == null) {
            return null;
        }
        String[] values = restrictions.split(",");
        DietaryPreferences[] returnArray = new DietaryPreferences[values.length];
        int index = 0;
        for (String s : values) {
            returnArray[index] = DietaryPreferences.valueOf(s);
            index += 1;
        }
        return returnArray;
    }
}
