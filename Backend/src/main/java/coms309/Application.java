package coms309;

import coms309.Recipe.Recipe;
import coms309.Recipe.RecipeService;
import coms309.Recipe.Recipes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NutriNavigator Spring Boot Application.
 * 
 * @author gnage002
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
