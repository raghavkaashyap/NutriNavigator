package coms309.Recommendations;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Period;
import java.util.*;

@Repository
public class RecommendationsRepo {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public List<Recipe> findHighlyRatedRecipes() throws SQLException {
        String query = """
        SELECT r.recipeId, r.recipe, r.ingredients, r.calories, r.date_added, AVG(rr.rating) AS avg_rating,
        GROUP_CONCAT(DISTINCT dp.enum_value) AS dietary_preferences, GROUP_CONCAT(DISTINCT dr.enum_value) AS dietary_restrictions
        FROM Recipes r JOIN RecipeRatings rr ON r.recipeId = rr.recipeId
        LEFT JOIN preferences_join pj ON r.recipeId = pj.recipeId
        LEFT JOIN dietary_preferences dp ON pj.enum = dp.id
        LEFT JOIN restrictions_join rj ON r.recipeId = rj.recipeId
        LEFT JOIN dietary_restrictions dr ON rj.enum = dr.id
        GROUP BY r.recipeId HAVING AVG(rr.rating) > 3""";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next() && i < 3){
                Recipe recipe = new Recipe();
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                String preferences = resultSet.getString("dietary_preferences");
                if (preferences != null) {
                    recipe.setDietaryPreferences(Arrays.asList(preferences.split(",")).toString());
                } else {
                    recipe.setDietaryPreferences(Collections.emptyList().toString());
                }
                String restrictions = resultSet.getString("dietary_restrictions");
                if (restrictions != null) {
                    recipe.setDietaryRestrictions(Arrays.asList(restrictions.split(",")).toString());
                } else {
                    recipe.setDietaryRestrictions(Collections.emptyList().toString());
                }
                recipes.add(recipe);
                i++;
            }
        }
        return recipes;
    }

    public List<Recipe> findRecipeByIngredients(String username) throws SQLException {
        String userRecipesQuery = """
        SELECT r.recipeId, r.ingredients
        FROM Recipes r
        INNER JOIN User_Recipes ur ON r.recipeId = ur.recipeID
        WHERE ur.username = ?""";

        String allRecipesQuery = """
        SELECT r.recipeId, r.recipe, r.ingredients, r.calories, r.date_added,
               GROUP_CONCAT(DISTINCT dp.enum_value) AS dietary_preferences,
               GROUP_CONCAT(DISTINCT dr.enum_value) AS dietary_restrictions
        FROM Recipes r
        LEFT JOIN preferences_join pj ON r.recipeId = pj.recipeId
        LEFT JOIN dietary_preferences dp ON pj.enum = dp.id
        LEFT JOIN restrictions_join rj ON r.recipeId = rj.recipeId
        LEFT JOIN dietary_restrictions dr ON rj.enum = dr.id
        GROUP BY r.recipeId""";

        List<Recipe> userRecipes = new ArrayList<>();
        List<Recipe> allRecipes = new ArrayList<>();
        List<Recipe> matchedRecipes = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement userRecipesStmt = connection.prepareStatement(userRecipesQuery);
             PreparedStatement allRecipesStmt = connection.prepareStatement(allRecipesQuery)) {
            userRecipesStmt.setString(1, username);
            try (ResultSet resultSet = userRecipesStmt.executeQuery()) {
                while (resultSet.next()) {
                    Recipe recipe = new Recipe();
                    recipe.setIngredients(resultSet.getString("ingredients"));
                    userRecipes.add(recipe);
                }
            }

            try (ResultSet resultSet = allRecipesStmt.executeQuery()) {
                while (resultSet.next()) {
                    Recipe recipe = new Recipe();
                    recipe.setName(resultSet.getString("recipe"));
                    recipe.setIngredients(resultSet.getString("ingredients"));
                    recipe.setCalories(resultSet.getInt("calories"));
                    recipe.setDate(resultSet.getDate("date_added").toLocalDate());

                    String preferences = resultSet.getString("dietary_preferences");
                    recipe.setDietaryPreferences(preferences != null
                            ? Arrays.asList(preferences.split(",")).toString()
                            : Collections.emptyList().toString());

                    String restrictions = resultSet.getString("dietary_restrictions");
                    recipe.setDietaryRestrictions(restrictions != null
                            ? Arrays.asList(restrictions.split(",")).toString()
                            : Collections.emptyList().toString());

                    allRecipes.add(recipe);
                }
            }
        }
        for (Recipe recipe : allRecipes) {
            for (Recipe userRecipe : userRecipes) {
                if (matchIngredients(recipe.getIngredients(), userRecipe.getIngredients())) {
                    matchedRecipes.add(recipe);
                    break;
                }
            }
        }

        Collections.shuffle(matchedRecipes);
        return matchedRecipes.stream().limit(3).toList();
    }

    public List<Recipe> findRecipeByPreferences(String username) throws SQLException{
        String query = """
        SELECT r.recipeId, r.recipe, r.ingredients, r.calories, r.date_added,
               GROUP_CONCAT(DISTINCT dp.enum_value) AS dietary_preferences,
               GROUP_CONCAT(DISTINCT dr.enum_value) AS dietary_restrictions
        FROM Recipes r
        LEFT JOIN preferences_join pj ON r.recipeId = pj.recipeId
        LEFT JOIN dietary_preferences dp ON pj.enum = dp.id
        LEFT JOIN restrictions_join rj ON r.recipeId = rj.recipeId
        LEFT JOIN dietary_restrictions dr ON rj.enum = dr.id
        WHERE (
            dp.enum_value IN (
                SELECT dp.enum_value
                FROM dietary_preferences dp
                INNER JOIN preferences_join pj ON dp.id = pj.enum
                INNER JOIN User_Recipes ur ON pj.recipeId = ur.recipeID
                WHERE ur.username = ?
            )
            OR dr.enum_value IN (
                SELECT dr.enum_value
                FROM dietary_restrictions dr
                INNER JOIN restrictions_join rj ON dr.id = rj.enum
                INNER JOIN User_Recipes ur ON rj.recipeId = ur.recipeID
                WHERE ur.username = ?
            )
        )
        GROUP BY r.recipeId
        ORDER BY RAND()
        LIMIT 3""";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next() && i < 3){
                Recipe recipe = new Recipe();
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                String preferences = resultSet.getString("dietary_preferences");
                if (preferences != null) {
                    recipe.setDietaryPreferences(Arrays.asList(preferences.split(",")).toString());
                } else {
                    recipe.setDietaryPreferences(Collections.emptyList().toString());
                }
                String restrictions = resultSet.getString("dietary_restrictions");
                if (restrictions != null) {
                    recipe.setDietaryRestrictions(Arrays.asList(restrictions.split(",")).toString());
                } else {
                    recipe.setDietaryRestrictions(Collections.emptyList().toString());
                }
                recipes.add(recipe);
                i++;
            }
        }
        return recipes;
    }

    public List<Recipe> recommendPopularRecipes() throws SQLException {
        String query = """
        SELECT r.recipeId, r.recipe, r.ingredients, r.calories, r.date_added, GROUP_CONCAT(DISTINCT dp.enum_value) AS dietary_preferences,
        GROUP_CONCAT(DISTINCT dr.enum_value) AS dietary_restrictions FROM Recipes r
        LEFT JOIN preferences_join pj ON r.recipeId = pj.recipeId
        LEFT JOIN dietary_preferences dp ON pj.enum = dp.id
        LEFT JOIN restrictions_join rj ON r.recipeId = rj.recipeId
        LEFT JOIN dietary_restrictions dr ON rj.enum = dr.id
        INNER JOIN popular_recipes_log prl ON r.recipeId = prl.recipeId
        GROUP BY r.recipeId ORDER BY prl.pulls DESC, RAND() LIMIT 3""";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                recipe.setDietaryPreferences(
                        resultSet.getString("dietary_preferences") != null
                                ? Arrays.asList(resultSet.getString("dietary_preferences").split(",")).toString()
                                : Collections.emptyList().toString()
                );
                recipe.setDietaryRestrictions(
                        resultSet.getString("dietary_restrictions") != null
                                ? Arrays.asList(resultSet.getString("dietary_restrictions").split(",")).toString()
                                : Collections.emptyList().toString()
                );
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    private boolean matchIngredients(String recipeIngredients, String userIngredients) {
        Set<String> recipeSet = new HashSet<>(Arrays.asList(recipeIngredients.split(",")));
        Set<String> userSet = new HashSet<>(Arrays.asList(userIngredients.split(",")));
        recipeSet.retainAll(userSet);
        return !recipeSet.isEmpty();
    }

    private List<Recipe> findRecommendations(String query, String username){
        return null;
    }

//    private List<String> fetchDietaryPreferences(Connection connection, int recipeId) throws SQLException {
//        String query = "SELECT dp.enum_value FROM dietary_preferences dp JOIN preferences_join pj ON dp.id = pj.enum WHERE pj.recipeId = ? ";
//        List<String> preferences = new ArrayList<>();
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, recipeId);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                preferences.add(resultSet.getString("enum_value"));
//            }
//        }
//        return preferences;
//    }
//
//    private List<String> fetchDietaryRestrictions(Connection connection, int recipeId) throws SQLException {
//        String query = "SELECT dr.enum_value FROM dietary_restrictions dr JOIN restrictions_join rj ON dr.id = rj.enum WHERE rj.recipeId = ?";
//        List<String> restrictions = new ArrayList<>();
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, recipeId);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                restrictions.add(resultSet.getString("enum_value"));
//            }
//        }
//        return restrictions;
//    }
}