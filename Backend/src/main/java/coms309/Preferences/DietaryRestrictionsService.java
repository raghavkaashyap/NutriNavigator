package coms309.Preferences;

import coms309.Recipe.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietaryRestrictionsService {
    @Autowired
    private DietaryRestrictionsRepo repo;

    public void fillRestrictions(Recipe r) {
        int id = repo.getRecipeId(r.getName());
        String dietaryRestrictions = listRestrictions(id);
        r.setDietaryRestrictions(dietaryRestrictions);
    }

    public void addRestrictions(Recipe r, String restrictions) {
        int id = repo.getRecipeId(r.getName());
        DietaryRestrictions[] re = DietaryRestrictionsUtilities.stringToEnumArray(restrictions);
        addToRecipe(id, re);
    }

    public void removeRestrictions(Recipe r) {
        int id = repo.getRecipeId(r.getName());
        repo.delete(id);
    }

    public void removeRestrictions(String name) {
        int id = repo.getRecipeId(name);
        repo.delete(id);
    }

    public void removeRestrictions(int id) {
        repo.delete(id);
    }

    public boolean addToRecipe(int recipeId, DietaryRestrictions[] restrictions) {
        return repo.insert(recipeId, restrictions);
    }

    public boolean addToRecipe(int recipeId, DietaryRestrictions restrictions) {
        return repo.insert(recipeId, restrictions);
    }

    public boolean removeFromRecipe(int recipeId, DietaryRestrictions[] restrictions) {
        for (DietaryRestrictions r : restrictions) {
            repo.delete(recipeId, r);
        }
        return true;
    }

    public boolean removeFromRecipe(int recipeId, DietaryRestrictions r) {
        return repo.delete(recipeId, r);
    }

    public String listRestrictions(int recipeId) {
        List<String> stringList = repo.listAll(recipeId);
        if (stringList.isEmpty()) {
            return null;
        } else if (stringList.size() == 1) {
            return stringList.get(0);
        }
        DietaryRestrictions[] rList = new DietaryRestrictions[stringList.size()];

        int index = 0;
        for (String s : stringList) {
            rList[index] = DietaryRestrictions.valueOf(s);
            index += 1;
        }

        return DietaryRestrictionsUtilities.enumArrayToString(rList);
    }
}
