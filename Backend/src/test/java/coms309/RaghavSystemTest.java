package coms309;

import coms309.Recipe.RecipeService;
import coms309.Users.UserService;
import coms309.Users.UserType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RaghavSystemTest {

    @LocalServerPort
    int port;

    @Autowired
    UserService userService;

    @Autowired
    RecipeService recipeService;

    @BeforeAll
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        userCreate();
    }

    @Test
    @Order(1)
    public void testAddBookmark(){
        String username = "test_user2";
        int recipeId = 14;
        Response response = RestAssured.given()
                .queryParam("username", username)
                .queryParam("recipeId", recipeId)
                .post("/bookmarks");
        assertEquals(200, response.getStatusCode());
        assertEquals("Added Bookmark", response.getBody().asString());
    }

    @Test
    @Order(2)
    public void testGetBookmarksIsEmpty() {
        Response response = RestAssured.given()
                .queryParam("username", "unit_test_user2")
                .when()
                .get("/bookmarks");

        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        JSONArray bookmarks = new JSONArray();
        assertTrue(bookmarks.isEmpty(), "Expected empty, but bookmarks exist");
    }

    @Test
    @Order(3)
    public void testRemoveBookmarkResponse() {
        Response response = RestAssured.given()
                .queryParam("username", "test_user2")
                .queryParam("recipeId", 14)
                .when()
                .delete("/bookmarks");

        assertEquals("Bookmark deleted", response.getBody().asString());
    }

    @Test
    @Order(4)
    public void testRemoveNonExistentBookmark() {
        String username = "unit_test_user1";
        int recipeId = 999;
        Response response = RestAssured.given()
                .queryParam("username", username)
                .queryParam("recipeId", recipeId)
                .delete("/bookmarks");

        // Assert
        assertEquals(404, response.getStatusCode());
        assertEquals("Bookmark not found", response.getBody().asString());
    }

    //testing comments
    @Test
    @Order(5)
    public void testAddComment(){
        String commenterUsername = "test_user";
        String recipeOwnerUsername = "test_user3";
        int recipeId = 13;
        String comment = "Hello this is a test!";
        Response response = RestAssured.given()
                .queryParam("commenterUsername", commenterUsername)
                .queryParam("recipeOwnerUsername", recipeOwnerUsername)
                .queryParam("recipeId", recipeId)
                .queryParam("comment", comment)
                .post("/comments");
        assertEquals(200, response.getStatusCode());
        assertEquals("Comment added successfully", response.getBody().asString());
    }

    @Test
    @Order(6)
    public void testGetCommentsById(){
        Response response = RestAssured.given()
                .queryParam("recipeId", 13)
                .when()
                .get("/comments/recipeId");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void testGetCommentsByCommenter(){
        Response response = RestAssured.given()
                .queryParam("commenterUsername", "test_user")
                .when()
                .get("/comments/user");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testGetCommentsByRecipeOwner(){
        Response response = RestAssured.given()
                .queryParam("recipeOwnerUsername", "test_user2")
                .when()
                .get("/comments/owner");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void testDeleteCommentByRecipeId(){
        Response response = RestAssured.given()
                .queryParam("recipeId", 13)
                .when()
                .delete("/comments/recipeId");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(12)
    public void testDeleteCommentByCommentId(){
        Response response = RestAssured.given()
                .queryParam("commentId", 2)
                .when()
                .delete("/comments/commentId");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(11)
    public void testDeleteCommentByUser(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .when()
                .delete("/comments/username");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void testUpdateComment(){
        Response response = RestAssured.given()
                .queryParam("commentId", 27)
                .queryParam("newMessage", "Hola. This is an update test!")
                .put("/comments");
        assertEquals(200, response.getStatusCode());
    }
    //end of comment testing

    //testing recommendations
    @Test
    @Order(13)
    public void testGetRecommendations(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .when()
                .get("/api/recommendations");
        assertEquals(200, response.getStatusCode());
    }
    //end of recommendation testing

    //ratings tests
    @Test
    @Order(14)
    public void testGetRatingsByRecipe(){
        Response response = RestAssured.given()
                .queryParam("recipeId", 13)
                .when()
                .get("/api/ratings/all-ratings");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(15)
    public void testGetRatingsByRecipeName(){
        Response response = RestAssured.given()
                .queryParam("recipeName", "Water")
                .when()
                .get("/api/ratings/recipeName");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(16)
    public void testGetRecipesRatedAbove(){
        Response response = RestAssured.given()
                .queryParam("threshold", 3)
                .when()
                .get("/api/ratings/threshold");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(17)
    public void testAverageRating(){
        Response response = RestAssured.given()
                .queryParam("recipeId", 13)
                .when()
                .get("/api/ratings/id/averageRating");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(18)
    public void testAverageRatingByName(){
        Response response = RestAssured.given()
                .queryParam("recipeName", "Water")
                .when()
                .get("/api/ratings/averageRating");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(19)
    public void testGetUserRatingByRecipeName(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .queryParam("recipeName", "Water")
                .when()
                .get("/api/ratings/user/recipeName");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(20)
    public void testUpdateRating(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .queryParam("recipeId", 13)
                .queryParam("newRating", 4)
                .when()
                .put("/api/ratings/recipeId");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(21)
    public void testUpdateRatingByName(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .queryParam("recipeName", "Water")
                .queryParam("newRating", 4)
                .when()
                .put("/api/ratings/recipeName");
        assertEquals(200, response.getStatusCode());
    }

    //testing recipes
    @Test
    @Order(22)
    public void testGetRecipes(){
        Response response = RestAssured.given()
                .queryParam("username", "test_moderator")
                .header("Authorization", "Bearer " + "zBZo/ECOhzOhTQ4NPJ15fs1a0uHBdVrJXMe/2h4Tb5mz3yE2MfBPpEW/Niolrr30yFbPTaiGID0QliSeWHK4ZJDnvoTX+PVbvrSqYWOzE/NsBOe0TzJBGicO9TeJU4xRLimpnEkTT4aY1Bg3OgmppMrJDLftXttfT502SCJYPYs8wQ2jZA8ci5RSgO8Z/w/2szj04B96GsCOXJbxdX2cSmbKC3zUntPsWVl0M0cfN/HwXVCxopDHf6CCxu71rTHAJjEZJd+5tjaxEPkWNOPMjQ==")
                .when()
                .get("/api/recipes");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(23)
    public void testGetRecipeById(){
        Response response = RestAssured.given()
                .queryParam("username", "test_moderator")
                .queryParam("id", 14)
                .header("Authorization", "Bearer " + "zBZo/ECOhzOhTQ4NPJ15fs1a0uHBdVrJXMe/2h4Tb5mz3yE2MfBPpEW/Niolrr30yFbPTaiGID0QliSeWHK4ZJDnvoTX+PVbvrSqYWOzE/NsBOe0TzJBGicO9TeJU4xRLimpnEkTT4aY1Bg3OgmppMrJDLftXttfT502SCJYPYs8wQ2jZA8ci5RSgO8Z/w/2szj04B96GsCOXJbxdX2cSmbKC3zUntPsWVl0M0cfN/HwXVCxopDHf6CCxu71rTHAJjEZJd+5tjaxEPkWNOPMjQ==")
                .when()
                .get("/api/recipes");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(24)
    public void testGetRecipeByName(){
        Response response = RestAssured.given()
                .queryParam("username", "test_moderator")
                .header("Authorization", "Bearer " + "zBZo/ECOhzOhTQ4NPJ15fs1a0uHBdVrJXMe/2h4Tb5mz3yE2MfBPpEW/Niolrr30yFbPTaiGID0QliSeWHK4ZJDnvoTX+PVbvrSqYWOzE/NsBOe0TzJBGicO9TeJU4xRLimpnEkTT4aY1Bg3OgmppMrJDLftXttfT502SCJYPYs8wQ2jZA8ci5RSgO8Z/w/2szj04B96GsCOXJbxdX2cSmbKC3zUntPsWVl0M0cfN/HwXVCxopDHf6CCxu71rTHAJjEZJd+5tjaxEPkWNOPMjQ==")
                .when()
                .get("/api/recipes/getName/Water");
        assertEquals(200, response.getStatusCode());
    }

    //statistics test
    @Test
    @Order(25)
    public void testYearToDate(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .header("Authorization", "Bearer " + "WxnzfL2qy2EjIxaarzgZd3Cej0YwdZL0S16FQkB/m2qHas3U1+ss+sqKXZhMS0ZmBdiIdPHFQgutDjNcJazUh/d8MLMHwoA4xUswS6hpTb61LTLfFBWV9BTmhvfPdvjHc/gmcdixcYxdQKYroBBBuKS4XWU0iJGvyLU09YJ6rnOaFIPHrlHFzT6o1fdAewF0G3gWYDKO1TN6F8X7YBXBzcUM2FU5vs5eZul02wypaiidORLEPkjUSBtVHX1OPn6k")
                .when()
                .get("api/statistics/year-to-date");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(26)
    public void testLast6Months(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .header("Authorization", "Bearer " + "WxnzfL2qy2EjIxaarzgZd3Cej0YwdZL0S16FQkB/m2qHas3U1+ss+sqKXZhMS0ZmBdiIdPHFQgutDjNcJazUh/d8MLMHwoA4xUswS6hpTb61LTLfFBWV9BTmhvfPdvjHc/gmcdixcYxdQKYroBBBuKS4XWU0iJGvyLU09YJ6rnOaFIPHrlHFzT6o1fdAewF0G3gWYDKO1TN6F8X7YBXBzcUM2FU5vs5eZul02wypaiidORLEPkjUSBtVHX1OPn6k")
                .when()
                .get("api/statistics/last6months");
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(27)
    public void testThisMonth(){
        Response response = RestAssured.given()
                .queryParam("username", "test_user")
                .header("Authorization", "Bearer " + "WxnzfL2qy2EjIxaarzgZd3Cej0YwdZL0S16FQkB/m2qHas3U1+ss+sqKXZhMS0ZmBdiIdPHFQgutDjNcJazUh/d8MLMHwoA4xUswS6hpTb61LTLfFBWV9BTmhvfPdvjHc/gmcdixcYxdQKYroBBBuKS4XWU0iJGvyLU09YJ6rnOaFIPHrlHFzT6o1fdAewF0G3gWYDKO1TN6F8X7YBXBzcUM2FU5vs5eZul02wypaiidORLEPkjUSBtVHX1OPn6k")
                .when()
                .get("api/statistics/this-month");
        assertEquals(200, response.getStatusCode());
    }

    //end of statistics test

    private void userCreate(){
        userService.createUser("unit_test_user1", passwordHash("unit_test_user1"), UserType.USER, "user1@test.com");
        userService.createUser("unit_test_user2", passwordHash("unit_test_user2"), UserType.USER, "user2@test.com");
        userService.createUser("unit_test_moderator1", passwordHash("unit_test_moderator1"), UserType.MODERATOR, "mdtr@test.com");
    }

    @AfterAll
    private void cleanUp(){
        userService.delete("unit_test_user1");
        userService.delete("unit_test_user2");
    }

    private String passwordHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
