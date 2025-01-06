package coms309.Statistics;

import coms309.Recipe.Recipe;
import coms309.Security.JWTService;
import coms309.Users.UserType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@OpenAPIDefinition(info = @Info(title = "Controller for User comment handling", version = "1.0"))
@RestController
@RequestMapping("api/statistics")
@Tag(name = "Statistics API", description = "Endpoints for retrieving various user and recipe statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    JWTService jwtService;

    @Operation(summary = "Get year-to-date recipes", description = "Retrieves recipes created by the user since the beginning of the year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/year-to-date")
    public ResponseEntity<List<Recipe>> getYearToDate(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            List<Recipe> recipes = statisticsService.getYearToDate(username);
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get last 6 months' recipes", description = "Retrieves recipes created by the user over the last 6 months")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/last6months")
    public ResponseEntity<List<Recipe>> getLastSixMonths(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            List<Recipe> recipes = statisticsService.getLastSixMonths(username);
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get last year's recipes", description = "Retrieves recipes created by the user in the past year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/last-year")
    public ResponseEntity<List<Recipe>> getLastYear(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            List<Recipe> recipes = statisticsService.getLastYear(username);
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get this month's recipes", description = "Retrieves recipes created by the user this month")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/this-month")
    public ResponseEntity<List<Recipe>> getThisMonth(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            List<Recipe> recipes = statisticsService.getThisMonth(username);
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get this week's recipes", description = "Retrieves recipes created by the user this week")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/this-week")
    public ResponseEntity<List<Recipe>> getThisWeek(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            List<Recipe> recipes = statisticsService.getThisWeek(username);
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get recipes within a given date range", description = "Retrieves recipes created by the user within a specified date range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<Recipe>> getWithinRange(@RequestParam String username, @RequestParam String startDate, @RequestParam String endDate, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)){
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Recipe> recipes = statisticsService.getWithinRange(username, start, end);
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get recipes for a specific month", description = "Retrieves recipes created by the user for a specific month and year")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/month")
    public ResponseEntity<List<Recipe>> getRecipeForMonth(@RequestParam String username, @RequestParam int month, @RequestParam int year, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)){
            return ResponseEntity.ok(statisticsService.getRecipeForMonth(username, month, year));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get follower count", description = "Retrieves the number of followers for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved follower count"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/followers")
    public ResponseEntity<Integer> getFollowerCount(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            int count = statisticsService.getFollowerCount(username);
            return ResponseEntity.ok(count);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get following count", description = "Retrieves the number of users a specific user is following")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved following count"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/following")
    public ResponseEntity<Integer> getFollowingCount(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            int count = statisticsService.getFollowingCount(username);
            return ResponseEntity.ok(count);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get average calories", description = "Calculates the average calorie intake per recipe for a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved average calories"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/average-calories")
    public ResponseEntity<Double> averageUserCalories(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            double average = statisticsService.averageCaloriesPerUser(username);
            return ResponseEntity.ok(average);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get number of regular users", description = "Retrieves the total number of regular users (for moderators only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved regular user count"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/regular-users")
    public ResponseEntity<Integer> getNumberOfRegularUsers(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            int regularUsers = statisticsService.getNumberOfRegularUsers();
            return ResponseEntity.ok(regularUsers);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Get recipes per user", description = "Retrieves the number of recipes created by each user (for moderators only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe count per user"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/recipes-per-user")
    public ResponseEntity<Map<String, Integer>> getRecipesPerUser(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)){
            Map<String,Integer> userRecipes = statisticsService.getNumberOfRecipesPerUser();
            return ResponseEntity.ok(userRecipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
