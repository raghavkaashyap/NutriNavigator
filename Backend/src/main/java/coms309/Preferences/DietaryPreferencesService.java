package coms309.Preferences;

import coms309.Recipe.DietaryPreferences;
import coms309.Recipe.DietaryPreferencesUtilities;
import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietaryPreferencesService {
    @Autowired
    private DietaryPreferencesRepo repo;

    public void fillPreferences(Recipe r) {
        int id = repo.getRecipeId(r.getName());
        String dietaryPreferences = listPreferences(id);
        r.setDietaryPreferences(dietaryPreferences);
    }

    public void addPreferences(Recipe r, String preferences) {
        int id = repo.getRecipeId(r.getName());
        DietaryPreferences[] p = DietaryPreferencesUtilities.stringToEnumArray(preferences);
        addToRecipe(id, p);
    }

    public void removePreferences(Recipe r) {
        int id = repo.getRecipeId(r.getName());
        repo.delete(id);
    }

    public void removePreferences(String name) {
        int id = repo.getRecipeId(name);
        repo.delete(id);
    }

    public void removePreferences(int id) {
        repo.delete(id);
    }

    public boolean addToRecipe(int recipeId, DietaryPreferences[] preferences) {
        return repo.insert(recipeId, preferences);
    }

    public boolean addToRecipe(int recipeId, DietaryPreferences preferences) {
        return repo.insert(recipeId, preferences);
    }

    public boolean removeFromRecipe(int recipeId, DietaryPreferences[] preferences) {
        for (DietaryPreferences p : preferences) {
            repo.delete(recipeId, p);
        }
        return true;
    }

    public boolean removeFromRecipe(int recipeId, DietaryPreferences p) {
        return repo.delete(recipeId, p);
    }

    public String listPreferences(int recipeId) {
        List<String> stringList = repo.listAll(recipeId);
        if (stringList.isEmpty()) {
            return null;
        } else if (stringList.size() == 1) {
            return stringList.get(0);
        }
        DietaryPreferences[] rList = new DietaryPreferences[stringList.size()];

        int index = 0;
        for (String s : stringList) {
            rList[index] = DietaryPreferences.valueOf(s);
            index += 1;
        }

        return DietaryPreferencesUtilities.enumArrayToString(rList);
    }
}
