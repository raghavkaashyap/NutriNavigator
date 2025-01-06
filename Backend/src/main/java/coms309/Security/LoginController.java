package coms309.Security;

import coms309.Exceptions.JWTException;
import coms309.Users.User;
import coms309.Users.UserService;
import coms309.Users.UserType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@OpenAPIDefinition(info = @Info(title = "Login and User Registration Controller", version = "1.0"))
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    @Operation(summary = "Login", description = "Allows an already registered user to log in with their credentials")
    @ApiResponse(responseCode = "OK", description = "Returns the signed and encrypted token for the user to include with subsequent requests")
    @PostMapping("/login")
    public String loginPage(@RequestParam String username, @RequestParam String password) {
        if (!userService.userExists(username)) {
            return "Error: user does not exist";
        }
        User loginUser = userService.findUser(username);

        if (loginUser.getBanTime() != 0) {
            long unixTime = System.currentTimeMillis() / 1000L;
            if ((long)loginUser.getBanTime() > unixTime) {
                return "User is banned until " + new Date(loginUser.getBanTime() * 1000L);
            }
        }

        String passwordHash = loginUser.getPasswordHash();

        if (!BCrypt.checkpw(password, passwordHash)) {
            return "Error: Password did not match";
        }

        String userToken = null;
        try {
            userToken = jwtService.retrieveUserToken(loginUser);
        } catch (JWTException ex) {
            System.out.println("Exception in JWTService: " + ex.getMessage());
        }

        if (userToken == null) {
            return "Could not generate session token";
        }

        userService.setLastLoginToNow(username);

        return userToken;
    }

    @Operation(summary = "Register", description = "Allows a brand new user to register an account")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "The user already exists, or the request could otherwise not be completed")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/login/register")
    public ResponseEntity<String> registerPage(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        if (userService.userExists(username)) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (userService.createUser(username, hashedPassword, UserType.USER, email)) {
            return new ResponseEntity<>("User created", HttpStatus.OK);
        }
        return new ResponseEntity<>("User could not be created", HttpStatus.BAD_REQUEST);
    }
}
