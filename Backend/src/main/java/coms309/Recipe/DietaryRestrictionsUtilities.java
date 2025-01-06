package coms309.Recipe;

public class DietaryRestrictionsUtilities {
    public static String enumArrayToString(DietaryRestrictions[] restrictions) {
        StringBuilder builder = new StringBuilder();
        boolean notFirst = false;
        for (DietaryRestrictions dr : restrictions) {
            if (notFirst) {
                builder.append(", ");
            }
            builder.append(dr.name());
            notFirst = true;
        }
        return builder.toString();
    }

    public static DietaryRestrictions[] stringToEnumArray(String restrictions){
        if (restrictions == null) {
            return null;
        }
        String[] values = restrictions.split(",");
        DietaryRestrictions[] returnArray = new DietaryRestrictions[values.length];
        int index = 0;
        for (String s : values) {
            s = s.trim();
            returnArray[index] = DietaryRestrictions.valueOf(s);
            index += 1;
        }
        return returnArray;
    }
}
