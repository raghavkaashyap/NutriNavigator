package coms309.Search;

import coms309.Recipe.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Service
public class SearchService {
    public List<Recipe> search(List<Recipe> searchList, String uploadUser, String ingredients, int caloriesLow, int caloriesHigh, String dateStart,
                               String dateEnd, String dietaryRestrictions, String dietaryPreferences) {
        if (searchList == null || searchList.isEmpty()) {
            return null;
        }

        Iterator<Recipe> iter = searchList.iterator();
        while (iter.hasNext()) {
            Recipe r = iter.next();
            if (ingredients != null) {
                if (!containsIngredients(r, ingredients.split(","))) {
                    iter.remove();
                    continue;
                }
            }
            if (caloriesLow != -1) {
                if (r.getCalories() < caloriesLow) {
                    iter.remove();
                    continue;
                }
            }
            if (caloriesHigh != -1) {
                if (r.getCalories() > caloriesHigh) {
                    iter.remove();
                    continue;
                }
            }
            if (dateStart != null) {
                LocalDate recipeDate = r.getDate();
                LocalDate searchDate = LocalDate.parse(dateStart);
                if (!dateIsAfter(recipeDate, searchDate)) {
                    iter.remove();
                    continue;
                }
            }
            if (dateEnd != null) {
                LocalDate recipeDate = r.getDate();
                LocalDate searchDate = LocalDate.parse(dateEnd);
                if (!dateIsBefore(recipeDate, searchDate)) {
                    iter.remove();
                    continue;
                }
            }
            if (dietaryPreferences != null) {
                DietaryPreferences[] recipePreferences = DietaryPreferencesUtilities.stringToEnumArray(r.getDietaryPreferences());
                DietaryPreferences[] preferencesList = DietaryPreferencesUtilities.stringToEnumArray(dietaryPreferences);
                if (!containsDietaryPreferences(recipePreferences, preferencesList)) {
                    iter.remove();
                    continue;
                }
            }

            if (dietaryRestrictions != null) {
                DietaryRestrictions[] recipeRestrictions = DietaryRestrictionsUtilities.stringToEnumArray(r.getDietaryRestrictions());
                DietaryRestrictions[] restrictionsList = DietaryRestrictionsUtilities.stringToEnumArray(dietaryRestrictions);
                if (!containsDietaryRestrictions(recipeRestrictions, restrictionsList)) {
                    iter.remove();
                    continue;
                }
            }
        }
        if (searchList.isEmpty()) {
            return null;
        }
        return searchList;
    }

    public boolean containsIngredients(Recipe recipe, String[] ingredients) {
        String[] recipeIngredients = recipe.getIngredients().split(",");
        HashSet<String> ingredientsSet = new HashSet<>(Arrays.asList(ingredients));
        for (String s : recipeIngredients) {
            if (ingredientsSet.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean dateIsAfter(LocalDate limit, LocalDate date) {
        return limit.isBefore(date);
    }

    public boolean dateIsBefore(LocalDate limit, LocalDate date) {
        return limit.isAfter(date);
    }

    public boolean containsDietaryPreferences(DietaryPreferences[] recipePreferences, DietaryPreferences[] searchPreferences) {
        if (recipePreferences == null || searchPreferences == null) {
            return false;
        }
        HashSet<DietaryPreferences> searchSet = new HashSet<>(Arrays.asList(searchPreferences));
        for (DietaryPreferences p : recipePreferences) {
            if (searchSet.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsDietaryRestrictions(DietaryRestrictions[] recipeRestrictions, DietaryRestrictions[] searchRestrictions) {
        if (recipeRestrictions == null || searchRestrictions == null) {
            return false;
        }
        HashSet<DietaryRestrictions> searchSet = new HashSet<>(Arrays.asList(searchRestrictions));
        for (DietaryRestrictions r : recipeRestrictions) {
            if (searchSet.contains(r)) {
                return true;
            }
        }
        return false;
    }
}
