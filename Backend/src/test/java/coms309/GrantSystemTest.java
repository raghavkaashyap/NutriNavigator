package coms309;

import coms309.Plan.PlanService;
import coms309.Recipe.Recipes;
import coms309.UserRecipes.UserRecipesService;
import coms309.Users.User;
import coms309.Users.UserService;
import coms309.Users.UserType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Before;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GrantSystemTest {
    @Autowired
    private UserService userService;

    @Autowired
    private Recipes recipeService;

    @Autowired
    private UserRecipesService userRecipesService;

    @Autowired
    private PlanService planService;

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        sqlSetup();
    }

    @Test
    public void testRegisterPage() {
        Response response = given()
                .param("username", "unit_test_user1")
                .param("password", "unit_test_user1")
                .param("email", "user1@unittest.com")
                .post("/login/register");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testLoginPage() {
        Response response = given()
                .param("username", "unit_test_admin1")
                .param("password", "unit_test_admin1")
                .when()
                .post("/login");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String stringResponse = response.getBody().asString();
        assertNotNull(stringResponse);
    }

    @Test
    public void testAdminGetUserInfo() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_admin1")
                .when()
                .get("/admin/getuser");
        int statusCode = response.getStatusCode();
        assertEquals(302, statusCode);
    }

    @Test
    public void testAdminListRecentUsers() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .when()
                .get("/admin/listrecentusers");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testModeratorListRecentUsers() {
        String jwt = getToken("unit_test_moderator1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_moderator1")
                .when()
                .get("/moderator/listrecentusers");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testUpdatePassword() {
        String jwt = getToken("unit_test_user3");
        User user1 = userService.findUser("unit_test_user3");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_user3")
                .param("newpassword", "test_new_password")
                .post("/user/changepassword");
        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode);
        User user2 = userService.findUser("unit_test_user3");
        assertNotEquals(user1.getPasswordHash(), user2.getPasswordHash());
    }

    @Test
    public void testUpdateEmail() {
        String jwt = getToken("unit_test_user2");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_user2")
                .param("newemail", "new_unit_test_email@gmail.com")
                .post("/user/changeemail");
        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode);
        User user = userService.findUser("unit_test_user2");
        assertEquals("new_unit_test_email@gmail.com", user.getEmail());
    }

    @Test
    public void testDeleteAccount() {
        String jwt = getToken("unit_test_user2");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_user2")
                .post("/user/delete");
        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode);
        assert(!userService.userExists("unit_test_user2"));
    }

    @Test
    public void testPromoteUserToMod() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_user3")
                .post("/admin/promote");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        User testUser = userService.findUser("unit_test_user3");
        assertEquals(testUser.getUserType(), UserType.MODERATOR);
    }

    @Test
    public void testDemoteModToUser() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_moderator2")
                .post("/admin/demote");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        User testUser = userService.findUser("unit_test_moderator2");
        assertEquals(testUser.getUserType(), UserType.USER);
    }

    @Test
    public void testDeleteUser() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_user3")
                .post("/admin/deleteuser");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        assert(!userService.userExists("unit_test_user3"));
    }

    @Test
    public void testBanUser() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_user2")
                .param("bantime", 98345345)
                .post("/admin/banuser");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        User testUser = userService.findUser("unit_test_user2");
        assert(testUser.getBanTime() == 98345345);
    }

    @Test
    public void testUnbanUser() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_user2")
                .post("/admin/unbanuser");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        User testUser = userService.findUser("unit_test_user2");
        assert(testUser.getBanTime() == 0);
    }

    @Test
    public void testGetUserInfo() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .param("targetuser", "unit_test_user2")
                .get("/admin/getuser");
        int statusCode = response.getStatusCode();
        assertEquals(302, statusCode);
        String name = response.jsonPath().getString("payload.username");
        String email = response.jsonPath().getString("payload.email");
        assert(name.equals("unit_test_user2"));
        assert(email.equals("user2@test.com"));
    }

    @Test
    public void testListRecentUsers() {
        String jwt = getToken("unit_test_admin1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_admin1")
                .get("/admin/listrecentusers");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testModeratorBanUser() {
        String jwt = getToken("unit_test_moderator1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_moderator1")
                .param("targetuser", "unit_test_user2")
                .param("bantime", 98345345)
                .post("/moderator/banuser");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        User testUser = userService.findUser("unit_test_user2");
        assert(testUser.getBanTime() == 98345345);
    }

    @Test
    public void testModeratorUnbanUser() {
        String jwt = getToken("unit_test_moderator1");
        Response response = given()
                .header("Authorization", "Bearer " + jwt)
                .param("username", "unit_test_moderator1")
                .param("targetuser", "unit_test_user2")
                .post("/moderator/unbanuser");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        User testUser = userService.findUser("unit_test_user2");
        assert(testUser.getBanTime() == 0);
    }

    @After
    public void tearDown() {
        sqlReset();
    }

    private String pwHash(String pw) {
        return BCrypt.hashpw(pw, BCrypt.gensalt());
    }

    private void sqlSetup() {
        userService.createUser("unit_test_moderator1", pwHash("unit_test_moderator1"), UserType.MODERATOR, "moderator1@test.com");
        userService.createUser("unit_test_moderator2", pwHash("unit_test_moderator2"), UserType.MODERATOR, "moderator2@test.com");
        userService.createUser("unit_test_admin1", pwHash("unit_test_admin1"), UserType.ADMIN, "admin1@test.com");
        userService.createUser("unit_test_user2", pwHash("unit_test_user2"), UserType.USER, "user2@test.com");
        userService.createUser("unit_test_user3", pwHash("unit_test_user3"), UserType.USER, "user3@test.com");
        userService.createUser("unit_test_user4", pwHash("unit_test_user4"), UserType.USER, "user4@test.com");
    }

    private void sqlReset() {
        userService.delete("unit_test_user1");
        userService.delete("unit_test_user2");
        userService.delete("unit_test_user3");
        userService.delete("unit_test_user4");
        userService.delete("unit_test_moderator1");
        userService.delete("unit_test_moderator2");
        userService.delete("unit_test_admin1");
    }

    private String getToken(String user) {
        String token = given().param("username", user)
                .param("password", user)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        return token;
    }
}
