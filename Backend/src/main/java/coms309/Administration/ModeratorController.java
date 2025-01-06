package coms309.Administration;

import coms309.APIResponse;
import coms309.Security.JWTService;
import coms309.Users.User;
import coms309.Users.UserService;
import coms309.Users.UserType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.PrimitiveIterator;

@OpenAPIDefinition(info = @Info(title = "Controller for Moderator level interactions", version = "1.0"))
@RestController
public class ModeratorController {
    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @Operation(summary = "User Ban", description = "Allows a moderator to ban a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token, or the requester is not an moderator")
    @ApiResponse(responseCode = "CONFLICT", description = "The moderator action is not permitted the requested ban time is too long")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/moderator/banuser")
    public ResponseEntity<String> banUser(@RequestParam String username, @RequestParam String targetuser, @RequestParam int bantime, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.MODERATOR, token)) {
            return new ResponseEntity<>("Moderator action not authorized", HttpStatus.FORBIDDEN);
        }
        //Moderators can only ban users for up to 3 days
        long currentTime = System.currentTimeMillis() / 1000L;
        if ((long) bantime - currentTime > 259200) {
            return new ResponseEntity<>("User ban time is too high for moderator permission", HttpStatus.CONFLICT);
        }
        if (userService.setUserBanTime(targetuser, bantime)) {
            return new ResponseEntity<>("Set user ban time to " + bantime, HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "User Unban", description = "Allows a moderator to unban a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token, or the requester is not an moderator")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @PostMapping("/moderator/unbanuser")
    public ResponseEntity<String> unbanUser(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.MODERATOR, token)) {
            return new ResponseEntity<>("Moderator action not authorized", HttpStatus.FORBIDDEN);
        }
        if (userService.setUserBanTime(targetuser, 0)) {
            return new ResponseEntity<>("Set user ban time to 0", HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not complete action", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Get User", description = "Allows a moderator to retrieve information about a specified user")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token, or the requester is not an moderator")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/moderator/getuser")
    public ResponseEntity<APIResponse<User>> getUserInfo(@RequestParam String username, @RequestParam String targetuser, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token not found"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.MODERATOR, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Moderator action not authorized"), HttpStatus.FORBIDDEN);
        }
        User foundUser = userService.findUser(targetuser);
        if (foundUser != null) {
            return new ResponseEntity<>(new APIResponse<>(foundUser, "User found"), HttpStatus.FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "User not found"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "List Recent Users", description = "Allows a moderator to list the most recently logged in users")
    @ApiResponse(responseCode = "BAD_REQUEST", description = "Token is missing or the action failed")
    @ApiResponse(responseCode = "FORBIDDEN", description = "The moderator action is not permitted because of a bad token, or the requester is not an moderator")
    @ApiResponse(responseCode = "OK", description = "Action was completed successfully")
    @GetMapping("/moderator/listrecentusers")
    public ResponseEntity<APIResponse<List<User>>> listRecentUsers(@RequestParam String username, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return new ResponseEntity<>(new APIResponse<>(null, "Token not found"), HttpStatus.BAD_REQUEST);
        }
        if (!jwtService.checkUserIsType(username, UserType.MODERATOR, token)) {
            return new ResponseEntity<>(new APIResponse<>(null, "Moderator action not authorized"), HttpStatus.FORBIDDEN);
        }
        List<User> userList = userService.getRecentUsers();
        if (userList != null) {
            return new ResponseEntity<>(new APIResponse<>(userList, "Users found"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new APIResponse<>(null, "No users found"), HttpStatus.NOT_FOUND);
    }
}
