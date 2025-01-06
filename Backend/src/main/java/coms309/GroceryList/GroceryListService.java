package coms309.GroceryList;

import coms309.Plan.Plan;
import coms309.Plan.PlanRepo;
import coms309.Recipe.Recipe;
import coms309.Recipe.Recipes;
import coms309.UserRecipes.UserRecipeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class GroceryListService {
    @Autowired
    private Recipes recipeRepo;

    @Autowired
    private PlanRepo planRepo;

    public List<String> generateList(int userId) {
        HashSet<String> grocerySet = new HashSet<>();

        List<Plan> planList = planRepo.list(userId);
        for (Plan p : planList) {
            try {
                Recipe r = recipeRepo.getRecipeById(p.getRecipeId());
                String[] ingredientList = r.getIngredients().split(",");
                for (String s : ingredientList) {
                    grocerySet.add(s.trim());
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception in generateList() loop: " + ex.getMessage());
            }
        }

        List<String> returnList = new ArrayList<>();
        returnList.addAll(grocerySet);

        if (!returnList.isEmpty()) {
            return returnList;
        }
        return null;
    }
}
