package coms309.Users;

import coms309.Security.JWTService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(info = @Info(title = "Controller for User Management", version = "1.0"))
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @Operation(summary = "Update User Email", description = "Allows a user to change their email")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "ACCEPTED", description = "Email was updated successfully")
    @PostMapping("/user/changeemail")
    public ResponseEntity<String> updateEmail(@RequestParam String username, @RequestParam String newemail, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>("Action not permitted", HttpStatus.FORBIDDEN);
        }
        if (userService.setUserEmail(username, newemail)) {
            return new ResponseEntity<>("Email successfully changed", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Action could not be completed", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Update User Password", description = "Allows a user to change their password")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "ACCEPTED", description = "Password was updated successfully")
    @PostMapping("/user/changepassword")
    public ResponseEntity<String> updatePassword(@RequestParam String username, @RequestParam String newpassword, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>("Action not permitted", HttpStatus.FORBIDDEN);
        }
        if (userService.setUserPassword(username, newpassword)) {
            return new ResponseEntity<>("Password successfully changed", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Action could not be completed", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Delete User", description = "Allows a user to delete their own account")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The admin action is not permitted because of a bad token, or the requester is not an admin")
    @ApiResponse(responseCode = "ACCEPTED", description = "Account was deleted")
    @PostMapping("/user/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token is missing", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIdentity(username, token)) {
            return new ResponseEntity<>("Action not permitted", HttpStatus.FORBIDDEN);
        }
        if (userService.delete(username)) {
            return new ResponseEntity<>("Account successfully deleted", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Account could not be deleted", HttpStatus.BAD_REQUEST);
    }
}
